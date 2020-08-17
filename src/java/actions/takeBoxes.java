// Internal action code for project discharge_truck

package actions;

import java.util.Map;

import entities.model.Helper;
import entities.model.Truck;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class takeBoxes extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();
	private Map<Integer, Truck> truckMap = DischargeEnv.model.getWorld().getTruckMap();
	
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: truck's name
	 * args[1]: helper's name
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {    
    	Truck t = truckMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	Helper h = helperMap.get(Integer.parseInt(args[1].toString().split("_")[1]));
    	
    	ts.getAg().delBel(Literal.parseLiteral("carrying(_)"));
    	
    	if(!t.isDischarged())
    	{
	    	int rest = t.getQtdThings() - h.getCapacity();
	    	
	    	// The helper takes the rest of boxes from truck
	    	if(rest <= 0)
	    	{
	    		ts.getAg().addBel(Literal.parseLiteral("carrying(" + t.getQtdThings() + ")"));	    		
	    		t.setQtdThings(0);
	    	}
	    	// The helper takes all possible boxes according with its capacity
	    	else
	    	{
	    		ts.getAg().addBel(Literal.parseLiteral("carrying(" + h.getCapacity() + ")"));
	    		t.setQtdThings(rest);
	    	}
    	}
    	else
    	{
    		// Inform that helper is not carrying any box
    		ts.getAg().addBel(Literal.parseLiteral("carrying(0)"));
    		
    		// Update the status of truck in the helper's mind
    		ts.getAg().delBel(Literal.parseLiteral("empty_truck(_)"));
    		ts.getAg().addBel(Literal.parseLiteral("empty_truck(true)"));
    		
    		// The helper is free to accept others contracts
    		ts.getAg().delBel(Literal.parseLiteral("busy(_)"));
    		ts.getAg().addBel(Literal.parseLiteral("busy(false)"));
    	}
    	
        return true;
    }
}