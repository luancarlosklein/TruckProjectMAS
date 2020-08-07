move(truck_0).

+move(Target): true
	<-	!at(Target).
	
@m1
+!at(Target): at(Target) 
	<-	.print("I arrived!").

//Take a step towards
@m2
+!at(Target): not at(Target)
	<-	move_towards(Target);
		!at(Target).