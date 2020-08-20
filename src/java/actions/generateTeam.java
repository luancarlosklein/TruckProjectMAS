// Internal action code for project discharge_truck

package actions;

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

public class generateTeam extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();
	
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: worker's name
	 * args[1]: CNPId
	 * args[2]: list of offers
	 * args[3]: return (a team of helpers)
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Map<Helper, Structure> offerMap = new HashMap<Helper, Structure>();

    	// Getting worker
    	Worker worker = workerMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	
    	// Getting the team id
    	NumberTerm cnpid = (NumberTerm) args[1];
    	int teamId = (int) cnpid.solve();
    	
    	// Getting the map of offers
    	ListTerm offers = (ListTerm) args[2];
    	
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
    	
    	while(!worker.teamIsComplete(teamId) && !offerMap.keySet().isEmpty())
    	{    	
    		Helper helper = selectTheBest(offerMap);    		
    		worker.addHelperToTeam(teamId, helper);
    		offerMap.remove(helper);
    	}
    	return un.unifies(worker.getMembersAsTermList(teamId), args[3]);
    }
    
    /**
     * This method selects the best offer and returns the helper that made this offer.
     * At this moment, the choice is done randomly.
     * @param offerMap: a map compound of all offer made by helpers.
     * @return the helper that made the best offer. 
     */
    private Helper selectTheBest(Map<Helper, Structure> offerMap)
    {
    	Random rand = new Random();
    	Helper[] helpers = offerMap.keySet().toArray(new Helper[offerMap.keySet().size()]);
    	return helpers[rand.nextInt(offerMap.keySet().size())];
    }
}