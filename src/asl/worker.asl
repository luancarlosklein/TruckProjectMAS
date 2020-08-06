//Stop the walk, for general cases
//@m1
//+!at(P) : at(P)  
//<-	true.
//	
////Take a step towards
//@m2
//+!at(P) : not at(P)
//<- ?id(ID); 
//  	move_towards(P, ID);
//  	?batery(X);
//  	-+batery(Y);
//  	?time(T);
//  	NT = T + 1;
//  	-+time(NT);
//    !at(P).