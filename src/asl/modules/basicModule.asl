// The basic rules and plans used by agents 

/* RULES *************/

// Get agent's name
getMyName(Name) :- .my_name(Name).

// Get agent's id
getMyId(Id) :- id(Id).


/* PLANS *************/

/**
 * Move the agent from an initial position to a target position.
 * The agents moves step by step (a position at time). 
 * His movement is shown on the screen when the 'gui' option is enable.
 */
+move(Target): true
	<-	!at(Target).
	
@m1
+!at(Target): at(Target) & getMyName(Name) & getMyId(Id) 
	<-	.print("I arrived!", Name, Id).

//Take a step towards
@m2
+!at(Target): not at(Target)
	<-	move_towards(Target);
		!at(Target).