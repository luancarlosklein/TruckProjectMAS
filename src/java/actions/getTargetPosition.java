// Internal action code for project discharge_truck

package actions;

import entities.model.SimpleElement;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

/**
 * Action's arguments (from args parameter):
 * args[0]: target' name
 * args[1]: return (the target' position)
 */
public class getTargetPosition extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	int id = Integer.parseInt(args[0].toString().split("_")[1]);
    	SimpleElement element = DischargeEnv.model.getElement(id);
    	
    	return un.unifies(Literal.parseLiteral("pos("+ element.getPos().x + "," + element.getPos().y + ")"), args[1]);
    }
}