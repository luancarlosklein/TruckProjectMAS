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
		specialization(My_specialty) &
		busy(false)
		
	<-	if(Type == fragile & (My_specialty == fragile_specialization | My_specialty == dual_specialization))
		{
			!make_offer(CNPId, fragile, Nb_boxes, Unload_Time, Truck);
			
		}
		elif(Type == common & (My_specialty == common_specialization | My_specialty == dual_specialization))
		{
			!make_offer(CNPId, common, Nb_boxes, Unload_Time, Truck);
		}
		else
		{
			.send(Truck, tell, refuse(CNPId));
			-cfp(CNPId,_)[source(Truck)];
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
	<-	actions.worker.getTeamID(Me, CNPId, TeamId);
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
	 		.concat("ADD TEAM: ", TeamId, Message)
	 		actions.worker.saveInFile(Me, Message);
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
			.concat("TEAM READY: ", TeamId, Message)
			actions.worker.saveInFile(Me, Message);
			actions.worker.proposeOffer(TeamId, Me, Offer);
			.send(Truck, tell, proposal(CNPId, Offer));
			+my_proposal(CNPId, Offer);
				
			if(Offers \== [])
			{
				!reject_offers(TeamId, Offers);
			}
		}
		else
		{
			.print("The team ", TeamId," is not ready yet!");	
		}
.

/**
 * Inform the rejection to helpers that weren't selected for job.
 * @param TeamId: id of team.
 * @param Helpers: list of not selected helpers.
 */
+!reject_offers(TeamId, [offer(_, Helper)|T])
	<-	.send(Helper, tell, reject_proposal(TeamId));
		!reject_offers(TeamId, T);
.

+!reject_offers(_,[]).

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
		.print("Removing ", Helper ," from team ", TeamId ,". He is busy.");
		actions.worker.deleteHelperFromTeam(TeamId, Me, Helper);
		.concat("FIRING HELPER: ", Helper, Message);
		actions.worker.saveInFile(Me, Message);
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
		.print(Helper ," was hired to Team ", TeamId ,".");
		actions.worker.hireHelper(TeamId, Me, Helper);
		.concat("HIRING HELPER: ", Helper, Message);
		actions.worker.saveInFile(Me, Message);
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
		+unloadedBoxes(CNPId, 0);
		+client(CNPId, Truck);
		.send(Truck, tell, service(CNPId, started))
		.print("I won the CNP ", CNPId);
		actions.worker.getTeam(TeamId, Me, Team);
		.print("My team: ", Team);
		.length(Team, N_members);
		+number_reports(TeamId, N_members);
		.findall(call(CNPId), my_proposal(CNPId, _), Calls);
		!dispense_teams(CNPId, Calls);
		actions.generic.getTime(Time);
		+unloadTime(CNPId, Time);
		!start_activities(TeamId, Team);
.

/**
 * The worker won the call, but he is doing another service for someone
 * @param CNPId: id of the call
 */
 @w_cnp4 [atomic]
+accept_proposal(CNPId)[source(Truck)]:	busy(true)
	<-	.send(Truck, tell, service(CNPId, aborted));
		!cancel_service(CNPId); 
 		!end_call(CNPId);
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
		getMyName(Me)
		
   	<-	.print("I lost CNP ", CNPId, ".");
 		!cancel_service(CNPId); 
 		!end_call(CNPId);
.

/**
 * Cancel the job assigned to a team.
 * @param CNPId: id of the call 
 */
+!cancel_service(CNPId)
	:	team(CNPId, TeamId, _, _) &
		getMyName(Me)
		
	<-	.print("Deleting team: ", TeamId);
		actions.worker.getTeam(TeamId, Me, Team);
   		actions.worker.deleteTeam(TeamId, Me);
   		.concat("DELETE TEAM: ", TeamId, Message);
   		actions.worker.saveInFile(Me, Message);
   		!break_contract(TeamId, Team);
.

/**
 * Break off the contracts done with helpers.
 * @param TeamId: id of team.
 * @param Rejected_helpers: list of helpers not selected to job.
 */
+!break_contract(TeamId, [Helper|T])
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
@w_cnp5 [atomic]
+report(TeamId, results(Delivered_boxes, Taken_boxes, Task_time))[source(Helper)]
	: 	team(CNPId, TeamId, _, _) &
		cnp_state(CNPId, contract) &
		client(CNPId, Truck) & 
		getMyName(Me)		
	
	<-	?unloadedBoxes(CNPId, Boxes);
		?number_reports(TeamId, N_reports)
		B = Boxes + Delivered_boxes;
		N = N_reports - 1;		
		// evaluate helper
		
		if(N == 0)
		{
			?unloadTime(CNPId, Stime);
			actions.generic.getTime(Ftime);
			T = Ftime - Stime;
			.send(Truck, tell, report(CNPId, results(B, T)));
			.print("The service was concluded, CNPId: ", CNPId);
			actions.worker.deleteTeam(TeamId, Me);
			.concat("[TASK COMPLETED] DELETE TEAM: ", TeamId, Message);
			actions.worker.saveInFile(Me, Message);
			!end_call(CNPId);
			!update_position;
			-+busy(false);
		}
		else
		{
			-+unloadedBoxes(CNPId, B);
			-+number_reports(TeamId, N);	
		}
.

/*
 * The position of work is changed randomly
 */
@w_cnp6 [atomic]
+!update_position: getMyName(Me)
	<-	move_worker;
		actions.generic.updateAgentPosition(Me);
.

/*
 * The work cleans his memory about data from last call
 */
+!end_call(CNPId): team(CNPId, TeamId, _, _)
	<-	-report(TeamId,_)[source(_)];
		-reject_proposal(CNPId)[source(_)];
		-accept_proposal(CNPId)[source(_)];
		-service(TeamId,_)[source(_)];
		-number_reports(TeamId,_);
		-unloadTime(CNPId,_);
		-unloadedBoxes(CNPId,_);
		-client(CNPId,_);
		-my_proposal(CNPId,_);
		-proposal(CNPId, _)[source(_)];
		-refuse(CNPId)[source(_)];
		-cnp_state(CNPId,_);
		-team(CNPId, TeamId,_,_);
		-task(CNPId,_,_,_);
		-cfp(CNPId,_)[source(_)];
.