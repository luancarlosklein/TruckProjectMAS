package actions.worker;

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

/**
 * A Worker calls this action to generate an offer.
 * The Worker send his offer to a Trucker.
 */
public class proposeOffer extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();

	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: teamId
	 * @param args[1]: worker's name
	 * @return args[2]: an offer
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception
    {
		// Getting the input parameters
		NumberTerm teamId = (NumberTerm) args[0]; 
		Worker worker = workerMap.get(Integer.parseInt(args[1].toString().split("_")[1]));
	
		// Returning an offer
		return un.unifies(generateOffer(worker.getTeamMembers((int) teamId.solve())), args[2]);
    }
	
	/**
	 * Compute the offer based on the attributes of team members
	 * @param teamMembers: member of a team
	 * @return an offer.
	 */
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