/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }		// rules and plans for social protocols
{ include("src/asl/modules/providingModule.asl") }	// rules and plans for providing a service

/* BEHAVIOR *************/

//!start.

/**
 * Set the initial beliefs of helper
 */
+!start: getMyName(Me)
	<-	actions.initializeHelper(Me);
		!register("provider_helper", "requester_worker");
		+truck(truck_3);
		+depot(depot_5);
		+empty_truck(false);
		+cargo_type(fragile);
//		+cargo_type(common);
		+busy(true);
		?safety(Safety_default);
		?battery(Battery_default);
		+current_safety(Safety_default);
		+current_battery(Battery_default);
		+taken_boxes(0);
		+delivered_boxes(0);
		!goToTruck.

/**
 * The helper moves to the indicated truck by a worker.
 * When the helper arrives at the truck position, he takes some boxes
 */
+!goToTruck: truck(Truck)
	<-	!at(Truck);
		!takeBoxesFrom(Truck);
		?carrying(Carried_boxes);
		?taken_boxes(Taken_boxes);
		-+taken_boxes(Taken_boxes + Carried_boxes);
		.print("Number of boxes taken from truck: ", Carried_boxes);
		!goToDepot.

@h_a1[atomic]
+!takeBoxesFrom(Truck): getMyName(Me)
	<-	actions.takeBoxes(Truck, Me);.

/**
 * The helper moves to the indicated depot by a worker.
 * When the helper arrives at the depot position, he drops the boxes off
 */
+!goToDepot: empty_truck(false) & depot(Depot)
	<-	!at(Depot);
		?carrying(Carried_boxes);
		?delivered_boxes(Delivered_boxes);
		-+delivered_boxes(Delivered_boxes + Carried_boxes);
		-+carrying(0);
		!checkBattery;
		!checkFailure;
		!goToTruck.

+!goToDepot: empty_truck(true) & delivered_boxes(Delivered_boxes) & taken_boxes(Taken_boxes)
	<-	-+busy(false);
		.print("I finish my task! I'm going back to the depot.");
		.print("Amount of boxes taken from truck: ", Taken_boxes);
		.print("Amount of boxes delivered at the depot: ", Delivered_boxes);
		!goToGarage.

/**
 * The helper moves to the nearest garage from his current position
 * When a helper arrives at a garage, he recharges his battery and receives maintenance
 * In this case, the recharge of battery and the maintenance process don't have cost for helper.
 */
+!goToGarage: safety(Safety_default) & battery(Battery_default)
	<-	.findall(garage(Name), garage(Name), Garages);
		!getNearesTarget(Garages, Garage);	
		!at(Garage);
		-+current_safety(Safety_default);
		-+current_battery(Battery_default).

/**
 * The helper moves to the nearest recharge point from his current position
 * If the helper stops his task to recharge, so he is penalized. This process cost 2 seconds for helper.
 */
+!goToRecharge: battery(Battery_default)
	<-	.findall(recharge(Name), recharge(Name), Recharges);
		!getNearesTarget(Recharges, Recharge);
		!at(Recharge);
		.wait(2000);
		-+current_battery(Battery_default).

/**
 * Check if the battery level of agent is low 
 */
+!checkBattery
	<-	?current_battery(Battery);
		?energy_cost(Cost);
		Battery_level = Battery - Cost;
		-+current_battery(Battery_level);
		
		if(Battery_level <= 0)
		{
			.print("I don't have battery. I'm going to a recharge point.");
			!goToRecharge;
		}
		.print("My battery level is: ", Battery_level).

/**
 * Check if there is something wrong (a failure)
 * When the helper failures he goes to the garage, receives maintenance, and he is penalized at 6 seconds  
 */
+!checkFailure: failure_prob(Probability)
	<-	.random(P);
		?current_safety(Count);
		C = Count - 1;
		-+current_safety(C)
		
		if(P <= Probability & C <= 0)
		{
			.print("I broke, stopping to maintenance. I'm going to the garage.");
			!goToGarage;
			.wait(6000);
		}
		.print("My safety count is: ", C).

/**
 * Check if a box was dropped on the floor and the type of box
 * If the box that fell to the ground is fragile, this box is destroyed and the helper is penalized at 1 second.
 * Otherwise, if the box is common, the agent is just penalized at 1 second
 */
+!checkAccident: dexterity(Dex) & cargo_type(Type)
	<-	.random(D);
		
		if(D > Dex)
		{
			.print("ACCIDENT: I dropped a box on the floor.");
			
			if(Type == fragile)
			{
				?carrying(Carried_boxes);
				-+carrying(Carried_boxes - 1);
			}
			.wait(1000);
		}.

/**
 * When the helper is carrying one or more boxes, and he is moving, he must check if happened an accident.
 * The accidents happen when helpers drop a box on the floor.
 * The frequency of accidents depends on the dexterity level of helper.
 */
+at(somewhere): carrying(Carried_boxes) & Carried_boxes > 0
	<- 	!checkAccident.

/**
 * Answer to call for proposal
 * @param CNPId: id of required service
 * @param Task: the service to be done 
 */
@h_cnp1 +cfp(CNPId, Task)[source(Agent)]: provider(Agent, "requester_worker")
	<-	// produce an Offer
		+proposal(CNPId, Task, Offer);
      	.send(Agent, tell, proposal(CNPId, Offer)).

/**
 * The agent won the CNP, and he must perform the requested task
 * @param CNPId: id of required service 
 */
@h_cnp2 +accept_proposal(CNPId): proposal(CNPId, Task, Offer)
	<-	.print("My proposal '",Offer,"' won CNP ",CNPId, " for ",Task,"!").
      	// do the task and report to initiator