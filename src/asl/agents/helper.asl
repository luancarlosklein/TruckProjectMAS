/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }		// rules and plans for social protocols
{ include("src/asl/modules/providingModule.asl") }	// rules and plans for providing a service


/* BEHAVIOR *************/

start(true).

/**
 * Set the initial beliefs of helper
 */
+start(true): getMyName(Name)
	<-	actions.initializeHelper(Name);
		!register("provider_helper", "requester_worker");
		+move(truck_0).

/**
 * Answer to call for proposal
 * @param CNPId: id of required service
 * @param Task: the service to be done 
 */
@c1 +cfp(CNPId, Task)[source(Agent)]: provider(Agent, "requester_worker")
	<-	// produce an Offer
		+proposal(CNPId, Task, Offer);
      	.send(Agent, tell, propose(CNPId, Offer)).

/**
 * The agent won the CNP, and he must perform the requested task
 * @param CNPId: id of required service 
 */
@r1 +accept_proposal(CNPId): proposal(CNPId, Task, Offer)
	<-	.print("My proposal '",Offer,"' won CNP ",CNPId, " for ",Task,"!").
      	// do the task and report to initiator