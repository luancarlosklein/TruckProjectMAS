/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }			// rules and plans for social protocols
{ include("src/asl/modules/contractingModule.asl") }	// rules and plans for contracting a service

/* BEHAVIOR *************/

count(0).
!start.

/**
 * Set initial beliefs for trucker
 */
+!start: getMyName(Me)
	<-	actions.trucker.initialize(Me);
		!register("requester_trucker");
.

/**
 * Define when a truck can star the unload process.
 */
+!unload: getMyName(Me) & visible(true)
	<-	.print("Truck ", Me ," added on the system");
		?cargo_type(Task_type);
		?qtd_things(Number_of_boxes);
		?unload_time(Unload_time);
		!start_cnp("provider_worker", task(Task_type, Number_of_boxes, Unload_time));
.

+!unload: getMyName(Me) & visible(false).

/**
 * Start the CNP.
 * @param Providers: define the class of agents that will provide the service.
 * @param Task: description of service.
 */	
+!start_cnp(Providers, Task)
	<-	!getNextCNPId(CNPId);
		+cnp_state(CNPId, propose);
		+task(CNPId, Task);
		!call(CNPId, Task, Providers, Participants);
		!bid(CNPId, Participants);
		!contract(CNPId)
.

/**
 * Generate an exclusive CNPId for the task
 * @return CNPID
 */	
@t_1[atomic]
+!getNextCNPId(CNPId): getMyId(Id)
	<-	actions.trucker.getNextCNPId(Id, CNPId);
		.print("NEW CALL - CNPID: ", CNPId);
.

/**
 * Attempt of contracting workers for the service.
 * Case no worker is available, the request for the service is  restarted.
 * @param CNPId: CNPId of the call
 */	
@t_cont1[atomic]
+!contract(CNPId)
	:	getMyName(Me) & 
		cnp_state(CNPId, propose) & 
		getReceivedOffers(CNPId, Offers)
		
	<-	if(Offers \== [])	// try to hire a worker
      	{
      		.length(Offers, N_offers);
      		.print("Number of offers received: ", N_offers);
      		actions.trucker.chooseBestOffer(Me, Offers, Winner);
      		!invite_worker(CNPId, Winner, Offers);
      	}
      	else	// end the call
      	{
      		.print("It was not posible to find avaliable workers, going out the system.");
      		!end_call(CNPId);	
      	}
.

/**
 * Inform to worker that his offer was accepted.
 * @param CNPId: CNPId of the call
 * @param Winner: the worker that wins the call.
 * @param Offers: list of received offers.
 */
+!invite_worker(CNPId, Winner, [offer(_, Worker)|T]) 
	<-	if(Worker == Winner)
		{
			.send(Worker, tell, accept_proposal(CNPId));
			-proposal(CNPId, _)[source(Worker)];
		}
		else
		{
      		!invite_worker(CNPId, Winner, T);
      	}
.
      
+!invite_team(_, _,[]).

/**
 * The worker cannot start the service because he is busy.
 * @param CNPId: CNPId of the call
 * @param status: service aborted, the worker is already doing another service for someone.
 */
+service(CNPId, aborted)[source(Worker)]
	<-	!contract(CNPId);
.

/**
 * The worker started the service.
 * The trucker informs to others workers that he's just hired someone.
 * @param CNPId: CNPId of the call
 * @param status: service started, the worker hired some helpers and started the unload process.
 */
+service(CNPId, started)[source(Worker)]: getReceivedOffers(CNPId, Offers)
	<-	-+cnp_state(CNPId, contract);
		!reject_offers(CNPId, Worker, Offers);
.

/**
 * Inform the rejection to workers that weren't selected for job.
 * @param CNPId: CNPId of the call
 * @param Winner: the worker that wins the call.
 * @param Offers: list of received offers.
 */
+!reject_offers(CNPId, Winner, [offer(_, Worker)|T])
	<-	if(Winner \== Worker)
		{
			.print("Rejecting: ", Worker);
			.send(Worker, tell, reject_proposal(CNPId));
			-proposal(CNPId, _)[source(Worker)];			
		}
		!reject_offers(CNPId, Winner, T);
.

+!reject_offers(_,_,[]).

/**
 * The worker informs the end of service.
 * @param CNPId: CNPId of the call
 * @param results: performance data about the service execution.
 */
+report(CNPId, results(Unload_Boxes, Time))[source(Woker)]
	<-	.print("The worker has just finished the service ");
		.print("number of unload boxes: ", Unload_Boxes);
		.print("unload time: ", Time);
		?count(C);
		K = C + 1;
		-count(C);
		+count(K);
		
		// evaluation process
		!end_call(CNPId);
.

/*
 * The truck go away (leaves the system)
 */
+!end_call(CNPId): true
	<-	-id(_);
		-pos(_,_);
		-qtd_things(_);
		-cargo_type(_);
		-visible(_);
		-unload_time(_);
		-service(CNPId,_)[source(_)];
		-report(CNPId,_)[source(_)];
		-proposal(CNPId, _)[source(_)];
		-refuse(CNPId)[source(_)];
		-cnp_state(CNPId,_);
		-task(CNPId,_);
		
		?count(C);
		if(C < 3)
		{
			.print("####### ROUND: ", C);
			.send(manager, achieve, quit);
		}
		else
		{
			.print("End of executions");
		}
.