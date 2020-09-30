// Internal action code for project discharge_truck

package actions.generic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

/**
 * This action remove the beliefs of an agent that are associated to a task by CNPId.
 */
public class clearMemory extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: CNPId
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	NumberTerm cnpid = (NumberTerm) args[0];
    	List<Literal> beliefs = new ArrayList<Literal>();
    	
    	// Getting every beliefs associated with the cnpid
    	Iterator<Literal> i = ts.getAg().getBB().iterator();
    	while(i.hasNext())
    	{
    		Literal belief = i.next();
    		
    		if(belief.getTerms().contains(cnpid))
    			beliefs.add(belief);
    	}   	
    	// Removing every beliefs associated with cnpid 
    	for(Literal l : beliefs)
    		ts.getAg().delBel(l);
    	
        return true;
    }
}