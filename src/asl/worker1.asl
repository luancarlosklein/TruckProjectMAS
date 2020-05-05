// Agent worker in project unloading_truck

/* Initial beliefs*/
id(1).
capacity(5). //Maximum weight the agent can carry
batery(100). //Agent batery level, between 0 to 100 (0% - 100%)
positionDrop(0,0). //The position on the board for the discharge the box
qtdGoal(10). //Quantity of objects it have the goal to discharge
truckStatus(full). //Each agent have your own truck to discharge, and this parameter informs how the truck is in the current moment
hand_in(none). //If the agent is carrying something
dropLocal(none). //The rigth place to drop the box
qtdDischarge(0). //Qtd discharged by the agent
truckGet(truck). //The truck the agent is resposable
capacityHelper(0).//Capacity extra from de helper
helper(true). //The variable is false when the agent depends of one helper
plays(initiator,truck1).
plays(initiator,truck2).  
busy(false).
qtdTruck1(0).
qtdTruck2(0).


/*Initial rules */
//The rule that checks if the agent can take a box to carry 
canGetBox(Weight) :- capacity(X) & capacityHelper(Y) & (X+Y) > Weight.

// The rule that checks the agent's batery level (low level)					
lowBatery :- batery(Y) & Y < 40. 

//For the call for helper (contract net)
all_proposals_received(CNPId) :- 
  .count(introduction(participant,_),NP) & // number of participants
  .count(propose(CNPId,_), NO) &      // number of proposes received
  .count(refuse(CNPId), NR) &         // number of refusals received
  NP = NO + NR.

/* Initial goals */

//Worker arrived to the drop D with help. In this case, the agent need the confirmation the Helper is arrived to the drop too.
+helper(true): dropLocal(D) & hand_in(box) & at(D)
	<- -+hand_in(none);
		-+busy(false);
		-+dropLocal(none);
		-+capacityHelper(0);
		?qtdDischarge(Z);
		E = Z + 1;
		-+qtdDischarge(E);
		?truckGet(X);
		if (X == truck1)
		{
			?qtdTruck1(K);
			L = K + 1;
			-+qtdTruck1(L);
		}
		else
		{
			?qtdTruck2(K);
			L = K + 1;
			-+qtdTruck2(L);
		}
		!goToRecharge.
		
//The helper arrived at the truck.
+msg(M)[source(Ag)] : truckGet(Y) & at(X) & X == Y
	<- .print("O ajudande chegou!!!!");
		-msg(M)[source(Ag)];
		-+helper(false);
		-+capacityHelper(M);
		?box(WeightBox, Local);
		!getBox(WeightBox).
				
//The helper arrive at the drop
+arrived(M)[source(Ag)] : true
<- .print("O ajudante entregou: ", Ag);
   -arrived(M)[source(Ag)];
   -+helper(true).
	

+failureCnp(true)[source(Ag)]: true
<- -failureCnp(true)[source(Ag)];
	.print("O ganhar não está ocupado. Preciso realizar uma nova cnp!");
	.wait(5000);
	!startCNP(2).
	
/* Plans */
//Stop the walk, worker arrived to the drop D without help 	
+!at(D): at(D) & dropLocal(D) & hand_in(box) & helper(true)
	<-  -+hand_in(none);
		-+busy(false);
		-+dropLocal(none);
		-+capacityHelper(0);
		?qtdDischarge(Z);
		E = Z + 1;
		-+qtdDischarge(E);
		?truckGet(X);
		if (X == truck1)
		{
			?qtdTruck1(K);
			L = K + 1;
			-+qtdTruck1(L);
		}
		else
		{
			?qtdTruck2(K);
			L = K + 1;
			-+qtdTruck2(L);
		}
		!goToRecharge.
	
//Stop the walk, for general cases
@m1
+!at(P) : at(P)  
<-	true.
	
//Take a step towards
@m2
+!at(P) : not at(P)
<- ?id(ID); 
  	move_towards(P, ID);
  	?batery(X);
	//Y = X - 4;
  	-+batery(Y);
    !at(P).
			
//Recharge batery
@recharge[atomic]
+!goToRecharge: lowBatery 
<- !at(garage);
	.wait(10000);
   -+batery(100).
				   
+!goToRecharge: not lowBatery <- true.

//In the truck
+!goToTruck(T): true 
<-  !at(T);	
	?box(WeightBox, Local);
	-+dropLocal(Local); 
	!getBox(WeightBox).
			 	   
