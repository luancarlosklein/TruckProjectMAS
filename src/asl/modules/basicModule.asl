// The general rules and plans used by agents. 

/* RULES *************/

// Get agent's name
getMyName(Name) :- .my_name(Name).

// Get agent's id
getMyId(Id) :- id(Id).

// Get agent's position(x, y)
getMyPosition(X, Y) :- pos(X, Y).

// Get the number of offers received
getReceivedOffers(CNPId, Offers) :- .findall(offer(Offer, Agent), proposal(CNPId, Offer)[source(Agent)], Offers).

/**
 * Move the agent from an initial position to a target position.
 * The agents moves step by step (a position at time). 
 * His movement is shown on the screen when the 'gui' option is enable.
 * @param Target: the target position (where the agent wants to go)
 */
+!at(Target): at(Target) & getMyName(Me) & velocity(Velocity)
	<-	actions.generic.updateAgentPosition(Me);
		.print("I arrived at the ", Target);
		.wait(Velocity);
.

//Take a step towards
+!at(Target): not at(Target) & getMyName(Me) & velocity(Velocity)
	<-	move_towards(Target);
		actions.generic.updateAgentPosition(Me);
		.wait(Velocity);
		!at(Target);
.

/**
 * Get the most nearest target from agent considering a list of possible targets.
 * @param Target_list: list of possible targets.
 * @return Nearest_target: the nearest target from agent.
 */	
+!getTheNearestFromMe(Target_list, Nearest_target): getMyPosition(X, Y) 
	<-	actions.generic.getNearestTarget(Target_list, X, Y, Nearest_target);
		.print("The nearest target is: ", Nearest_target);
.

/**
 * Get the most nearest target from a given position.
 * @param Target_list: list of possible targets.
 * @param pos(X, Y): the reference position for distance computation.
 * @return Nearest_target: the nearest target from agent.
 */
 +!getTheNearestTarget(Target_list, pos(X, Y), Nearest_target)
	<-	actions.generic.getNearestTarget(Target_list, X, Y, Nearest_target);
		.print("The nearest target is: ", Nearest_target);
.