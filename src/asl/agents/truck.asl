/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }			// rules and plans for social protocols
{ include("src/asl/modules/contractingModule.asl") }	// rules and plans for contracting a service

/* BEHAVIOR *************/

!start(true).

/**
 * Set the initial beliefs of trucker
 */
+!start(true): getMyName(Name)
	<-	actions.initializeTruck(Name)
		!register("requester_trucker").