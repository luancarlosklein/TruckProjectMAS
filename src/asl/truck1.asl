// Agent truck in project discharge_truck
/* Initial beliefs and rules */
qtdThings(0).//Qtd inside of this truck
//truckloadCurrently([]).
myPos(0, 9).
start(true).
boxDelivered(true).

//For the call for helper (contract net)
all_proposals_received(CNPId) :- 
  .count(introduction(participant,_),NP) & // number of participants
  .count(propose(CNPId,_), NO) &      // number of proposes received
  .count(refuse(CNPId), NR) &         // number of refusals received
  NP = NO + NR.

/* Initial goals */

//!startCNP(3).
	

@goIn[atomic]
+going(indo)[source(A)]: true
<- .time(H, M, S);
	+hourToGo(H, M, S);
	-going(indo)[source(A)].
	

@goLea[atomic]
+going(saindo)[source(A)]: true
<- .time(H, M, S);
	?hourToGo(Hs, Ms, Ss);
	X = (H - Hs)*3600 + (M - Ms)*60 + (S - Ss);
	.print(" O agente DEMOROU ", X, " segundos PARA CHEGAR E IR EMBORA");
	-going(saindo)[source(A)].


+start(X): true
<- discharge_truck.GenerateTruck.

/* 
//Generate a new truck
@emptyTruck[atomic]
+truckloadCurrently([]): true
<- .print("Caminhão Vazio!");
	discharge_truck.GenerateTruck.
*/
//The winner is already busy, do a new cnp
@puBack[atomic]
+failureCnpForTruck(X, Y)[source(Ag)]: true
<-	+boxPutBack(X, Y, Ag);
	!putBoxBack;
	-failureCnpForTruck(_,_)[source(Ag)];
	//-failureCnpForTruck(X, Y)[source(Ag)];
	.print("PRECISO DE UMA NOVA CNP!");
	.wait(3000).
	//!startCNP(3).

+!sendBox([box(W,D)|T], WAg, CNPId): true
<- 	-+box(W,D);
	?qtdThings(Z);
	Y = Z - 1;
	-+qtdThings(Y);
	.send(WAg,tell,accept_proposal(CNPId, truck1, box(W, D)));
	.print("MANDEI UMA CAIXA");
   -+truckloadCurrently(T).
				 				 
//Get the box, and put it in the charge, again
+!putBoxBack: true
<- 
	?qtdThings(Z);
	Y = Z + 1;
	-+qtdThings(Y);
	discharge_truck.PutBoxBack.

//Make this fot call a worker
+truckloadCurrently(X): qtdThings(Y) & Y == 0
<-  .print("AQUI ACABOU. FINISH. FALICEUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU").


//Make this fot call a worker
+truckloadCurrently(X): qtdThings(Y) & Y > 0 & boxDelivered(true)
<-  .print("Alguem ai pra descarregar t1?");
	.wait(3000);
	-+boxDelivered(false);
	!startCNP(3).
	
// start the CNP
@cnp9
+!startCNP(Id) 
<-  -+cnp_state(Id,propose);   // remember the state of the CNP
    .findall(Name,introduction(participant,Name),LP);
    //.print("Sending CFP to ",LP);
    ?myPos(X, Y);
    .send(LP,tell,cfp(Id, X, Y));
    .concat("+!contract(",Id,")",Event);
    // the deadline of the CNP is now + 4 seconds, so
    // the event +!contract(Id) is generated at that time
    .at("now +4 seconds", Event).

-!startCNP(Id) <- 
  !startCNP(Id).
 
// receive proposal 
// if all proposal have been received, don't wait for the deadline
@r1 +propose(CNPId,Offer):  cnp_state(CNPId,propose) & all_proposals_received(CNPId)
<- !contract(CNPId).

// receive refusals   
@r2 +refuse(CNPId):  cnp_state(CNPId,propose) & all_proposals_received(CNPId)
<- !contract(CNPId).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId)
<- -+cnp_state(CNPId,contract);
   .findall(offer(O,A),propose(CNPId,O)[source(A)],L);
   //.print("Offers are ",L);
   .length(L, S);
   -+qtdOffers(S);
   L \== []; // constraint the plan execution to at least one offer
   .max(L,offer(WOf,WAg)); // sort offers, the first is the best
   .print("Winner is ",WAg," with ",WOf);
   !announce_result(CNPId,L,WAg); 
   -+cnp_state(CNPId,finished);
   .abolish(propose(3,_));
   .abolish(refuse(3,_)).

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(CNPId) <- true.


-!contract(CNPId): qtdThings(Y) & Y > 0
<- .print("CNP ",CNPId," has failed!");
   .wait(5000);
   -+boxDelivered(true); 
   !startCNP(3).
 
-!contract(CNPId): qtdThings(Y) & Y <= 0
<- .print("CNP ",CNPId," has failed! Stop now!").

+!announce_result(_,[],_).
// announce to the winner


+!announce_result(CNPId,[offer(O,WAg)|T],WAg) 
<-  ?truckloadCurrently(L);
	!sendBox(L, WAg, CNPId);      
    //?box(X, Y);
    //.send(WAg,tell,accept_proposal(CNPId, truck1, box(X, Y)));
    !announce_result(CNPId,T,WAg);
    -+boxDelivered(true);.
      
// announce to others
+!announce_result(CNPId,[offer(O,LAg)|T],WAg) 
<- .send(LAg,tell,reject_proposal(CNPId));
   !announce_result(CNPId,T,WAg).



