/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }		// rules and plans for social protocols
{ include("src/asl/modules/providingModule.asl") }	// rules and plans for providing a service

/* BEHAVIOR *************/

!start(true).

/**
 * Set the initial beliefs of helper
 */
+!start(true): getMyName(Me)
	<-	actions.initializeHelper(Me);
		!register("provider_helper", "requester_worker");
		+truck(truck_2);
		+depot(depot_4);
		+busy(true);
		!goToTruck.

/**
 * The helper moves to the truck indicated by a worker.
 * When the helper arrives at the truck, he takes some boxes
 */
+!goToTruck: truck(Truck)
	<-	!at(Truck);
		!takeBoxes(Truck);
		?carrying(Qtd_boxes);
		.print("Amount of boxes taken: ", Qtd_boxes);
		!goToDepot.

@h_a1[atomic]
+!takeBoxes(Truck): getMyName(Me)
	<-	actions.takeBoxes(Truck, Me);.

/**
 * The helper moves to the depot indicated by a worker.
 * When the helper arrives at the depot, he drops the boxes off
 */
+!goToDepot: carrying(Qtd_boxes) & Qtd_boxes > 0 & depot(Depot)
	<-	!at(Depot);
		-+carrying(0);
		!checkBattery;
		!checkFailure;
		!goToTruck.

+!goToDepot: carrying(Qtd_boxes) & Qtd_boxes <= 0
	<-	-+busy(false);
		.print("I finish my task! I'm going back to the depot.");
		!goToGarage.

/**
 * The helper moves to the nearest garage from his current position
 * When a helper arrives at a garage, he recharges his battery and receives maintenance
 * In this case, the battery recharge and the maintenance process don't have cost for helper.
 */
+!goToGarage
	<-	.findall(garage(Name), garage(Name), Garages);
		!getNearesTarget(Garages, Garage);	
		!at(Garage);
		-+safety_count(10);
		-+battery(1.0).

/**
 * The helper moves to the nearest recharge point from his current position
 * If the helper stops his task to recharge, he is penalized. This process cost 2 seconds for helper.
 */
+!goToRecharge
	<-	.findall(recharge(Name), recharge(Name), Recharges);
		!getNearesTarget(Recharges, Recharge);
		!at(Recharge);
		.wait(2000);
		-+battery(1.0).

/**
 * Check if the battery level of agent is low 
 */
+!checkBattery: battery(Battery) & energy_cost(Cost)
	<-	Blevel = Battery - Cost;
		-+battery(Blevel);
		
		if(Blevel <= 0)
		{
			.print("I don't have battery. I'm going to a recharge point.");
			!goToRecharge;
		}
		.print("My battery level is: ", Blevel).

/**
 * Check if there is something is wrong (a failure)
 * When the helper failures he goes to the garage, receives maintenance, and he is penalized at 6 seconds  
 */
+!checkFailure: failure_prob(Probability) & safety_count(Count)
	<-	.random(P);
		C = Count - 1;
		-+safety_count(C)
		
		if(P <= Probability & C <= 0)
		{
			.print("I broke, stopping to maintenance. I'm going to the garage.");
			!goToGarage;
			.wait(6000);
		}
		.print("My safety count is: ", C).

+!checkBoxDroped.
// if the box is fragile, it must decrease 1 of the amount of droped boxes off.
// otherwise the time penalization must be applied.

/**
 * Answer to call for proposal
 * @param CNPId: id of required service
 * @param Task: the service to be done 
 */
@h_cnp1 +cfp(CNPId, Task)[source(Agent)]: provider(Agent, "requester_worker")
	<-	// produce an Offer
		+proposal(CNPId, Task, Offer);
      	.send(Agent, tell, propose(CNPId, Offer)).

/**
 * The agent won the CNP, and he must perform the requested task
 * @param CNPId: id of required service 
 */
@h_cnp2 +accept_proposal(CNPId): proposal(CNPId, Task, Offer)
	<-	.print("My proposal '",Offer,"' won CNP ",CNPId, " for ",Task,"!").
      	// do the task and report to initiator