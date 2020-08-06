
//////Move//////////////////////
//Check if the agent arrived to the right place

move(truck_0).

+move(Target) : true
	<-	!at(Target).

//Take a step towards
@m2
+!at(Target) : not at(Target)
	<-	move_towards(Target);
		!at(Target).