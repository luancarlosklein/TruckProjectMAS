// Internal action code for project discharge_truck

package actions;

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

public class findTheNearest extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: list of artifacts
	 * args[1]: position x of agent
	 * args[2]: position y of agent
	 * args[3]: return (the artifact nearest from agent)
	 */
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
	{   
		ListTerm artifacts = (ListTerm) args[0];
		NumberTerm x = (NumberTerm) args[1];
		NumberTerm y = (NumberTerm) args[2];
		SimpleElement bestOption = null;
		double minDist = Double.MAX_VALUE;	
		
		// Finding the artifact nearest of the agent
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
			throw new Error("The map is incomplete, some element is missing.");
		
		return un.unifies(Literal.parseLiteral(bestOption.getName()), args[3]);	
    }
}
