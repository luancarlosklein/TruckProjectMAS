/*
 * This agent is responsible for initialization and management of environment.
 * He creates and removes others agents from environment.
 */

//*** Plans

/*
 * Create a new worker
 */ 
+add_worker(Name)
	<-	.create_agent(Name, "worker.asl");
		.print(Name);
		-add_worker(Name).

/*
 * Create a new worker
 */ 
+add_trucker(Name)
	<-	.create_agent(Name, "truck.asl");
		-add_trucker(Name).
		
/*
 * Create a new helper
 */ 
+add_helper(Name)
	<-	.create_agent(Name, "helper.asl").
		-add_helper(helper).

///*
// * Remove an agent (worker, helper, or trucker)
// */ 
//+remove(Id_agent)
//	<-	actions.removeAgent(Id_worker, Name);
//		.kill_agent(Name);
//		-remove(Id_agent).