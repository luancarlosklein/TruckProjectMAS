// Internal action code for project discharge_truck

package actions.worker;

import java.util.Set;
import java.util.TreeSet;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

/**
 * This action merges two lists into a list without repetition
 */
public class concatToSet extends DefaultInternalAction {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: list1
	 * @param args[1]: list2
	 * @return args[2] (concatenated list)
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	ListTerm list1 = (ListTerm) args[0];
    	ListTerm list2 = (ListTerm) args[1];
    	ListTerm list3 = new ListTermImpl();
    	
    	Set<String> merge = new TreeSet<String>();
    	
    	for(Term t : list1)
    	{
    		merge.add(t.toString());
    	}
    	
    	for(Term t : list2)
    	{
    		merge.add(t.toString());
    	}
    	
    	for(String s : merge)
    	{
    		list3.add(Atom.parseLiteral(s));
    	}
    	return un.unifies(list3, args[2]);
    }
}