// Internal action code for project discharge_truck

package actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

/**
 * Action's arguments (from args parameter):
 * args[0]: Element 1
 * args[1]: ELement 2
 * args[2]: compound id
 */
public class getCompoundID extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;

	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
		String id1 = args[0].toString().split("_")[1];
		String id2 = args[1].toString().split("_")[1];
		
		return un.unifies(new NumberTermImpl(Integer.parseInt(id1 + id2)), args[2]);
    }
}
