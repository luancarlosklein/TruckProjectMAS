// Internal action code for project discharge_truck

package actions;

import java.util.Map;

import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class checkIfTeamIsReady extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: teamId
	 * args[1]: worker's name
	 * args[2]: list of offers
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
		
		if(ready && offers.isEmpty())
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
