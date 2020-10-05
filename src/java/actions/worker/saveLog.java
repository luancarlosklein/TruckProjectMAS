package actions.worker;

import java.io.FileWriter;
import java.util.Map;

import entities.model.HelperTeam;
import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * This action saves the data about a worker in a txt file.
 * This action is used to debug the behavior of a worker. 
 * Each file is created in debugger folder (PATH: debugger/agent_name.txt).
 */
public class saveLog extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: worker's name
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {		
		Worker w = workerMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
		FileWriter file = new FileWriter("debugger/" + w.getName() + ".txt", true);
		
		file.write("TIME(" + System.currentTimeMillis() + "),MESSAGE(" + args[1].toString() + "):\n");
		
		for(HelperTeam team : w.getTeams())
		{
			file.write("- "+ team +" \n");
		}
		file.write("\n");
		file.close();
        return true;
    }
}