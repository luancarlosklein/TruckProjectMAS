// Agent truck in project discharge_truck

/* Initial beliefs and rules */
qtdThings(0).//Qtd inside of this truck

/* Initial goals */

!start.

/* Plans */


+needBox[source(A)]
<- request_box_t1;
   ?qtdThings(X);
   .send(A, tell, doBox).

+!start : true <- .print("hello world.").
