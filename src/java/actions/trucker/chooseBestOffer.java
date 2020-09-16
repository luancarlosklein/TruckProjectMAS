// Internal action code for project discharge_truck

package actions.trucker;

import java.util.Map;

import entities.model.Truck;
import environments.DischargeEnv;
import jason.NoValueException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;

/**
 * A trucker uses this action to choose the best offer among the received offers.
 */
public class chooseBestOffer extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Truck> truckerMap = DischargeEnv.model.getWorld().getTruckMap();

	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: agent's name
	 * @param args[1]: list of offers
	 * @return args[2]: name of worker who sent the best offer.
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
	{
		Truck t = truckerMap.get(Integer.parseInt(args[0].toString().split("_")[1]));		
		return un.unifies(getBestOffer((ListTerm) args[1], t), args[2]);
    }
	
	/**
	 * Compute the best offer;
	 * @param offers: list of received offers.
	 * @param truck: a truck 
	 */
	private Atom getBestOffer(ListTerm offers, Truck truck) throws NoValueException
	{
		Atom bestWorker = null;
		double bestScore = 0;
		@SuppressWarnings("unused")
		long bestTime = 0;
		
		for(Term offer : offers)
		{
			Structure s = (Structure) offer;
			Structure attributes  = (Structure) s.getTerm(0);
			Atom workerName = (Atom) s.getTerm(1);
			NumberTerm score = (NumberTerm) attributes.getTerm(0);
			NumberTerm time = (NumberTerm) attributes.getTerm(1);
			
			if(bestWorker == null)
			{
				bestWorker = workerName;
				bestScore = score.solve();
				bestTime = (long) time.solve();
			}
			else
			{
				if(bestScore < (double) score.solve())
				{
					bestScore = (double) score.solve();
					bestTime = (long) time.solve();
					bestWorker = workerName;
				}
			}
		}
		return bestWorker;
	}
}