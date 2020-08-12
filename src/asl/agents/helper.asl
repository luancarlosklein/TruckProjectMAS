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
		+truck(truck_0);
		+depot(depot_5);
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
		!goToTruck.

+!goToDepot: carrying(Qtd_boxes) & Qtd_boxes <= 0
	<-	-+busy(false);
		.print("I finish my task! I'm going back to the depot.");
		!goToGarage.

/**
 * The helper moves to the nearest garage from his current position
 */
+!goToGarage
	<-	.findall(garage(Name), garage(Name), Garages);
		!getNearesTarget(Garages, Garage);	
		!at(Garage).

/**
 * The helper moves to the nearest recharge point from his current position
 */
+!goToRecharge
	<-	.findall(recharge(Name), recharge(Name), Recharges);
		!getNearesTarget(Recharges, Recharge);
		!at(Recharge).

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