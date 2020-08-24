// Internal action code for project discharge_truck

package actions;

import java.util.Collection;
import java.util.Map;

import entities.model.Helper;
import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;

public class proposeOfferWorker extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();

	/**
	 * Action's arguments (from args parameter):
	 * args[0]: teamId
	 * args[1]: worker's name
	 * args[2]: return (an offer)
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception
    {
		// Getting the input parameters
		NumberTerm teamId = (NumberTerm) args[0]; 
		Worker worker = workerMap.get(Integer.parseInt(args[1].toString().split("_")[1]));
	
		// Returning an offer
		return un.unifies(generateOffer(worker.getTeamMembers((int) teamId.solve())), args[1]);
    }
	
	private Structure generateOffer(Collection<Helper> teamMembers)
	{
		double score = 0;
		long time = 0l;
		
		for(Helper helper: teamMembers)
		{
			score += (helper.getCapacity() * helper.getVelocity())/ helper.getEnergyCost();
			time += helper.getCapacity() * helper.getVelocity();
		}
		
		Structure offer = new Structure("attributes");
		offer.addTerm(new NumberTermImpl(score));
		offer.addTerm(new NumberTermImpl(time));
		
		return offer;
	}
}