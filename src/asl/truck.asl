// Agent truck in project discharge_truck

/* Initial beliefs and rules */
qtdThings(0).//Qtd inside of this truck

/* Initial goals */

!start.

/* Plans */


+!needBox[source(Ag)]: true
<- //request_box_t1;
   
   .print("MANDEI UMA CAIXAAAAAAAAAA.");
   .send(Ag, tell, mand(5, drop2)).

+!start : true <- .print("hello world.").
