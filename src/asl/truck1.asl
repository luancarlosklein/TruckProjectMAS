// Agent truck in project discharge_truck
/* Initial beliefs and rules */
qtdThings(0).//Qtd inside of this truck
myPos(0, 9).//The pos in the map
start(true).//Generate a truck

image(worker0, 0, 0).
image(worker1, 0, 0).
image(worker2, 0, 0).

reputation(worker0, 0, 0).
reputation(worker1, 0, 0).
reputation(worker2, 0, 0).

//////////////////////////////////////////////////////////////////////////////////

//For the call for helper (contract net)
all_proposals_received(CNPId) :- 
  .count(introduction(participant,_),NP) & // number of participants
  .count(propose(CNPId,_), NO) &      // number of proposes received
  .count(refuse(CNPId), NR) &         // number of refusals received
  NP = NO + NR.


//////////////////////////////////////////////////////////////////////////////////

+myPos(X, Y): true
<- .time(H, M, S);
	L = H*3600 + M*60 + S;
	+hourStart(L);
	.send([truck2, truck3], tell, hourStart(L)). 

/* Initial goals */


+restartTruck(true): true
<-
	.print("Restart the truck!(T1)");
	-cnp_state(_,_);
	-confirmation(_);
	-qtdOffers(_);
	-+qtdThings(0);
	!remake;
	-+start(true).

+!remake:true
<- discharge_truck.RemakeAgentMind.

+!start: true
<- discharge_truck.GenerateTruck.

@a[atomic]
+start(true): true 
<- 
	!start;
	?truckloadCurrently(L);
	!generateNextBox(L);
	!startCNP(4);
	-+start(false).    

+!generateNextBox([]): true
<- 	.print("The truck is empty now! Nothing tho discharge here (T1)!").  

+!generateNextBox([box(W,D)|T]): true
<- 	-+box(W,D);
	//.send(WAg,tell,accept_proposal(CNPId, truck2, box(W, D)));
     -+truckloadCurrently(T).
			
//Make this fot call a worker
+!doANewCnp(X): qtdThings(Y) & Y == 0
<-  .wait(1000);
	.print("The truck is empty now! Nothing tho discharge here (T1)! Let's generate another");
	.concat("+restartTruck(true)",Event);
    .at("now +6 seconds", Event).
    
//Make this fot call a worker
+!doANewCnp(X): qtdThings(Y) & Y > 0
<-  .print("I have a box! Is someone to discharge?(T1)");
	.wait(3000);
	!startCNP(X).

//Make the truck fanatic to discharge all
+!doANewCnp(X): true
<- .wait(5000);
   .print("The doNewCnp not work! Do it again! (T1)");
   !startCNP(X).

				 				
+?boxRe(Peso, DROPL)
   :  box(Peso, DROPL)
   <- 
    ?truckloadCurrently(L);
	!generateNextBox(L);      
    ?qtdThings(Z);
	Y = Z - 1;
	-+qtdThings(Y);
   .print("I send a box! (T1)");
   !doANewCnp(4).
 
//The winner is already busy, do a new cnp
@puBack[atomic]
+failureCnpForTruck(true)[source(Ag)]: true
<-  
    ?proposalWin(CNPId,Capa, Time, Ag);
	-proposalWin(CNPId,Capa, Time, Ag);
	-failureCnpForTruck(true)[source(Ag)];
	.time(H, M, S);
	Ti = H*60*60 + M*60 + S;
	//+impression(self, Ag, Ti, [Capa, Time], [0,0]);
	//.my_name(Me);
	//.send([truck2, truck3], tell, impression(Me, Ag, Ti, [Capa, Time], [0,0]));
	.print("I need a new CNP! The winner agent is busy now! (T1)");
	.wait(3000);
	!startCNP(4).

//To send the information about the box for the winner agent
+!sendBox([box(W,D)|T], WAg, CNPId): true
<- 	-+box(W,D);
	?qtdThings(Z);
	Y = Z - 1;
	-+qtdThings(Y);
	//.send(WAg,tell,accept_proposal(CNPId, truck1, box(W, D)));
	.print("I send a box! (T1)");
     -+truckloadCurrently(T).
				 			 
//Get the box, and put it in the charge, again
+!putBoxBack: true
<- 
	?qtdThings(Z);
	Y = Z + 1;
	-+qtdThings(Y);
	discharge_truck.PutBoxBack.

// start the CNP
@cnp9
+!startCNP(Id) 
<-  -+cnp_state(Id,propose);   // remember the state of the CNP
    .findall(Name,introduction(participant,Name),LP);
    .send(LP,tell,cfp(Id));
    .concat("+!contract(",Id,")",Event);
    // the deadline of the CNP is now + 4 seconds, so
    // the event +!contract(Id) is generated at that time
    .at("now +1 seconds", Event).

