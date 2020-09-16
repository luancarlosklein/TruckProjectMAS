package actions.worker;

import java.util.Map;

import entities.model.Helper;
import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

/**
 * This action updates the status of a member of a team to hired.
 */
public class hireHelper extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: teamId
	 * @param args[1]: worker's name
	 * @param args[2]: Helper's name
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
		// Getting the input parameters
		NumberTerm teamId = (NumberTerm) args[0]; 
		Worker worker = workerMap.get(Integer.parseInt(args[1].toString().split("_")[1]));
		Helper helper = helperMap.get(Integer.parseInt(args[2].toString().split("_")[1]));
		
		// Updating the helper status, the helper becomes a member of team.
		worker.hireHelper((int) teamId.solve(), helper);
        return true;
    }
}
