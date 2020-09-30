// Internal action code for project discharge_truck

package actions.trucker;

import entities.model.Truck;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;

/**
 * This actions gets the next trucker from the list of truckers
 */
public class getNext extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Arguments (come from parameter args):
	 * @return args[0]: trucker's name.
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
		Truck t = DischargeEnv.model.getWorld().getNextTruck();
		return un.unifies(new Atom(t.getName()), args[0]);
    }
}
