// Agent truck in project discharge_truck
/* Initial beliefs and rules */
qtdThings(0).//Qtd inside of this truck
myPos(4, 9).//The agent position 
start(true).//Generate the truck
startP(true).//Make a delay to call a CNP
hourStartTrue(false).




//////////////////////////////////////////////////////////////////////////////////

//For the call for helper (contract net)
all_proposals_received(CNPId) :- 
  .count(introduction(participant,_),NP) & // number of participants
  .count(propose(CNPId,_), NO) &      // number of proposes received
  .count(refuse(CNPId), NR) &         // number of refusals received
  NP = NO + NR.

//////////////////////////////////////////////////////////////////////////////////

/* Initial goals */
+restartTruck(true): true
<-
//	.wait(5000); 
	.print("Restart the truck!(T2)");
//	-box(_,_);	
	-cnp_state(_,_);
	-confirmation(_);
	-qtdOffers(_);
	-+qtdThings(0);
	!remake;
	-+start(true).


+!generateProfile:true
<- discharge_truck.GenerateProfile.


+!remake:true
<- discharge_truck.RemakeAgentMind.

+!start: true
<- discharge_truck.GenerateTruck.

+!createMind: true
<- discharge_truck.CreateMindTruck.

//Make a delay, for to avoid the conflict to the other truck
+startP(true): true
<- 	.wait(3000).

+!start: true
<- discharge_truck.GenerateTruck.

@aS[atomic]
+start(true): hourStartTrue(true) 
<-  
	!start;
	?truckloadCurrently(L);
	!generateNextBox(L);
	!startCNP(5);
	!generateProfile;
	-+start(false).
	

@a[atomic]
+start(true): true 
<-  .wait("+hourStart(_)[source(truck1)]");
    -+hourStartTrue(true);
    !createMind;
	!start;
	?truckloadCurrently(L);
	!generateNextBox(L);
	!startCNP(5);
	!generateProfile;
	-+start(false);
	.
	
//Make this fot call a worker
+!doANewCnp(X): qtdThings(Y) & Y == 0
<-  .wait(1000);
	.print("The truck is empty now! Nothing tho discharge here (T2)!Let's generate another");
	.concat("+restartTruck(true)",Event);
    .at("now +4 seconds", Event).

//Make this fot call a worker
+!doANewCnp(X): qtdThings(Y) & Y > 0
<-  .print("I have a box! Is someone to discharge?(T2)");
	.wait(3000);
	!startCNP(X).

//Make the truck fanatic to discharge all
+!doANewCnp(X): true
<- .wait(5000);
	.print("The doNewCnp not work! Do it again! (T2)");
   !startCNP(X).
	
+!generateNextBox([]): true
<- 	.print("The truck is empty now! Nothing tho discharge here (T2)!").  

+!generateNextBox([box(W,D)|T]): true
<- 	-+box(W,D);
     -+truckloadCurrently(T).
    	 				
+?boxRe(Peso, DROPL)
   :  box(Peso, DROPL)
   <- 
    ?truckloadCurrently(L);
	!generateNextBox(L);      
    ?qtdThings(Z);
	Y = Z - 1;
	-+qtdThings(Y);
   .print("I send a box (T2)");
   !doANewCnp(5).
	
//The winner is already busy, do a new cnp
@puBack[atomic]
+failureCnpForTruck(true)[source(Ag)]: true
<-  
    ?proposalWin(CNPId,Capa, Time, Ag);
	-proposalWin(CNPId,Capa, Time, Ag);
	-failureCnpForTruck(true)[source(Ag)];
	//The agent not work! 
	.time(H, M, S);
	?hourStart(HourStart)[source(truck1)];
	Ti = H*60*60 + M*60 + S - HourStart; 
	//.my_name(Me);
	//+impression(self, Ag, Ti, [Capa, Time], [0,0]);
	//.send([truck1, truck3], tell, impression(Me, Ag, Ti, [Capa, Time], [0,0]));
	.print("I need a new CNP! The winner agent is busy now! (T2)");
	.wait(3000);
	!startCNP(5).

+!sendBox([box(W,D)|T], WAg, CNPId): true
<- 	-+box(W,D);
	?qtdThings(Z);
	Y = Z - 1;
	-+qtdThings(Y);
	//.send(WAg,tell,accept_proposal(CNPId, truck2, box(W, D)));
	.print("I send a box!(T2)");
     -+truckloadCurrently(T).
				 				 
// start the CNP
@cnp9
+!startCNP(Id) 
<-  -+cnp_state(Id,propose);   // remember the state of the CNP
    .findall(Name,introduction(participant,Name),LP);
    //.print("Sending CFP to ",LP);
    .send(LP,tell,cfp(Id));
    .concat("+!contract(",Id,")",Event);
    // the deadline of the CNP is now + 4 seconds, so
    // the event +!contract(Id) is generated at that time
    .at("now +1 seconds", Event).

-!startCNP(Id) <- 
  !startCNP(Id).
 
+!selectWorker: true
<- discharge_truck.SelectForCnp.

// receive proposal 
// if all proposal have been received, don't wait for the deadline
@r1 
+propose(CNPId,Offer,Time)[source(A)]:  
cnp_state(CNPId,propose) & all_proposals_received(CNPId)
<- 
  -propose(CNPId,Offer, Time)[source(A)];
  +proposeA(CNPId, Offer, Time, A);
  !contract(CNPId).

@r1T[atomic] 
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
   		.print("Winner is ",WAg," with ",WOf, " Time: ", Time, " T2");
   		!announce_result(CNPId,L,WAg); 
   		-+cnp_state(CNPId,finished);
    }   
   -winnerCnp(_,_);
   .abolish(proposeA(5,_,_,_));
   .abolish(propose(5,_,_));
   .abolish(refuse(5)).

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(CNPId) <- true.

+qtdOffers(0): qtdThings(Y) & Y > 0
<- .print("CNP has failed!: No prosose (T2)");
   .wait(7000);
   !startCNP(5).

-!contract(CNPId): qtdThings(Y) & Y > 0
<- .print("CNP ",CNPId," has failed! (T2)");
   .wait(7000);
   !startCNP(5).
 
-!contract(CNPId): qtdThings(Y) & Y <= 0
<- .print("CNP ",CNPId," has failed! Stop now!(T2)").

+!announce_result(_,[],_).
// announce to the winner

+!announce_result(CNPId,[offer(O,WAg)|T],WAg) 
<-  
	.send(WAg,tell,accept_proposal(CNPId, truck2));     
    !announce_result(CNPId,T,WAg).
      
// announce to others
+!announce_result(CNPId,[offer(O,LAg)|T],WAg) 
<- .send(LAg,tell,reject_proposal(CNPId));
   !announce_result(CNPId,T,WAg).

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
	?hourStart(HourStart)[source(truck1)];
	Ti = H*60*60 + M*60 + S - HourStart;
	+impression(self, A, Ti, [Capa, Time], [J,CapaScore]);
	.my_name(Me);
	.send([truck1, truck3], tell, impression(Me, A, Ti, [Capa, Time], [J,CapaScore]));
	-leaving(TIME, WeightBox)[source(A)];
	-proposalWin(CNPId, Capa, Time, A).
	
+!calculateImage <- discharge_truck.CalculateImage.
+!calculateReputation <- discharge_truck.CalculateReputation.

+impression(self,I,J,K,O): true
<-
   !calculateImage;
   !calculateReputation.
   


+impression(A,I,J,K,O)[source(A)]
<- 
	!calculateReputation.


