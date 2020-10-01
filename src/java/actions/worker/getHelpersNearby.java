// Internal action code for project discharge_truck

package actions.worker;

import java.util.Map;

import entities.model.Helper;
import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

/**
 * This action returns a list of helpers nearby of worker.
 */
public class getHelpersNearby extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: worker's name
	 * @return args[1]: list of helpers
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Worker worker = workerMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	
    	ListTerm helpers = new ListTermImpl();
    	int x = worker.getPos().x + worker.getSeekRange();
    	int y = worker.getPos().y + worker.getSeekRange();
    	
    	for(Helper h : helperMap.values())
    	{
    		if(h.getPos().x <= x && h.getPos().y <= y)
    		{
    			helpers.add(Atom.parseLiteral(h.getName()));
    		}
    	}
    	return un.unifies(helpers, args[1]);
    }
}