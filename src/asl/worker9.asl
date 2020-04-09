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
qtdTruckDischarge(0).
truckGet(truck).
capacityHelper(0).
helper(true).

/*Initial rules */

//The rule that checks if the agent can take a box to carry 
canGetBox(Weight) :- capacity(X) & capacityHelper(Y) & (X+Y) > Weight.

// The rule that checks the agent's batery level (low level)					
lowBatery :- batery(Y) & Y < 40. 

/* Initial goals */
!goToTruck.

		
//Go to the truck


//+batery(X): not lowBatery & hand_in(none)
//	<- !goToTruck.

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

/* Plans */

//Worker arrived to the drop D without help 	
+!at(D): at(D) & dropLocal(D) & hand_in(box) & helper(true)
	<- ?qtdDischarge(X);
		Y = X + 1;
		-+qtdDischarge(Y);
		-+hand_in(none);
		-+dropLocal(none);
		-+capacityHelper(0);
		.print("SAIDAAAAAAA 333333333333333333333");
		!goToTruck.

//For general cases
@m1
+!at(P) : at(P)  <-
	true.
//Take a step towards
@m2
+!at(P) : not at(P)
  <- move_towards(P, 9);
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
				!at(truck);
				?box(WeightBox, Local);
			    !getBox(WeightBox).
			 
+!goToTruck: lowBatery 
	<- !goToRecharge.	   

/* 
+mand(WeightBox, Local)[source(Truck)]: true
<-   +box(5, drop2);
	//?box(WeightBox, Local);
	-+dropLocal(Local); 
	!getBox(WeightBox);
	-mand(WeightBox, Local)[source(Truck)].	
	
	
	*/
+!getBox(Weight): canGetBox(Weight) & hand_in(none) & not lowBatery
				<-  -+hand_in(box);
					?box(WeightBox, Local);
			    	-+dropLocal(Local); 
				   	!at(Local).

-!goToTruck: not lowBatery & hand_in(none) 
			<-  .print("FALHOU NO CHECK");
				!at(truck);
				?box(WeightBox, Local);
			    -+dropLocal(Local); 
			    !getBox(WeightBox).
			   			 

//The plan ask for help for other agent, if the Box Weight (W) is bigger than the agent capacity(C)
//The plan ask for help for other agent, if the Box Weight (W) is bigger than the agent capacity(C)
+!getBox(Weight): not canGetBox(Weight)
<-  ?truckGet(T);
	?box(WeightBox, Local);
    .send(worker11,tell,msg(T, Local));
	.print("Preciso de ajuda!").

+msg(M)[source(Ag)] : at(truck)
	<- .print("O ajudande chegou!!!!");
		-+helper(false);
		-+capacityHelper(M);
		?box(WeightBox, Local);
		!getBox(WeightBox);
		-msg(M)[source(Ag)].
		
+msg(M)[source(Ag)] : at(drop1) | at(drop2)
<- -+helper(true);
	-msg(M)[source(Ag)].

