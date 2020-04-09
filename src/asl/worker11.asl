// Agent ajudante in project discharge_truck

/* Initial beliefs and rules */

/* Initial goals */
drop(dropIr).
truck(truckIr).
capacity(10).

ajudado(false).
carregando(null).
havePlan(false).
lengthPlan(0).

podeCarregar :- truck(X) & at(Y) & (X == Y) & carregando(false).

podeDescarregar :- drop(X) & at(Y) & (X == Y) & carregando(true).

///////////Actions

//////Move//////////////////////
//Check if the agent arrived to the right place
@m1
+!at(P) : at(P) <-
	nextStep(11);
	.print("FINALIZADO").

//Take a step towards
@m2
+!at(P) : not at(P)
  <- move_towards(P, 11);
  .print("DANDO UM PASSO normal")
  !at(P).
////////////////////////////////////

+move(X) : true
<- 
   .print("DANDO UM PASSO para ", X)
   !at(X).

+carregar(X) : true
<- 
   -+ carregando(true);
   -+ ajudado(false);
   ?agenteAjudado(Ag);
   .print("Carreguei. Agora Vamos!");
    nextStep(11);
   .send(Ag,tell,msg(10)).
   
+descarregar(X) : true
<-  
	nextStep(11);
	-+ carregando(false);
    -+ ajudado(true);
    ?agenteAjudado(Ag);
    .print("DESCARREGANDO");
    .send(Ag,tell,msg("Arrived")).
   
    
+msg(M, Drop)[source(Ag)] : true
	<- 
		.print("O agente ",Ag," pediu ajuda!");
		.print("Truck: ", M);
		.print("Drop: ",Drop);
		-+drop(Drop);
		-+truck(M);
		-msg(M, Drop)[source(Ag)];
		-+agenteAjudado(Ag);
		generatePlan(11).
		
	