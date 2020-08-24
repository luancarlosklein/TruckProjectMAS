/* MODULES *************/

{ include("src/asl/modules/basicModule.asl") }			// rules and plans for social protocols
{ include("src/asl/modules/contractingModule.asl") }	// rules and plans for contracting a service

/* BEHAVIOR *************/

!start.

/**
 * Set the initial beliefs of trucker
 */
+!start: getMyName(Name)
	<-	actions.initializeTruck(Name);
		!register("requester_trucker");
		?cargo_type(Task_type);
		?qtd_things(Number_of_boxes);
		?unload_time(Unload_time);
		!start_cnp("provider_worker", task(Task_type, Number_of_boxes, Unload_time))
.
	
/**
 * Start the CNP.
 * @param Providers: type or class of service providers.
 * @param Task: a simple service description.
 */	
+!start_cnp(Providers, Task): getMyId(CNPId)
	<-	+cnp_state(CNPId, propose);
		!call(CNPId, Task, Providers, Participants_list);
		!bid(CNPId, Participants_list);
		!contract(CNPId)
.

@t_cont1[atomic]
+!contract(CNPId): cnp_state(CNPId, propose)
	<-	-+cnp_state(CNPId, contract);  
      	.findall(offer(Offer, Ag), proposal(CNPId, Offer)[source(Ag)], Offers);
      	.length(Offers, N_offers);
      	.print("Number of offers received: ", N_offers);
      	
      	// implement a better version of this.
      	.send(Ag, tell, accept_proposal(CNPId))
.

+report(Unload_Boxes, Time)[source(Woker)]
	<-	.print(Unload_Boxes, Time).