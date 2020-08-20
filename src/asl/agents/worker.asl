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
		!register("provider_worker", "requester_trucker");
.

/**
 * Answer to call for proposal
 * @param CNPId: id of required service by a truck
 * @param Task: the service to be done 
 */
@w_cnp1 +cfp(CNPId, task(Task_type, Number_of_boxes))[source(Truck)]
	:	provider(Truck, "requester_trucker") & 
		specialization(Specialization)
	<-	
		if(Task_type == fragile & (Specialization == fragile_specialization | Specialization == dual_specialization))
		{
			!make_offer(fragile, Number_of_boxes, Truck);
			
		}
		elif(Task_type == common & (Specialization == common_specialization | Specialization == dual_specialization))
		{
			!make_offer(common, Number_of_boxes, Truck);
		}
.

@w_cnp2 +!make_offer(Cargo_type, Number_of_boxes, Truck): getMyName(Me)
	<-	actions.getCompoundID(Truck, Me, CNPId);
		actions.getTargetPosition(Truck, pos(X, Y));
		.findall(depot(Name), depot(Name), Depots);
		!getNearesTarget(Depots, X, Y, Depot);
		!start_cnp(CNPId, "provider_helper", task(Truck, Depot, Cargo_type));
.

/**
 * Start the CNP.
 * @param CNPId: id of service requested from helpers.
 * @param Providers: type or class of service providers.
 * @param Task: a simple service description.
 */
+!start_cnp(CNPId, Providers, Task)
	<-	+cnp_state(CNPId, propose);
		!call(CNPId, Task, Providers, Participants_list);
		!bid(CNPId, Participants_list);
		!contract(CNPId)
.

@t_cont1[atomic]
+!contract(CNPId): cnp_state(CNPId, propose) & getMyName(Me)
	<-	-+cnp_state(CNPId, contract);  
      	.findall(offer(Offer, Ag), proposal(CNPId, Offer)[source(Ag)], Offers);
      	.length(Offers, N_offers);
      	.print("Number of offers received: ", N_offers);
      	
      	Offers \== [];	
 		
 		actions.generateTeam(Me, CNPId, Offers, Helpers_team);
 		.print(Helpers_team);
 		
 		!inform_team(CNPId, Helpers_team);
 		
.

//+!inform_team(winners[Helper|T])

+!inform_team(CNPId, [Helper|T]) 
	<-	.send(Helper, tell, accept_proposal(CNPId));
      	!inform_team(CNPId, T)
.

// Case the list of offers is empty      
+!inform_team(_,[]).      	

//
///**
// * The agent won the CNP, and he must perform the requested task
// * @param CNPId: id of required service 
// */
//@w_cnp3 +accept_proposal(CNPId): proposal(CNPId, Task, Offer)
//	<-	.print("My proposal '",Offer,"' won CNP ",CNPId, " for ",Task,"!").
//      	// do the task and report to initiator