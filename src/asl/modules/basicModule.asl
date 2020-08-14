// The basic rules and plans used by agents 

/* RULES *************/

// Get agent's name
getMyName(Name) :- .my_name(Name).

// Get agent's id
getMyId(Id) :- id(Id).

// Get agent's position(x, y)
getMyPosition(X, Y) :- pos(X, Y).

/* PLANS *************/

/**
 * Move the agent from an initial position to a target position.
 * The agents moves step by step (a position at time). 
 * His movement is shown on the screen when the 'gui' option is enable.
 * @param Target: the target position (where the agent wants to go)
 */
+!at(Target): at(Target) & getMyName(Me) & velocity(Velocity)
	<-	actions.updateAgentPosition(Me);
		.print("I arrived at the ", Target);
		.wait(Velocity).

//Take a step towards
+!at(Target): not at(Target) & getMyName(Me) & velocity(Velocity)
	<-	move_towards(Target);
		actions.updateAgentPosition(Me);
		.wait(Velocity);
		!at(Target).

/**
 * Considering a list of possible targets, this plan finds the nearest target from agent.
 * @param Tlist: list of targets
 * @param Target: the nearest target
 */	
+!getNearesTarget(Tlist, Target): getMyPosition(X, Y)
	<-	actions.findTheNearest(Tlist, X, Y, Target);
		.print("The nearest target: " , Target).