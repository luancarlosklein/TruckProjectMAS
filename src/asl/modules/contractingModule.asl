// This module implements the contracting role of contract-net-protocol (CNP)
/***
 * For using the CNP it is recommended to follow the calling order bellow:
 * !register(Role);
 * !call(CNPId, Task, Ptype, Plist)
 * !bid(CNPId, Plist)
 * !contract(...)(MUST BE IMPLEMENTED: it must choose the best offer)
 * !delivery(...)(MUST BE IMPLEMENTED: it evaluates the provided service after its conclusion)
 * !show_winner(CNPId, Loffers, Boffer, Wagent)
 * !result(CNPId, Plist, Wagent)  
 ***/ 	

/* RULES *************/

/**
 * This rule computes the number of participants of a call for proposals
 * @param CNPId: id of required service
 * @return NP (number of participants) 
 * */ 
all_proposals_received(CNPId, NP) 
	:-	.count(proposal(CNPId, _)[source(_)], NO) &		// number of proposals received
		.count(refuse(CNPId)[source(_)], NR) &      	// number of refusals received
		NP = NO + NR.
		
/* PLANS *************/

/**
 * Register the agent as a contractor of a given service 
 * @param Role: type of contracting  
 * */ 
+!register(Role): true
	<-	.df_register(Role).
      
/**
 * Send the call for proposal to possible participants 
 * @param CNPId: id of required service
 * @param Task: the service to be done
 * @param Ptype: type of the expected participants
 * @return Plist (list of participants of calling) 
 */  
+!call(CNPId, Task, Ptype, Plist)
	<-	.print("Waiting for participants, task: ", Task, "...");
		.wait(2000);
		.df_search(Ptype, Plist);
		.print("Sending call for proposal to ", Plist);
		.send(Plist, tell, cfp(CNPId, Task)).

/**
 * Wait for proposals of participants arrive
 * The deadline of the CNP is about 4 seconds (or until that all proposals has been received)
 * @param CNPId: id of required service
 * @return Plist (list of participants of calling) 
 */
+!bid(CNPId, Plist)
	<-	.wait(all_proposals_received(CNPId, .length(Plist)), 4000, _).

/**
 * Show the winner of the calling
 * @param CNPId: id of required service
 * @param Loffers: list of received offers
 * @param Boffer: the best received offer
 * @param Wagent: the winner agent 
 */
+!show_winner(CNPId, Loffers, Boffer, Wagent):  Loffers \== []
	<-	.print("Offers are: ", Loffers);
      	.print("The winner is ", Wagent,". Offer: ", Boffer).
      	
// No offer case
+!show_winner(CNPId,_ , _, nowinner)
	<-	.print("There is not a winner for the calling, CNPId: ", CNPId).

/**
 * Announce who won the calling for all participants
 * @param CNPId: id of required service
 * @param Loffers: list of received offers
 * @param Wagent: the winner agent 
 */	
// Announce result to the winner
+!result(CNPId, [offer(_, Agent)|T], Wagent): Agent == Wagent 
	<-	.send(Agent, tell, accept_proposal(CNPId));
      	!result(CNPId, T, Wagent).

// Announce to others      
+!result(CNPId,[offer(_, Agent)|T], Wagent): Agent \== Wagent
	<-	.send(Agent, tell, reject_proposal(CNPId));
      	!result(CNPId, T, Wagent).

// Case the list of offers is empty      
+!result(_,[],_).