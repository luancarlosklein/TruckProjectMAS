// Agent truck in project discharge_truck

/* Initial beliefs and rules */
qtdThings(0).//Qtd inside of this truck
truckloadCurrently([]).
myPos(0, 9).

/* Initial goals */


//For the call for helper (contract net)
all_proposals_received(CNPId) :- 
  .count(introduction(participant,_),NP) & // number of participants
  .count(propose(CNPId,_), NO) &      // number of proposes received
  .count(refuse(CNPId), NR) &         // number of refusals received
  NP = NO + NR.

//!startCNP(5).
	
+!sendBox([box(W,D)|T], A): true
<- 
	-+box(W,D);
	-+truckloadCurrently(T).
				 
+msg(X)[source(A)]: true
<- 
?truckloadCurrently(L);
.print("MANDEI UMA CAIXAAAAAAAAAA.   ");
 !sendBox(L, A).


@emptyTruck[atomic]
+truckloadCurrently([]): true
<- .print("Caminhão Vazio!");
	discharge_truck.GenerateTruck.


+!truckloadCurrently(X): true
<-  .print("Alguem ai pra descarregar?");
	.wait(1000);
	!startCNP(5).
	


// start the CNP
@cnp9
+!startCNP(Id) 
   <- 
      -+cnp_state(Id,propose);   // remember the state of the CNP
      .findall(Name,introduction(participant,Name),LP);
      .print("Sending CFP to ",LP);
      .send(LP,tell,cfp(Id));
      .concat("+!contract(",Id,")",Event);
      // the deadline of the CNP is now + 4 seconds, so
      // the event +!contract(Id) is generated at that time
      .at("now +4 seconds", Event).

-!startCNP(Id) <- 
  !startCNP(Id).
 
// receive proposal 
// if all proposal have been received, don't wait for the deadline
@r1 +propose(CNPId,Offer)
   :  cnp_state(CNPId,propose) & all_proposals_received(CNPId)
   <- !contract(CNPId).

// receive refusals   
@r2 +refuse(CNPId) 
   :  cnp_state(CNPId,propose) & all_proposals_received(CNPId)
   <- !contract(CNPId).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId)
   <- 
   	  -+cnp_state(CNPId,contract);
      .findall(offer(O,A),propose(CNPId,O)[source(A)],L);
      .print("Offers are ",L);
      .length(L, S);
      -+qtdOffers(S);
      L \== []; // constraint the plan execution to at least one offer
      .max(L,offer(WOf,WAg)); // sort offers, the first is the best
      .print("Winner is ",WAg," with ",WOf);
      !announce_result(CNPId,L,WAg);
      -+cnp_state(CNPId,finished);
      .abolish(propose(_,_)).

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(CNPId) <- .print("PUTASSSSSSSSSSSSSSSSSSSSSSS").

-!contract(CNPId)
   <- .print("CNP ",CNPId," has failed!");
   	.wait(7000); 
   	!startCNP(5).

+!announce_result(_,[],_).
// announce to the winner

+!announce_result(CNPId,[offer(O,WAg)|T],WAg) 
 <- 		      
 		?truckloadCurrently(L);
		.print("MANDEI UMA CAIXAAAAAAAAAA.   ");
		 !sendBox(L, WAg);      
      //.send(WAg,tell,msg(T, Local));
      ?box(X, Y);
      .send(WAg,tell,accept_proposal(CNPId, truck3, box(X, Y)));
      !announce_result(CNPId,T,WAg);
      .wait(3000);
      !startCNP(5).
      
// announce to others
+!announce_result(CNPId,[offer(O,LAg)|T],WAg) 
   <- .send(LAg,tell,reject_proposal(CNPId));
      !announce_result(CNPId,T,WAg).

