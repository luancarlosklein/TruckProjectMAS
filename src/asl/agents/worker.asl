/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }			// rules and plans for social protocols
{ include("src/asl/modules/providingModule.asl") }		// rules and plans for providing a service
{ include("src/asl/modules/contractingModule.asl") }	// rules and plans for contracting a service

/* BEHAVIOR *************/

!start.

/**
 * Set initial beliefs for worker
 */
+!start: getMyName(Name)
	<-	actions.worker.initialize(Name);
		!register("requester_worker");
		!register("provider_worker", "requester_trucker");
		+busy(false);	
.

/**
 * Answering the call for proposal.
 * @param CNPId: id of the call.
 * @param task(Type): type of cargo.
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
 * Attempt for making an offer for the trucker.
 * Before to answer the call, the worker will try to hire helpers to perform the service.
 * @param CNPId: id of the call.
 * @param Cargo_type: type of cargo.
 * @param Nb_boxes: number of boxes inside the truck.
 * @param Unload_Time: time to perform the task. 
 * @param Truck: name of trucker that requested the service.
 */
+!make_offer(CNPId, Cargo_type, Nb_boxes, Unload_Time, Truck): getMyName(Me)
	<-	actions.worker.getTeamID(Me, Truck, TeamId);
		actions.generic.getTargetPosition(Truck, pos(X, Y));
		.findall(depot(Name), depot(Name), Depots);
		!getTheNearestTarget(Depots, pos(X, Y), Depot);
		+team(CNPId, TeamId, Truck, Depot);
		+task(CNPId, Nb_boxes, Unload_Time, Cargo_type);
		!start_cnp(CNPId);
.

/**
 * Start the CNP, the worker sends a call for proposal to helpers.
 * @param CNPId: id of the call.
 */
+!start_cnp(CNPId)
	: 	team(CNPId, TeamId, Truck, Depot) &
		task(CNPId, Nb_boxes, Unload_Time, Cargo_type)
		
	<-	+cnp_state(CNPId, propose);
		!call(TeamId, task(Truck, Depot, Cargo_type), "provider_helper", Participants_list);
		!bid(TeamId, Participants_list);
		!invite_helpers(CNPId);
.

/**
 * The worker try to hire helpers for the task.
 * @param CNPId: id of the call.
 */
+!invite_helpers(CNPId)
	:	team(CNPId, TeamId, Truck, Depot) &
		task(CNPId, Nb_boxes, Unload_Time, Cargo_type) & 	
		getReceivedOffers(TeamId, Offers) & 		
		getMyName(Me)
		
	<- 	if(Offers \== [])
      	{	
	 		actions.worker.createTeam(TeamId, Me, Offers, Nb_boxes, Unload_Time, Team);
	 		!invite_team(TeamId, Team);
		}
		!check_team(TeamId);
.

/**
 * Inform to helpers who makes part of the team.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param Helpers: list of helpers from current team.
 */
+!invite_team(TeamId, [Helper|T]) 
	<-	.send(Helper, tell, accept_proposal(TeamId));
      	!invite_team(TeamId, T);
.
      
+!invite_team(_,[]).

/**
 * Check if the team is ready for starting the job.
 * If the team is ready, the worker send an offer to trucker.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 */
+!check_team(TeamId)
	: 	team(CNPId, TeamId, Truck, Depot) &
		task(CNPId, Nb_boxes, Unload_Time, Cargo_type) & 
		getReceivedOffers(TeamId, Offers) &
		getMyName(Me)
		
	<-	if(actions.worker.teamIsReady(TeamId, Me, Offers))
		{
			// check risk profile.
			actions.worker.proposeOffer(TeamId, Me, Offer);
			.send(Truck, tell, proposal(CNPId, Offer));
			+my_proposal(CNPId, Offer);
				
			if(Offers \== [])
			{
				!reject_offer(TeamId, Offers);
			}
		}
		else
		{
			.print("The team is not ready yet!");	
		}
.

/**
 * Inform the rejection to helpers that weren't selected for job.
 * @param TeamId: id of team.
 * @param Helpers: list of not selected helpers.
 */
+!reject_offer(TeamId, [offer(_, Helper)|T])
	<-	.send(Helper, tell, reject_proposal(TeamId));
		!reject_offer(TeamId, T);
.

+!reject_offer(_,[]).

/**
 * The helper informs that he won't join to team.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param status: service aborted, the helper is already doing another service for someone.
 */
 @w_cnp1 [atomic]
+service(TeamId, aborted)[source(Helper)]
	: 	team(CNPId, TeamId, Truck, Depot) &
		task(CNPId, Nb_boxes, Unload_Time, Cargo_type) &
		getMyName(Me) 
		
	<-	-proposal(TeamId, _)[source(Helper)];
		.print("Removing ", Helper ," from team. He is busy.");
		actions.worker.deleteHelperFromTeam(TeamId, Me, Helper);
		!invite_helpers(CNPId);
.

/**
 * The helper informs that he will accept the job.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param status: service started, the helper will start the service.
 */
 @w_cnp2 [atomic]
