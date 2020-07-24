// Basic beliefs and plans of an worker agent

/* BELIEFS *************/
capacity(4). 		// Maximum weight that agent can carry
batery(100). 		// Battery level (0% - 100%)
qtdGoal(10). 		// Number of goals of agent (boxes to be to discharged)
jumbled(0.9).

posTruck1(0, 9).
posTruck2(4, 9).
posTruck3(4, 9).

positionDrop(0,0). 	// Position where a box must be discharged


truckStatus(full). 	// Status of truck at the moment
hand_in(none). 		// If the agent is carrying something
dropLocal(none). 	// The right place to drop the box
qtdDischarge(0). 	// Boxes discharged by the agent
truckGet(truck). 	// The truck which the agent is responsible
capacityHelper(0).	// Extra capacity reached with help from the helper
helper(true). 		// The variable is false when the agent depends on a helper
busy(false).
time(0).

plays(initiator,truck1).
plays(initiator,truck2). 
plays(initiator,truck3). 

qtdTruck1(0).
qtdTruck2(0).
qtdTruck3(0).


/* RULES *************/

/* PLANS *************/