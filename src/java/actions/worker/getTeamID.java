// Internal action code for project discharge_truck

package actions.worker;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

/**
 * This actions generates the Id for a team.
 */
public class getTeamID extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: worker's name
	 * @param args[1]: trucker's name
	 * @return args[2]: a new id.
	 */
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
		String id1 = args[0].toString().split("_")[1];
		String id2 = args[1].toString().split("_")[1];
		return un.unifies(new NumberTermImpl(Integer.parseInt(id1 + id2)), args[2]);
    }
}