// Internal action code for project discharge_truck

package actions.trucker;

import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

/**
 * This actions generates the cnpid sequence.
 */
public class getNextCNPId extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: trucker's id.
	 * @return args[1]: next cnpid.
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
		int nextId = DischargeEnv.cnpid.getAndIncrement() + 1;
		int id = Integer.parseInt(nextId + args[0].toString());
		
    	return un.unifies(new NumberTermImpl(id), args[1]);
    }
}