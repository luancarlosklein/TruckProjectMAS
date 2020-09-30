// Internal action code for project discharge_truck

package actions.trucker;

import java.util.Map;

import entities.model.Truck;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * This action inserts the trucker in the waiting queue.
 */
public class goToWaitingQueue extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Truck> truckMap = DischargeEnv.model.getWorld().getTruckMap();

	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: Agent's name
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
		Truck t = truckMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
		DischargeEnv.model.getWorld().getTruckersOrder().add(t);
        return true;
    }
}
