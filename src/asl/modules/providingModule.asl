// This module implements the providing role of contract-net-protocol (CNP)
/***
 * For using the CNP it is recommended to follow the calling order bellow:
 * !register(Role, Initiator);
 * +cnp(CNPId, Task)(MUST BE IMPLEMENTED: it creates and sends a proposal)
 * +accept_proposal(CNPId)(MUST BE IMPLEMENTED: it executes the delegated task)
 * +reject_proposal(CNPId)
 ***/

/* PLANS *************/

/**
 * Register the agent as a provider of some service 
 * @param Role: type of provided service
 * @param Initiator: to whom the service will be provided  
 * */         
+!register(Role, Initiator): true
	<-	.df_register(Role);
		.df_subscribe(Initiator);
.