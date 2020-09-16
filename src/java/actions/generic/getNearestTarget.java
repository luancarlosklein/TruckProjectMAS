// Internal action code for project discharge_truck

package actions.generic;

import entities.model.SimpleElement;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;

/**
 * This action finds the nearest target from a given agent, considering a list of possible targets.
 */
public class getNearestTarget extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: list of targets
	 * @param args[1]: position x of agent
	 * @param args[2]: position y of agent
	 * @return args[3]: the nearest target from agent
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
	{   
		ListTerm artifacts = (ListTerm) args[0];
		NumberTerm x = (NumberTerm) args[1];
		NumberTerm y = (NumberTerm) args[2];
		SimpleElement bestOption = null;
		double minDist = Double.MAX_VALUE;	
		
		for(Term artifact : artifacts)
		{
			Structure elementName = (Structure) artifact;
			SimpleElement element = DischargeEnv.model.getElement(Integer.parseInt(elementName.getTerm(0).toString().split("_")[1]));
			double dist = Math.abs(x.solve() - element.getPos().x) + Math.abs(y.solve() - element.getPos().y);
			
			if(dist < minDist)
			{
				minDist = dist;
				bestOption = element;
			}
		}
		
		if(bestOption == null)
		{
			throw new Error("The map is incomplete, some element is missing.");
		}
		return un.unifies(Literal.parseLiteral(bestOption.getName()), args[3]);	
    }
}