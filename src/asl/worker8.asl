// Agent worker in project unloading_truck

/* Initial beliefs*/
capacity(0). //Maximum weight the agent can carry
batery(100). //Agent batery level, between 0 to 100 (0% - 100%)
positionDrop(0,0). //The position on the board for the discharge the box
qtdGoal(10). //Quantity of objects it have the goal to discharge
qtdDischarge(0). //Quantity of objects it have already discharge until now
truckStatus(full). //Each agent have your own truck to discharge, and this parameter informs how the truck is in the current moment
hand_in(none). //If the agent is carrying something
dropLocal(none). //The rigth place to drop the box
qtdTruckDischarge(0). //Qtd discharged by the agent
truckGet(truck2). //The truck the agent is resposable
capacityHelper(0).//Capacity extra from de helper
helper(true). //The variable is false when the agent depends of one helper


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
!goToTruck.

//Go to the recharge
+batery(X) : lowBatery & hand_in(none)
	<- !goToRecharge.
	
+qtdTruck(X)[source(percept)]:true
<- 
	-+truckStatus(X).

//Worker arrived to the drop D with help. In this case, the agent need the confirmation the Helper is arrived to the drop too.
+helper(true): dropLocal(D) & hand_in(box) & at(D)
	<- ?qtdDischarge(X);
		Y = X + 1;
		-+qtdDischarge(Y);
		-+hand_in(none);	
		-+capacityHelper(0);
		-+dropLocal(none);
		!goToTruck.

+msg(M)[source(Ag)] : at(X) & truckGet(Y) & X=Y
	<- .print("O ajudande chegou!!!!");
		-+helper(false);
		-+capacityHelper(M);
		?box(WeightBox, Local);
		!getBox(WeightBox);
		-msg(M)[source(Ag)].
		
+msg(M)[source(Ag)] : at(drop1) | at(drop2)
<- -+helper(true);
	-msg(M)[source(Ag)].

/* Plans */

//Worker arrived to the drop D without help 	
+!at(D): at(D) & dropLocal(D) & hand_in(box) & helper(true)
	<- ?qtdDischarge(X);
		Y = X + 1;
		-+qtdDischarge(Y);
		-+hand_in(none);
		-+dropLocal(none);
		-+capacityHelper(0);
		!goToTruck.

//For general cases
@m1
+!at(P) : at(P)  <-
	true.
//Take a step towards
@m2
+!at(P) : not at(P)
  <- move_towards(P, 8);
  	//?batery(X);
	//Y = X;
  	//-+batery(Y);
    !at(P).
			
//Recharge batery
+!goToRecharge: lowBatery 
				<- !at(garage);
				   -+batery(100).
				   
+!goToRecharge: not lowBatery <- true.

//In the truck
+!goToTruck: not lowBatery & hand_in(none)
			<-  
				?truckGet(Truck);
				!at(Truck);
				?box(WeightBox, Local);
			    !getBox(WeightBox).
			 
+!goToTruck: lowBatery 
	<- !goToRecharge.	   

+!getBox(Weight): canGetBox(Weight) & hand_in(none) & not lowBatery
				<-  -+hand_in(box);
					?box(WeightBox, Local);
			    	-+dropLocal(Local); 
				   	!at(Local).

-!goToTruck: not lowBatery & hand_in(none) 
			<-  .print("FALHOU NO CHECK");
				?truckGet(Truck);
				!at(Truck);
				?box(WeightBox, Local);
			    -+dropLocal(Local); 
			    !getBox(WeightBox).
			   			 

//The plan ask for help for other agent, if the Box Weight (W) is bigger than the agent capacity(C)
+!getBox(Weight): not canGetBox(Weight)
<-  //?truckGet(T);
	//?box(WeightBox, Local);
    //.send(helper0,tell,msg(T, Local));
	.print("Preciso de ajuda!");
	!startCNP(2).
	
	
//////////////////////////////////////////////////////////////////////////////////////////
// start the CNP
@cnp8
+!startCNP(Id) 
   <- // wait participants introduction
      +cnp_state(Id,propose);   // remember the state of the CNP
      .findall(Name,introduction(participant,Name),LP);
      .print("Sending CFP to ",LP);
      .send(LP,tell,cfp(Id));
      .concat("+!contract(",Id,")",Event);
      // the deadline of the CNP is now + 4 seconds, so
      // the event +!contract(Id) is generated at that time
      .at("now +4 seconds", Event).


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
   :  cnp_state(CNPId,propose)
   <- -+cnp_state(CNPId,contract);
      .findall(offer(O,A),propose(CNPId,O)[source(A)],L);
      .print("Offers are ",L);
      L \== []; // constraint the plan execution to at least one offer
      .max(L,offer(WOf,WAg)); // sort offers, the first is the best
      .print("Winner is ",WAg," with ",WOf);
      !announce_result(CNPId,L,WAg);
      -+cnp_state(Id,finished).

-!startCNP(Id) <- 
  !startCNP(Id).
 
// nothing todo, the current phase is not 'propose'
@lc2 +!contract(CNPId).

-!contract(CNPId)
   <- .print("CNP ",CNPId," has failed!").

+!announce_result(_,[],_).
// announce to the winner
+!announce_result(CNPId,[offer(O,WAg)|T],WAg) 
   <- 
      ?truckGet(Truck);
	  ?box(WeightBox, Local);
      //.send(WAg,tell,msg(T, Local));
      .send(WAg,tell,accept_proposal(CNPId, Truck, Local));
      !announce_result(CNPId,T,WAg).
// announce to others
+!announce_result(CNPId,[offer(O,LAg)|T],WAg) 
   <- .send(LAg,tell,reject_proposal(CNPId));
      !announce_result(CNPId,T,WAg).
      
+proposalInviable(true): true
 <- .print("I need do a new cnp!");
 	-proposalInviable(true);
 	.wait(2000);
 	!startCNP(1).