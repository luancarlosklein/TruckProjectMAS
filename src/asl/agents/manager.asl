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
		.print("A new worker was created. Name: ", Name);
.

/*
 * Create a new worker
 */ 
+add_trucker(Name)
	<-	.create_agent(Name, "truck.asl");
		.print("A new truck was created. Name: ", Name);
		.send(Name, achieve, unload);
.
		
/*
 * Create a new helper
 */ 
+add_helper(Name)
	<-	.create_agent(Name, "helper.asl");
		.print("A new helper was created. Name: ", Name);
.

@t_cont1[atomic]
+!quit[source(Truck)]
	<-	actions.trucker.getNext(Next_trucker);	
		actions.generic.resetProperties(Truck);
		actions.trucker.goToWaitingQueue(Truck);
		.send(Next_trucker, achieve, start);
		.send(Next_trucker, achieve, unload);
.

///*
// * Remove an agent (worker, helper, or trucker)
// */ 
//+remove(Id_agent)
//	<-	actions.removeAgent(Id_worker, Name);
//		.kill_agent(Name);
//		-remove(Id_agent).