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
		!register("provider_worker", "requester_trucker").

/**
 * Answer to call for proposal
 * @param CNPId: id of required service
 * @param Task: the service to be done 
 */
@w_cnp1 +cfp(CNPId, task(Task_type, Number_of_boxes))[source(Agent)]
	:	provider(Agent, "requester_trucker") & 
		specialization(Specialization)
	<-	
		if(Task_type == fragile & (Specialization == fragile_specialization | Specialization == dual_specialization))
		{
			!make_offer(fragile, CNPId, Offer, Number_of_boxes, Agent);
			
		}
		elif(Task_type == common & (Specialization == common_specialization | Specialization == dual_specialization))
		{
			!make_offer(common, CNPId, Offer, Number_of_boxes, Agent)
		}.

@w_cnp2 +!make_offer(Task_type, CNPId, Offer, Number_of_boxes, Agent)
	<-	
		if(Task_type == fragile)
		{
			Offer = "fragile";	
		}
		else
		{
			Offer = "common";
		}
		
		.print("I have an offer: ", Offer);
		+proposal(CNPId, Task, Offer);
      	.send(Agent, tell, proposal(CNPId, Offer)).

/**
 * The agent won the CNP, and he must perform the requested task
 * @param CNPId: id of required service 
 */
@w_cnp3 +accept_proposal(CNPId): proposal(CNPId, Task, Offer)
	<-	.print("My proposal '",Offer,"' won CNP ",CNPId, " for ",Task,"!").
      	// do the task and report to initiator