// Agent ajudante in project discharge_truck

/* Initial beliefs and rules */

/* Initial goals */
capacity(20).
ajudando(null).
truck(null).

+msg(M)[source(Ag)] : ajudando(null)
	<- .print("O agente ",Ag," pediu ajuda!");
		-+truck(M);
		-+ajudando(Ag);
		//!at(M);
		
		!at(M)[atomic];
		!arrived(Ag)[atomic];
		-msg(M)[source(Ag)].
	

+msg(M)[source(Ag)] : not ajudando(null)
	<- 	!at(M);
		.print("TERMINEI MINHA MISSÃO!");
		-+ajudando(null);
		-+truck(null);
		-msg(M)[source(Ag)].
	
//Check if the agent arrived to the right place
@m1
+!at(P) : at(P) <- true.

//Take a step towards
@m2
+!at(P) : not at(P)
  <- move_towards(P, 11);
    !at(P).
    
+!arrived(Ag): true
<-
	?capacity(X);
    .send(Ag,tell,msg(X));
	.print("Cheguei!").
	
+msg(M)[source(Ag)] : true
	<- .print("NÃO DEVERIA CHEGAR NESSA PARTE");
		-msg(M)[source(Ag)].
		
   