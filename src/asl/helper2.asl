// Agent ajudante in project discharge_truck

/* Initial beliefs and rules */
id(4).
drop(dropIr).
truck(truckIr).
capacity(20). // :- .random(R) & X = (10*R) + 5.
ajudado(false).
carregando(null).
havePlan(false).
lengthPlan(0).
stepPlan(0).
plan(none).
busy(false).
plays(initiator,worker0). 
plays(initiator,worker1). 
agenteAjudado(none).


/*Rules*/
podeCarregar :- truck(X) & at(Y) & (X == Y) & carregando(false).
podeDescarregar :- drop(X) & at(Y) & (X == Y) & carregando(true).

/*Actions*/

//////Move//////////////////////
//Check if the agent arrived to the right place

+move(X) : true
<- 
   !at(X).

@m1
+!at(P) : at(P) <-
	discharge_truck.DoAction.

//Take a step towards
@m2
+!at(P) : not at(P)
  <- 
  ?id(ID);
  move_towards(P, ID);
  !at(P).

////////////////////////////////////
//Arrived in the position of the worker
@car  
+carregar(X) : true
<- 
   -+carregando(true);
   -+ajudado(false);
   ?agenteAjudado(Ag);
   .print("Carreguei. Agora Vamos!");
   ?capacity(Y);
   .send(Ag,tell,msg(Y));
   discharge_truck.DoAction.
////////////////////////////////////////
      
@des      
+descarregar(X) : true
<-  
	-+carregando(false);
    -+ajudado(true);
    ?agenteAjudado(Ag);
    .print("DESCARREGANDO");
    .send(Ag,tell,arrived(true));
    discharge_truck.DoAction.
    
/////////////////////////////
//Do do first action of the plan
//Start de execution of the plan
+havePlan(true): true
<-  discharge_truck.DoAction.
//////////////////////////


///Contract net/////////////////////////////////
// send a message to the agent introducing myself as a participant (helper)
+plays(initiator,In)
   :  .my_name(Me)
   <- .send(In,tell,introduction(participant,Me)).

// answer to Call For Proposal   
@c1
+cfp(CNPId)[source(A)]
   :  plays(initiator,A) & busy(false) & capacity(Offer)
   <- +proposal(CNPId,Offer); // remember my proposal
      .send(A,tell,propose(CNPId,Offer));
      -cfp(CNPId)[source(A)].


// Refuse a Call for Proposal
+cfp(CNPId)[source(A)]  
   :   plays(initiator,A) & busy(true)
   <- .send(A,tell,refuse(CNPId));
   .print("EU ME RECUSEI!");
      -cfp(CNPId)[source(A)].


@r1Busy
+accept_proposal(CNPId, Truck, Drop)[source(A)]
   :  proposal(CNPId, Offer) & busy(true)
   <- 
   		.send(A, tell, failureCnp(true));
   		-accept_proposal(CNPId, Truck, Drop)[source(A)];
   		.print("My proposal '",Offer,"' won CNP ",CNPId,
             " for! BUT I'm busy now. Sorry!'").

@r1
+accept_proposal(CNPId, Truck, Drop)[source(A)]
   :  proposal(CNPId, Offer)
   <-
   		-+busy(true); 
   		.print("My proposal '",Offer,"' won CNP ",CNPId,
             " for!");        
        -+drop(Drop);
		-+truck(Truck);	
		-accept_proposal(CNPId, Truck, Drop)[source(A)];
		-proposal(CNPId, Offer);
		-+agenteAjudado(A);
		discharge_truck.GeneratePlan.
	  
@r2 +reject_proposal(CNPId)[source(A)]
   <- .print("I lost CNP ",CNPId, ".");
      -proposal(CNPId,_,_);
      -+busy(false);
      -reject_proposal(CNPId)[source(A)]. // clear memory

	