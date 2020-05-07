// Agent truck in project discharge_truck
/* Initial beliefs and rules */
qtdThings(0).//Qtd inside of this truck
myPos(0, 9).//The pos in the map
start(true).//Generate a truck
boxDelivered(true).
confiability(100,100).//Worker0, worker1...

//For the call for helper (contract net)
all_proposals_received(CNPId) :- 
  .count(introduction(participant,_),NP) & // number of participants
  .count(propose(CNPId,_), NO) &      // number of proposes received
  .count(refuse(CNPId), NR) &         // number of refusals received
  NP = NO + NR.

/* Initial goals */
	
+start(X): true
<- discharge_truck.GenerateTruck.

/* //For the infinit
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
	-proposalWin(_, _, _, Ag);
	!putBoxBack;
	-failureCnpForTruck(_,_)[source(Ag)];
	.print("I need a new CNP! The winner agent is busy now! (T1)");
	.wait(3000).

//To send the information about the box for the winner agent
+!sendBox([box(W,D)|T], WAg, CNPId): true
<- 	-+box(W,D);
	?qtdThings(Z);
	Y = Z - 1;
	-+qtdThings(Y);
	.send(WAg,tell,accept_proposal(CNPId, truck1, box(W, D)));
	.print("I send a box! (T1)");
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
<-  .print("The truck is empty now! Nothing tho discharge here (T1)!").

//Make this fot call a worker
+truckloadCurrently(X): qtdThings(Y) & Y > 0
<-  .print("I have a box! Is someone to discharge?(T1)");
	.wait(3000);
	-+boxDelivered(false);
	!startCNP(3).

//Make the truck fanatic to discharge all
+truckloadCurrently(X): true
<- .wait(5000);
   -+truckloadCurrently(X).
	
// start the CNP
@cnp9
+!startCNP(Id) 
<-  -+cnp_state(Id,propose);   // remember the state of the CNP
    .findall(Name,introduction(participant,Name),LP);
    .send(LP,tell,cfp(Id));
    .concat("+!contract(",Id,")",Event);
    // the deadline of the CNP is now + 4 seconds, so
    // the event +!contract(Id) is generated at that time
    .at("now +4 seconds", Event).

//If os somenthing wrong, try to make again
-!startCNP(Id) <- 
  !startCNP(Id).
 
//Call the fuction to select the best propose
+!selectWorker: true
<- discharge_truck.SelectForCnp.

// receive proposal 
// if all proposal have been received, don't wait for the deadline
@r1
+propose(CNPId,Offer,Time)[source(A)]:  
cnp_state(CNPId,propose) & all_proposals_received(CNPId)
<- 
   -propose(CNPId,Offer, Time)[source(A)];
   //Transforme the propose to another form (this is useful to the select, and the diferent name help us to don't have any problem with this)
   +proposeA(CNPId, Offer, Time, A);
	!contract(CNPId).
	
//Same to the other, but is no the final 
@r1T 
+propose(CNPId,Offer, Time)[source(A)]: true
<- 
    -propose(CNPId,Offer, Time)[source(A)];
	+proposeA(CNPId, Offer, Time, A).

// receive refusals   
@r2 +refuse(CNPId):  cnp_state(CNPId,propose) & all_proposals_received(CNPId)
<- !contract(CNPId).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId)
<- -+cnp_state(CNPId,contract);
   .findall(offer(O,A),proposeA(CNPId,O,T,A),L);
   .length(L, S);
   -+qtdOffers(S);
    if(S > 0)
    {
    	!selectWorker;
    	?winnerCnp(WAg, WOf);
    	?proposeA(CNPId, P, Time, WAg);
    	+proposalWin(CNPId, P, Time, WAg);
   		.print("Winner is ",WAg," with ",WOf, " Time: ", Time, " (T1)");
   		!announce_result(CNPId,L,WAg); 
   		-+cnp_state(CNPId,finished);
    }   
   -winnerCnp(_,_);
   .abolish(proposeA(3,_,_,_));
   .abolish(propose(3,_,_,_));
   .abolish(refuse(3)).

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(CNPId) <- true.

//If the cnp has no propose
+qtdOffers(0): qtdThings(Y) & Y > 0
<- .print("CNP has failed!: No prosose (T1)");
   .wait(5000);
   -+boxDelivered(true); 
   !startCNP(3).

//The cnp has failed, but there are box in the truck 
-!contract(CNPId): qtdThings(Y) & Y > 0
<- .print("CNP ",CNPId," has failed! (T1)");
   .wait(5000);
   -+boxDelivered(true); 
   !startCNP(3).
 
//The cnp has failed and there aren't box int he truck
-!contract(CNPId): qtdThings(Y) & Y <= 0
<- .print("CNP ",CNPId," has failed! Stop now!(T1)").

+!announce_result(_,[],_).
// announce to the winner

+!announce_result(CNPId,[offer(O,WAg)|T],WAg) 
<-  ?truckloadCurrently(L);
	!sendBox(L, WAg, CNPId);      
    -+boxDelivered(true)
    !announce_result(CNPId,T,WAg).
      
// announce to others
+!announce_result(CNPId,[offer(O,LAg)|T],WAg) 
<- .send(LAg,tell,reject_proposal(CNPId));
   !announce_result(CNPId,T,WAg).

//To verify the times to the agent
//The time the agent start to coming 
@com[atomic]
+coming(H, M, S)[source(A)]: true
<- 	+hourToGo(H, M, S, A);
	-coming(H, M, S)[source(A)].

//The time the agent is leaving
@gos[atomic]	
+leaving(H, M, S)[source(A)]: true
<- 	
	?proposalWin(CNPId, P, Time, A)
	?hourToGo(Hs, Ms, Ss, A);
	X = (H - Hs)*3600 + (M - Ms)*60 + (S - Ss);
	.print("The agent has to spend ", X, " seconds to get the box");
	//Actualize the confiabily about the agents
	if (A == worker0)
	{
		?confiability(W0, W1);
		Z = W0 + (Time - X);
		-+confiability(Z, W1);
	}
	if (A == worker1)
	{
		?confiability(W0, W1);
		K = W1 + (Time - X);
		-+confiability(W0, K);
	}
	-leaving(H, M, S)[source(A)];
	-proposalWin(CNPId, P, Time, A);
	-hourToGo(Hs, Ms, Ss, A).

