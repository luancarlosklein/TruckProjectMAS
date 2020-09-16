package actions.helper;

import java.util.Map;
import entities.model.Helper;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;

/**
 * A Helper calls this action to generate an offer.
 * The Helper send his offer to a worker.
 */
public class proposeOffer extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();

	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: Helper's name
	 * @return args[1]: an offer
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception
    {
		Helper h = helperMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
		
		Structure offer = new Structure("attributes");
		offer.addTerm(new NumberTermImpl(h.getCapacity()));
		offer.addTerm(new NumberTermImpl(h.getVelocity()));
		offer.addTerm(new NumberTermImpl(h.getBattery()));
		offer.addTerm(new NumberTermImpl(h.getEnergyCost()));
		offer.addTerm(new NumberTermImpl(h.getDexterity()));
		offer.addTerm(new NumberTermImpl(h.getFailureProb()));
		offer.addTerm(new NumberTermImpl(h.getSafety()));
		
		return un.unifies(offer, args[1]);
    }
}