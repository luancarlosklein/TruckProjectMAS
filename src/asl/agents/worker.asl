/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }			// rules and plans for social protocols
{ include("src/asl/modules/providingModule.asl") }		// rules and plans for providing a service
{ include("src/asl/modules/contractingModule.asl") }	// rules and plans for contracting a service

/* BEHAVIOR *************/

!start.

/**
 * Set the initial beliefs of worker
 */
+!start: getMyName(Name)
	<-	actions.initializeWorker(Name);
		!register("requester_worker");
		!register("provider_worker", "requester_trucker");	
.

/**
 * Answering the call for proposal from a truck
 * @param CNPId: id of requested service by the trucker
 * @param task(Type): type of the cargo.
 * @param task(Nb_boxes): number of boxes inside the truck.
 * @param task(Unload_Time): time to perform the task. 
 */
+cfp(CNPId, task(Type, Nb_boxes, Unload_Time))[source(Truck)]
	:	provider(Truck, "requester_trucker") & 
		specialization(My_specialty)
		
	<-	if(Type == fragile & (My_specialty == fragile_specialization | My_specialty == dual_specialization))
		{
			!make_offer(CNPId, fragile, Nb_boxes, Unload_Time, Truck);
			
		}
		elif(Type == common & (My_specialty == common_specialization | My_specialty == dual_specialization))
		{
			!make_offer(CNPId, common, Nb_boxes, Unload_Time, Truck);
		}
.

/**
 * Try to make an offer for the truck.
 * In this case, the worker will try to hire helpers for performing the task.
 * @param CNPId: id of requested service by the trucker
 * @param Cargo_type: type of the cargo.
 * @param Nb_boxes: number of boxes inside the truck.
 * @param Unload_Time: time to perform the task. 
 * @param Truck: name of trucker that requested the service.
 */
+!make_offer(CNPId, Cargo_type, Nb_boxes, Unload_Time, Truck): getMyName(Me)
	<-	actions.getCompoundID(Truck, Me, TeamId);
		actions.getTargetPosition(Truck, pos(X, Y));
		.findall(depot(Name), depot(Name), Depots);
		!getTheNearestTarget(Depots, pos(X, Y), Depot);
		+client(CNPId, Truck);
		+task(TeamId, CNPId, Nb_boxes, Unload_Time);
		
		// Making a call for proposals to helpers (members of team)
		!start_cnp(TeamId, Nb_boxes, Unload_Time, task(Truck, Depot, Cargo_type));
.

/**
 * Start the CNP, the worker sends a call for proposal to helpers.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param Nb_boxes: number of boxes inside the truck.
 * @param Unload_Time: time to perform the task.
 * @param Task: the service description.
 */
+!start_cnp(TeamId, Nb_boxes, Unload_Time, Task)
	<-	+cnp_state(TeamId, propose);
		!call(TeamId, Task, "provider_helper", Participants_list);
		!bid(TeamId, Participants_list);
		!invite_to_work(TeamId, Nb_boxes, Unload_Time);
.

/**
 * The worker try to hire helpers for task.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param Nb_boxes: number of boxes inside the truck.
 * @param Unload_Time: time to perform the task.
 */
+!invite_to_work(TeamId, Nb_boxes, Unload_Time)
	: 	cnp_state(TeamId, propose) & 
		getMyName(Me) & getReceivedOffers(TeamId, Offers)
		
	<-	.length(Offers, Nb_offers);
      	.print("Number of offers received: ", Nb_offers);
      	
      	Offers \== [];	
 		
 		actions.generateTeam(TeamId, Me, Offers, Nb_boxes, Unload_Time, Helpers_team);
 		.print("Current team: ", Helpers_team);
 		
 		!invite_team(TeamId, Helpers_team);
 		!team_is_ready;
.

/**
 * Inform to helpers who makes part of the team.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param Helpers_team: list of helpers that belong to current team.
 */
+!invite_team(TeamId, [Helper|T]) 
	<-	.send(Helper, tell, accept_proposal(TeamId));
      	!invite_team(TeamId, T);
.
      
+!invite_team(_,[]).

/**
 * The helper informs that he won't join to team.
 * In this case, the helper has already accepted another job.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param service_status: the current status of the service.
 */
 @w_cnp1 [atomic]
