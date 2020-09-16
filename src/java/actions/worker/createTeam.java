// Internal action code for project discharge_truck

package actions.worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import entities.model.Helper;
import entities.model.HelperTeam;
import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;

/**
 * This actions may create a new team of helper or update an existing team.
 */
public class createTeam extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: CNPId
	 * @param args[1]: worker's name
	 * @param args[2]: list of offers
	 * @param args[3]: number of boxes to be unload from truck
	 * @param args[4]: time to perform the task
	 * @return args[5]: a team of helpers
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Map<Helper, Structure> offerMap = new HashMap<Helper, Structure>();

    	// Getting the team id
    	NumberTerm cnpid = (NumberTerm) args[0];
    	int teamId = (int) cnpid.solve();
    	
    	// Getting worker
    	Worker worker = workerMap.get(Integer.parseInt(args[1].toString().split("_")[1]));
    	
    	// Getting the map of offers
    	ListTerm offers = (ListTerm) args[2];
    	
    	// Getting the number of boxes and the time of task
    	NumberTerm nbBoxes = (NumberTerm) args[3];
    	NumberTerm time = (NumberTerm) args[4];
    	
    	for(Term term : offers) 
    	{
    		Structure offer = (Structure) term;
    		offerMap.put(helperMap.get(Integer.parseInt(offer.getTerm(1).toString().split("_")[1])), (Structure) offer.getTerm(0));
    	}
    	
    	/* CREATING A TEAM 
    	 * In this point the worker has to decide which helper will be added to team.
    	 * Thus, reputation, image, and trust information must be taken into account in such making decision process.
    	 */
    	if(!worker.containsTeam(teamId))
    		worker.addTeam(new HelperTeam(teamId, 2));
    	
    	while(!worker.teamIsFull(teamId) && !offerMap.keySet().isEmpty())
    	{    	
    		Helper helper = selectTheBest(offerMap, (int) nbBoxes.solve(), (long) time.solve());    		
    		worker.addHelperToTeam(teamId, helper);
    		offerMap.remove(helper);
    	}
    	return un.unifies(worker.getNotReadyMembersAsTermList(teamId), args[5]);
    }
    
    /**
     * This method selects the best offer and returns the helper that made this offer.
     * At this moment, the choice is done randomly.
     * @param offerMap: a map compound of all offer made by helpers.
     * @param nbBoxes: number of boxes to be discharge from truck.
     * @param time: time to perform the task.
     * @return the helper that made the best offer. 
     */
    private Helper selectTheBest(Map<Helper, Structure> offerMap, int nbBoxes, long time)
    {
    	Random rand = new Random();
    	Helper[] helpers = offerMap.keySet().toArray(new Helper[offerMap.keySet().size()]);
    	return helpers[rand.nextInt(offerMap.keySet().size())];
    }
}