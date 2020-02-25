// Agent worker in project unloading_truck

/* Initial beliefs*/
capacity(10). //Maximum weight the agent can carry
batery(100). //Agent batery level, between 0 to 100 (0% - 100%)
positionDrop(0,0). //The position on the board for the discharge the box
qtdGoal(10). //Quantity of objects it have the goal to discharge
qtdDischarge(0). //Quantity of objects it have already discharge until now
truckStatus(full). //Each agent have your own truck to discharge, and this parameter informs how the truck is in the current moment
hand_in(none). //If the agent is carrying something
dropLocal(none). //The rigth place to drop the box
qtdTruckDischarge(0).

/*Initial rules */

//The rule that checks if the agent can take a box to carry 
canGetBox(Weight) :- capacity(X) & X > Weight & 
					hand_in(none) &
					not lowBatery.

// The rule that checks the agent's batery level (low level)					
lowBatery :- batery(Y) & Y < 40. 

/* Initial goals */

//Worker arrived to the drop 1
+at(drop1): dropLocal(drop1)  
	<- ?qtdDischarge(X);
		Y = X + 1;
		-+qtdDischarge(Y);
		-+hand_in(none);
		-+dropLocal(none).
	
//Worker arrived to the drop 2	
+at(drop2): dropLocal(drop2) 
	<- ?qtdDischarge(X);
		Y = X + 1;
		-+qtdDischarge(Y);
		-+hand_in(none);
		-+dropLocal(none).
		
//Go to the truck
+batery(X): not lowBatery & hand_in(none)
	<- !goToTruck.

//Go to the recharge
+batery(X) : lowBatery
	<- !goToRecharge.
	
+qtdTruck(X)[source(percept)]:true
<- 
	-+truckStatus(X).


/* Plans */

//Check if the agent arrived to the right place
@m1
+!at(P) : at(P) <- true.

//Take a step towards
@m2
+!at(P) : not at(P)
  <- move_towards(P, 0);
  	?batery(X);
	Y = X - 1;
  	-+batery(Y);
    !at(P).

//Recharge batery
+!goToRecharge: lowBatery 
				<- !at(garage);
				   -+batery(100).
				   
+!goToRecharge: not lowBatery <- true.

//In the truck
+!goToTruck: not lowBatery & hand_in(none)
			<- !at(truck);
				?box(WeightBox, Local);
			   -+dropLocal(Local); 
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
+!getBox(Weight): canGetBox(Weight)
				<- -+hand_in(box);
				   ?dropLocal(Local);
				   !at(Local).


-!goToTruck: not lowBatery & hand_in(none)
			<-  .print("FALHOU NO CHECK");
				!at(truck);
				?box(WeightBox, Local);
			    -+dropLocal(Local); 
			    !getBox(WeightBox).
			   			 


//The plan ask for help for other agent, if the Box Weight (W) is bigger than the agent capacity(C)
+!askHelp(W, C): W > C
<-  
	.concat("Solicitando ajuda para outros agente",M);
    .broadcast(ask, msg(M));
	.print("Preciso de ajuda!").

