package actions.worker;

import java.util.Map;

import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

/**
 * This checks if a team is ready to start the service.
 */
public class teamIsReady extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: teamId
	 * @param args[1]: worker's name
	 * @param args[2]: list of offers
	 */
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	// Getting the input parameters
		NumberTerm teamId = (NumberTerm) args[0]; 
		Worker worker = workerMap.get(Integer.parseInt(args[1].toString().split("_")[1]));
		ListTerm offers = (ListTerm) args[2];
		
		// Check if the team is ready to execute the task, if all member accept the invite
		int id = (int) teamId.solve();
		boolean ready = worker.teamIsReady((int) teamId.solve());
		
		if(!worker.teamIsFull((int) teamId.solve()) && !offers.isEmpty())
		{
			return false;
		}
		else if(ready && offers.isEmpty())
		{
			worker.setTeamAsReady(id);
			return true;
		}
		else if(ready && !offers.isEmpty() && worker.teamIsFull(id))
		{
			worker.setTeamAsReady(id);
			return true;
		}
		else if(ready && offers.isEmpty() && !worker.teamIsFull(id))
		{
			worker.setTeamAsReady(id);
			return true;
		}
		else return false;
    }
}