//If os somenthing wrong, try to make again
-!startCNP(Id) <- 
  !startCNP(Id).
 
//Call the fuction to select the best propose
+!selectWorker: true
<- discharge_truck.SelectForCnp.

// receive proposal 
// if all proposal have been received, don't wait for the deadline
@r1
+propose(CNPId,Offer,Time)[source(A)]:  
cnp_state(CNPId,propose) & all_proposals_received(CNPId)
<- 
   -propose(CNPId,Offer, Time)[source(A)];
   //Transforme the propose to another form (this is useful to the select, and the diferent name help us to don't have any problem with this)
   +proposeA(CNPId, Offer, Time, A);
	!contract(CNPId).
	
//Same to the other, but is no the final 
@r1T 
+propose(CNPId,Offer, Time)[source(A)]: true
<- 
    -propose(CNPId,Offer, Time)[source(A)];
	+proposeA(CNPId, Offer, Time, A).

// receive refusals   
@r2 +refuse(CNPId):  cnp_state(CNPId,propose) & all_proposals_received(CNPId)
<- !contract(CNPId).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId)
<- -+cnp_state(CNPId,contract);
   .findall(offer(O,A),proposeA(CNPId,O,T,A),L);
   .length(L, S);
   -+qtdOffers(S);
    if(S > 0)
    {
    	!selectWorker;
    	?winnerCnp(WAg, WOf);
    	?proposeA(CNPId, P, Time, WAg);
    	+proposalWin(CNPId, P, Time, WAg);
   		.print("Winner is ",WAg," with ",WOf, " Time: ", Time, " (T1)");
   		!announce_result(CNPId,L,WAg); 
   		-+cnp_state(CNPId,finished);
    }   
   -winnerCnp(_,_);
   .abolish(proposeA(4,_,_,_));
   .abolish(propose(4,_,_,_));
   .abolish(refuse(4)).

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(CNPId) <- true.

//If the cnp has no propose
+qtdOffers(0): qtdThings(Y) & Y > 0
<- .print("CNP has failed!: No prosose (T1)");
   .wait(5000);
   !startCNP(4).

//The cnp has failed, but there are box in the truck 
-!contract(CNPId): qtdThings(Y) & Y > 0
<- .print("CNP ",CNPId," has failed! (T1)");
   .wait(5000);
   !startCNP(4).
 
//The cnp has failed and there aren't box int he truck
-!contract(CNPId): qtdThings(Y) & Y <= 0
<- .print("CNP ",CNPId," has failed! Stop now!(T1)").

+!announce_result(_,[],_).
// announce to the winner

+!announce_result(CNPId,[offer(O,WAg)|T],WAg) 
<-  .send(WAg,tell,accept_proposal(CNPId, truck1));
    !announce_result(CNPId,T,WAg).
      
// announce to others
+!announce_result(CNPId,[offer(O,LAg)|T],WAg) 
<- .send(LAg,tell,reject_proposal(CNPId));
   !announce_result(CNPId,T,WAg).

//To verify the times to the agent
//The time the agent is leaving
@gos[atomic]	
+leaving(TIMESpend, WeightBox)[source(A)]: true
<- 	
	?proposalWin(CNPId, Capa, Time, A);
	X = TIMESpend - Time; 
	.print("The agent has to spend ", X, " seconds to get the box");
	//Actualize the confiabily about the agents
	
	if (Capa > WeightBox)
	{
		CapaScore = 1;
	}
	else
	{
		CapaScore = Capa / WeightBox;
	}
	
	//Save the impression about the time
	if (X <= 10)
	{
		J = (10 - X)/10;
	}
	else
	{
		J = 0;
	}
	.time(H, M, S);
	?hourStart(HourStart);
	Ti = H*60*60 + M*60 + S - HourStart;
	+impression(self, A, Ti, [Capa, Time], [J,CapaScore]);
	.my_name(Me);
	.send([truck2, truck3], tell, impression(Me, A, Ti, [Capa, Time], [J,CapaScore]));
	-leaving(TIME, WeightBox)[source(A)];
	-proposalWin(CNPId, Capa, Time, A).
	
//////////////////////////////////////////////////////////////////////////////////
//Actualize the Reputation and the image

+!calculateImage <- discharge_truck.CalculateImage.
+!calculateReputation <- discharge_truck.CalculateReputation.


+impression(self,I,J,K,O): true
<-
	!calculateImage;
   !calculateReputation.
   

+impression(A,I,J,K,O)[source(A)]
<- 
	!calculateReputation.



