// Internal action code for project discharge_truck

package actions.trucker;

import java.util.Map;

import entities.model.Truck;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

/**
 * This action initializes a trucker.
 */
public class initialize extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Truck> truckMap = DischargeEnv.model.getWorld().getTruckMap();
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: Truck's name
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Truck t = truckMap.get(Integer.parseInt(args[0].toString().split("_")[1]));    
    	
    	ts.getAg().addBel(Literal.parseLiteral("id(" + t.getId() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("visible(" + t.isVisible() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("qtd_things(" + t.getQtdThings() + ")"));
		ts.getAg().addBel(Literal.parseLiteral("pos(" + t.getPos().x + ", " + t.getPos().y + ")"));
		ts.getAg().addBel(Literal.parseLiteral("cargo_type(" + t.getCargoType().name().toLowerCase() + ")"));
		ts.getAg().addBel(Literal.parseLiteral("unload_time(" + t.getUnloadTime() + ")"));
  
        return true;
    }
}