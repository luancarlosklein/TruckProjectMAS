// Internal action code for project discharge_truck

package actions;

import java.util.Map;

import entities.model.Helper;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class initializeHelper extends DefaultInternalAction {
	private static final long serialVersionUID = 1L;
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: trucker's name
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Helper h = helperMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	
    	ts.getAg().addBel(Literal.parseLiteral("id(" + h.getId() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("pos(" + h.getPos().x + "," + h.getPos().y +")"));
  
        return true;
    }
}