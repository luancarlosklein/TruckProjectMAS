/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }			// rules and plans for social protocols
{ include("src/asl/modules/contractingModule.asl") }	// rules and plans for contracting a service

/* BEHAVIOR *************/

!start.

/**
 * Set initial beliefs for trucker
 */
+!start: getMyName(Name)
	<-	actions.trucker.initialize(Name);
		!register("requester_trucker");
		?cargo_type(Task_type);
		?qtd_things(Number_of_boxes);
		?unload_time(Unload_time);
		!start_cnp("provider_worker", task(Task_type, Number_of_boxes, Unload_time))
.
	
/**
 * Start the CNP.
 * @param Providers: define the class of agents that will provide the service.
 * @param Task: description of service.
 */	
+!start_cnp(Providers, Task): getMyId(CNPId)
	<-	+cnp_state(CNPId, propose);
		+reset(CNPId, 0);
		+task(CNPId, Task)
		!call(CNPId, Task, Providers, Participants);
		!bid(CNPId, Participants);
		!contract(CNPId)
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
      	else	// restart the call
      	{
      		.print("It was not posible to find avaliable workers, a new call will be done.");
      		?task(CNPId, Task);
      		?reset(CNPId, Times);
      		T = Times + 1;
      		-+reset(CNPId, T);
      		-cnp_state(CNPId, _);			
			-task(CNPId, _)
      		!start_cnp("provider_worker", Task)
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
	<-	-proposal(CNPId, _)[source(Worker)];
		!contract(CNPId);
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
	<-	.print("The worker has just finished the service ", Unload_Boxes, Time);
		-task(CNPId, _);
		-proposal(CNPId, _)[source(Worker)];
		-+cnp_state(CNPId, finished);
		// evaluation process
.