+!getBox(Weight): canGetBox(Weight)
<-  -+hand_in(box);
	?box(WeightBox, Local);
	-+dropLocal(Local); 
	!at(Local).

-!goToTruck(T): true
<-  .print("Falhou no check. Refazendo o goToTruck");
	!at(T);
	?box(WeightBox, Local);
	-+dropLocal(Local); 
	!getBox(WeightBox).
			   			 
//The plan ask for help for other agent, if the Box Weight (W) is bigger than the agent capacity(C)
+!getBox(Weight): not canGetBox(Weight)
<-  .print("Preciso de ajuda!");
	!startCNP(2).//Call a helper
	
//////////////////////////////////////////////////////////////////////////////////////////
//The contract net/////////
//Asking help

// start the CNP
@cnp9
+!startCNP(Id) 
<-  +cnp_state(Id,propose);   // remember the state of the CNP
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
@r1 +propose(CNPId,Offer):  cnp_state(CNPId,propose) & all_proposals_received(CNPId)
 <- !contract(CNPId).

// receive refusals   
@r2 +refuse(CNPId) :  cnp_state(CNPId,propose) & all_proposals_received(CNPId)
<- !contract(CNPId).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId):  cnp_state(CNPId,propose)
 <-   -+cnp_state(CNPId,contract);
      .findall(offer(O,A),propose(CNPId,O)[source(A)],L);
      .print("Offers are ",L);
      .length(L, S);
      -+qtdOffers(S);
      L \== []; // constraint the plan execution to at least one offer
      .max(L,offer(WOf,WAg)); // sort offers, the first is the best
      .print("Winner is ",WAg," with ",WOf);
      !announce_result(CNPId,L,WAg);
      -+cnp_state(CNPId,finished);
      .abolish(propose(2,_)).

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(CNPId).

-!contract(CNPId)
<- .print("CNP ",CNPId," has failed!").

+!announce_result(_,[],_).

// announce to the winner
+!announce_result(CNPId,[offer(O,WAg)|T],WAg) 
<-  ?truckGet(Truck);
	?box(WeightBox, Local);
    .send(WAg,tell,accept_proposal(CNPId, Truck, Local));
    !announce_result(CNPId,T,WAg).

// announce to others
+!announce_result(CNPId,[offer(O,LAg)|T],WAg) 
<-  .send(LAg,tell,reject_proposal(CNPId));
    !announce_result(CNPId,T,WAg).
      
//////////////////////////////////////////////////////////////////////////////////////////
//Answer trucks (to get a box) (answer a cnp)
 
+plays(initiator,In):  .my_name(Me)
<- .send(In,tell,introduction(participant,Me)).

// answer to Call For Proposal   
@c1T[atomic] 
+cfp(CNPId, X, Y)[source(A)]:  plays(initiator,A) & busy(false) & capacity(Offer)
<- +proposal(CNPId,Offer); // remember my proposal
   .send(A,tell,propose(CNPId,Offer));
   -cfp(CNPId, X, Y)[source(A)].

// Refuse a Call for Proposal
+cfp(CNPId, X, Y)[source(A)]: plays(initiator,A) & busy(true)
<- .send(A,tell,refuse(CNPId));
   .print("Estou ocupado! Não irei responder!");
   -cfp(CNPId, X, Y)[source(A)].
    
//The proposal is accept, but the agent is in another taks. Send a failure to the truck      
@r1Busy[atomic]
+accept_proposal(CNPId, Truck, box(X, Y))[source(A)]:  proposal(CNPId, Offer) & (busy(true) | hand_in(box))
<-  .send(A, tell, failureCnpForTruck(X, Y));
    -accept_proposal(CNPId, Truck, box(X, Y))[source(A)];
   	.print("My proposal '",Offer,"' won CNP ",CNPId,
             " for! BUT I'm busy now. Sorry!'").
         
@r1T
+accept_proposal(CNPId,Truck, box(X, Y))[source(A)]:  proposal(CNPId, Offer)
<-  
	-+busy(true);
	.print("My proposal '",Offer,"' won CNP ",CNPId,
             " for!");        
    -+box(X,Y);
    -accept_proposal(CNPId, Truck, box(X, Y))[source(A)];
    -proposal(CNPId, Offer);
    -+truckGet(Truck);
	!goToTruck(Truck).
	   
@r2T +reject_proposal(CNPId)[source(A)]
<-  .print("I lost CNP ",CNPId, ".");
    -proposal(CNPId,_,_);
    -+busy(false);
    -reject_proposal(CNPId)[source(A)]. // clear memory
