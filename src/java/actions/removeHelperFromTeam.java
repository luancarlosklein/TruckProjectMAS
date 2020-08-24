// Internal action code for project discharge_truck

package actions;

import java.util.Map;

import entities.model.Helper;
import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class removeHelperFromTeam extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();

	/**
	 * Action's arguments (from args parameter):
	 * args[0]: teamId
	 * args[1]: worker's name
	 * args[2]: Helper's name
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
		// Getting the input parameters
		NumberTerm teamId = (NumberTerm) args[0]; 
		Worker worker = workerMap.get(Integer.parseInt(args[1].toString().split("_")[1]));
		Helper helper = helperMap.get(Integer.parseInt(args[2].toString().split("_")[1]));
		
		// Removing helper from team
		worker.removeHelperFromTeam((int) teamId.solve(), helper);
		
        return true;
    }
}