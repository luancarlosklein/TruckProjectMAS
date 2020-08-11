/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }			// rules and plans for social protocols
{ include("src/asl/modules/providingModule.asl") }		// rules and plans for providing a service
{ include("src/asl/modules/contractingModule.asl") }	// rules and plans for contracting a service

/* RULES *************/

// This rule checks if the agent can carry a box. 
canGetBox(Weight) 
	:-	capacity(X) & 
		capacityHelper(Y) & 
		(X + Y) > Weight.

// This rule checks the battery level of agent					
lowBatery 
	:-	battery(Level) & 
	Level < 40.

/* BEHAVIOR *************/

start(true).

/**
 * Set the initial beliefs of worker
 */
+start(true): getMyName(Name)
	<-	actions.initializeWorker(Name);
		!register("requester_worker");
		!register("provider_worker", "requester_trucker").

/**
 * Answer to call for proposal
 * @param CNPId: id of required service
 * @param Task: the service to be done 
 */
@c1 +cfp(CNPId, Task)[source(Agent)]: provider(Agent, "requester_trucker")
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