+service(TeamId, accepted)[source(Helper)]
	: 	team(CNPId, TeamId, Truck, Depot) &
		task(CNPId, Nb_boxes, Unload_Time, Cargo_type) &
		getMyName(Me)
		
	<-	-proposal(TeamId, _)[source(Helper)];
		.print(Helper ," was hired for the task.");
		actions.worker.hireHelper(TeamId, Me, Helper);
		!check_team(TeamId);
.

/**
 * The worker won the call and he must perform the service
 * @param CNPId: id of the call
 */
 @w_cnp3 [atomic]
+accept_proposal(CNPId)[source(Truck)]
	:	busy(false) &
		provider(Truck, "requester_trucker") & 
		team(CNPId, TeamId, _, _) &
		getMyName(Me) &
		getReceivedOffers(CNPId, Offers)
		
	<-	-+cnp_state(CNPId, contract);
		-+busy(true);
		+unloadTime(CNPId, 0);
		+boxesUnloaded(CNPId, 0);
		.send(Truck, tell, service(CNPId, started))
		.print("I won the CNP!");
		actions.worker.getTeam(TeamId, Me, Team);
		.length(Team, N_members);
		+team_members(TeamId, N_members);
		.findall(call(CNPId), my_proposal(CNPId, _), Calls);
		!dispense_teams(CNPId, Calls);
		!start_activities(TeamId, Team);
.

/**
 * The worker won the call, but he is doing another service for someone
 * @param CNPId: id of the call
 */
+accept_proposal(CNPId)[source(Truck)]:	busy(true)
	<-	.send(Truck, tell, service(CNPId, aborted));
.

/**
 * The others teams are dispensed when the worker accepts a job.
 * @Current_CNPId: id of the call for which the worker accepted the job.
 * @Calls: list of calls where the worker is enrolled as participant
 */
+!dispense_teams(Current_CNPId, [call(CNPId)|T])
	<-	if(Current_CNPId \== CNPId)
		{
			!cancel_service(CNPId);	
		}
		!dispense_teams(Current_CNPId, T);
.

+!dispense_teams(Current_CNPId, []).

/**
 * Inform to team that the task must be started.
 * @param TeamId: id of team that will be hired to perform the task (it is a exclusive id).
 * @param Team: team of helpers.
 */
+!start_activities(TeamId, [Helper|T]) 
	<-	.send(Helper, tell, execute(TeamId));
      	!start_activities(TeamId, T);
.
      
+!start_activities(_,[]).

/**
 * The agent lost the CNP, so he breaks off the contract with helpers.
 * @param CNPId: id of required service 
 */
+reject_proposal(CNPId)[source(Truck)]
	:	provider(Truck, "requester_trucker") & 
		team(CNPId, TeamId, _, _) &
		getMyName(Me)
		
   	<-	-+cnp_state(TeamId, aborted);
   		.print("I lost CNP ", CNPId, ".");
 		!cancel_service(CNPId);  		
.

/**
 * Cancel the job assigned to a team.
 * @param CNPId: id of the call 
 */
+!cancel_service(CNPId)
	:	team(CNPId, TeamId, _, _) &
		getMyName(Me)
		
	<-	-my_proposal(CNPId, _);
   		-team(CNPId, TeamId, _, _);
		-task(CNPId, _, _, _);
		actions.worker.getTeam(TeamId, Me, Team);
   		actions.worker.deleteTeam(TeamId, Me);
   		!break_contract(TeamId, Team);
.

/**
 * Break off the contracts done with helpers.
 * @param TeamId: id of team.
 * @param Rejected_helpers: list of helpers not selected to job.
 */
+!break_contract(TeamId, [offer(_, Helper)|T])
	<-	.send(Helper, tell, service(TeamId, canceled));
		!break_contract(TeamId, T);
.

+!break_contract(_,[]).

/**
 * After all helper sent their reports, the worker ends the service and send his report to trucker.
 * @param TeamId: id of team.
 * @param Delivered_boxes: number of boxes delivered by helper
 * @param Taken_boxes: number of boxes that the helper took from truck.
 * @param Time: Time that the helper took to perform the task.
 */
@w_cnp4 [atomic]
+report(TeamId, results(Delivered_boxes, Taken_boxes, Time))[source(Helper)]
	: 	subscribe(Helper, "requester_worker") & 
		cnp_state(TeamId, contract) & 
		team(CNPId, TeamId, _, _) &
		getMyName(Me)		
	
	<-	?unloadTime(CNPId, T);
		?boxesUnloaded(CNPId, Boxes);
		?team_members(TeamId, N_members)
		T = T + Time;
		Boxes = Boxes + Delivered_boxes;
		N_members = N_members - 1;		
		// evaluate helper
		
		if(N_members == 0)
		{
			.send(Truck, tell, report(CNPId, results(Boxes, T)));
			.print("The service was concluded, CNPId: ", CNPId);
			-task(TeamId, CNPId, _, _);
			-unloadTime(CNPId, _);
			-boxesUnloaded(CNPId, _);
			-team_members(TeamId, _);
			-my_proposal(CNPId, _);
			actions.worker.deleteTeam(TeamId, Me);
			-+cnp_state(TeamId, finished);	
			-+busy(false);
		}
		else
		{
			-+unloadTime(CNPId, T);
			-+boxesUnloaded(CNPId, Boxes);
			-+team_members(N_members);	
		}
.