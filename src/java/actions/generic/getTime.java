// Internal action code for project discharge_truck

package actions.generic;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

/**
 * This action get current time from system.
 */

public class getTime extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Arguments (come from parameter args):
	 * @return args[0]: time in milliseconds.
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
	{
		return un.unifies(new NumberTermImpl(System.currentTimeMillis()), args[0]);
    }
}