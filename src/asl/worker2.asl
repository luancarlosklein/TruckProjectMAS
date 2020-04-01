// Agent worker in project unloading_truck

/* Initial beliefs*/
capacity(1). //Maximum weight the agent can carry
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
busy(no).


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
+batery(X) : lowBatery
	<- !goToRecharge.
	
+qtdTruck(X)[source(percept)]:true
<- 
	-+truckStatus(X).


/* Plans */

//Check if the agent arrived to the right place

//The drop is a special case, because with the end of this, we need to call de goToTruck
@m1
+!at(P) : at(P) & dropLocal(P) <-
	-+dropLocal(none);
	!goToTruck. 
	
//For general cases
@m2
+!at(P) : at(P) & not dropLocal(P) <-
	true.
	

//Take a step towards
@m3
+!at(P) : not at(P)
  <- move_towards(P, 2);
  	//?batery(X);
	//Y = X;
  	//-+batery(Y);
    !at(P).


//Worker arrived to the drop 1
+at(drop1): dropLocal(drop1) & hand_in(box)
	<- ?qtdDischarge(X);
		Y = X + 1;
		-+qtdDischarge(Y);
		-+hand_in(none);	
		-+capacityHelper(0).
		
	
//Worker arrived to the drop 2	
+at(drop2): dropLocal(drop2) & hand_in(box)
	<- ?qtdDischarge(X);
		Y = X + 1;
		-+qtdDischarge(Y);
		-+hand_in(none);
		-+capacityHelper(0).
	
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
    .send(worker11,tell,msg(T));
	.print("Preciso de ajuda!").

+msg(M)[source(Ag)] : at(truck)
	<- .print("O ajudande chegou!!!!");
		-+capacityHelper(M);
		-msg(M)[source(Ag)];
		?box(WeightBox, Local);
		-+dropLocal(Local); 
		!tellDropPlace(Local);
		-+dropLocal(Local); 
		!getBox(WeightBox).
		


+!tellDropPlace(Place): true
<-  
    .send(worker11,tell,msg(Place));
	.print("HEY AJUDANTE. ESTOU TE INFORMANDO O DROP").




