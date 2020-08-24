// Internal action code for project discharge_truck

package actions;

import java.util.Map;

import entities.model.Truck;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

/**
 * This class implements an action executed by an agent.
 * Action: initialize a trucker.
 */

public class initializeTruck extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Truck> truckMap = DischargeEnv.model.getWorld().getTruckMap();
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: trucker's name
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Truck t = truckMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	
    	t.setQtdThings(10);
    	
    	ts.getAg().addBel(Literal.parseLiteral("id(" + t.getId() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("qtd_things(" + t.getQtdThings() + ")"));
		ts.getAg().addBel(Literal.parseLiteral("pos(" + t.getPos().x + ", " + t.getPos().y + ")"));
		ts.getAg().addBel(Literal.parseLiteral("cargo_type(" + t.getCargoType().name().toLowerCase() + ")"));
		ts.getAg().addBel(Literal.parseLiteral("unload_time(" + t.getUnloadTime() + ")"));
  
        return true;
    }
}