+service(TeamId, aborted)[source(Helper)]
	: 	request(Helper, "provider_helper") &
		cnp_state(TeamId, propose) & getMyName(Me) & 
		task(TeamId, _, Nb_boxes, Unload_Time)
		
	<-	-proposal(TeamId, _)[source(Helper)];
		actions.removeHelperFromTeam(TeamId, Me, Helper);
		!invite_to_work(TeamId, Nb_boxes, Unload_Time);
.

/**
 * The helper informs that he will accept the job.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param service_status: the current status of the service.
 */
 @w_cnp2 [atomic]
+service(TeamId, accepted)[source(Helper)]
	: 	request(Helper, "provider_helper") &
		cnp_state(TeamId, propose) & getMyName(Me) & 
		task(TeamId, _, Nb_boxes, Unload_Time)
		
	<-	-proposal(TeamId, _)[source(Helper)];
		actions.hireHelperToTeam(TeamId, Me, Helper);
		!invite_to_work(TeamId, Nb_boxes, Unload_Time);
.

/**
 * Check if the team is ready to begin the job.
 * If everything is well, the worker send an offer to trucker.
 */
+!team_is_ready
	: 	cnp_state(TeamId, propose) & getMyName(Me) & 
		task(TeamId, CNPId, Nb_boxes, Unload_Time) & 
		getReceivedOffers(TeamId, Offers)
		
	<-	actions.checkIfTeamIsReady(TeamId, Me, Offers);
		proposeOfferToTruck(TeamId, Me, Offer);
		.send(Truck, tell, proposal(CNPId, Offer));
			
		if(Offers \== [])
		{
			!reject_offer(TeamId, Offers);
		}
.

/**
 * Inform the rejection to helpers don't selected to job.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param Rejected_helpers: list of helpers not selected to job.
 */
+!reject_offer(TeamId, [offer(_, Helper)|T])
	<-	.send(Helper, tell, reject_proposal(TeamId));
		!reject_offer(TeamId, T);
.

+!reject_offer(_,[]).

/**
 * The agent won the CNP, and he must perform the requested task
 * @param CNPId: id of required service 
 */
+accept_proposal(CNPId)[source(Truck)]
	:	provider(Truck, "requester_trucker") & getMyName(Me) & 
		task(TeamId, CNPId, _, _)
		
	<-	-+cnp_state(TeamId, contract);
		+unloadTime(CNPId, 0);
		+boxesUnloaded(CNPId, 0);
		.print("I won the CNP!");
		actions.getTeamMembers(TeamId, Me, Team);
		!start_of_activities(TeamId, Team);
.

/**
 * Inform to team that the task must be started.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param Team: team of helpers.
 */
+!start_of_activities(TeamId, [Helper|T]) 
	<-	.send(Helper, tell, execute(TeamId));
      	!inform_start_of_activities(TeamId, T);
.
      
+!start_of_activities(_,[]).

/**
 * The agent lost the CNP, so he breaks off the contract with helpers.
 * @param CNPId: id of required service 
 */
+reject_proposal(CNPId)[source(Truck)]
	: 	provider(Truck, "requester_trucker") & getMyName(Me) & 
		task(TeamId, CNPId, _, _)
		
   	<-	-+cnp_state(TeamId, aborted);
   		.print("I lost CNP ", CNPId, ".");
   		-proposal(CNPId, _, _);
   		-task(TeamId, CNPId, _, _)
   		actions.getTeamMembers(TeamId, Me);
   		!break_contract(TeamId, Team);
   		actions.deleteTeam(TeamId, Me);
.

/**
 * Break off the contract with helpers.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param Rejected_helpers: list of helpers not selected to job.
 */
+!break_contract(TeamId, [offer(_, Helper)|T])
	<-	.send(Helper, tell, service(TeamId, canceled));
		!break_contract(TeamId, T);
.

+!break_contract(_,[]).

@w_cnp3 [atomic]
+report(TeamId, Delivered_boxes, Taken_boxes, Time)[source(Helper)]
	: 	request(Helper, "provider_helper") & cnp_state(TeamId, contract) & 
		task(TeamId, CNPId, _, _) & client(CNPId, Truck)
	
	<-	-+cnp_state(TeamId, finished);
		?unloadTime(CNPId, T);
		?boxesUnloaded(CNPId, Boxes)
		T = T + Time;
		Boxes = Boxes + Delivered_boxes;
		.send(Truck, tell, report(Boxes, T));
		print("Team will be evaluated!");
		-unloadTime(CNPId,_);
		-boxesUnloaded(CNPId, _);
		-task(TeamId, CNPId, _, _);
.	