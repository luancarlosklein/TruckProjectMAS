// Internal action code for project discharge_truck

package actions;

import java.util.Map;
import java.util.Random;

import entities.model.Truck;
import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class initializeWorker extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	private Map<Integer, Truck> truckMap = DischargeEnv.model.getWorld().getTruckMap();
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: worker's name
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Random rand = new Random();
    	Worker w = workerMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	
    	ts.getAg().addBel(Literal.parseLiteral("id(" + w.getId() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("pos(" + w.getPos().x + "," + w.getPos().y +")"));
    	ts.getAg().addBel(Literal.parseLiteral("battery(" + w.getBattery() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("qtdGoal(" + w.getQtdGoals() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("jumbled(" + w.getJumbled() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("truckStatus(full)"));
    	ts.getAg().addBel(Literal.parseLiteral("hand_in(none)"));
    	ts.getAg().addBel(Literal.parseLiteral("dropLocal(none)"));
    	ts.getAg().addBel(Literal.parseLiteral("qtdDischarge(0)"));
    	ts.getAg().addBel(Literal.parseLiteral("capacityHelper(0)"));
    	ts.getAg().addBel(Literal.parseLiteral("helper(true)"));
    	ts.getAg().addBel(Literal.parseLiteral("busy(false)"));
    	ts.getAg().addBel(Literal.parseLiteral("time(0)"));
		
		for(Truck t : truckMap.values())
		{
			ts.getAg().addBel(Literal.parseLiteral("plays(initiator," + t.getName() + ")"));
			ts.getAg().addBel(Literal.parseLiteral("posTruck(" + t.getPos().x + "," + t.getPos().y + ")"));
			ts.getAg().addBel(Literal.parseLiteral("qtdTruck(" + t.getName() + "," + t.getQtdThings() + ")"));
			
			if(rand.nextDouble() <= 0.6)
				ts.getAg().addBel(Literal.parseLiteral("truckGet(" + t.getName() + ")"));	
		}
        return true;
    }
}