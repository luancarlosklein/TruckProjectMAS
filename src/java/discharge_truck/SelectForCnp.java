package discharge_truck;

import java.util.ArrayList;
import java.util.List;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class SelectForCnp extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   List <Integer> proposes = new ArrayList<Integer>();
	   List <Integer> confiability = new ArrayList<Integer>();
	   List <String> workes = new ArrayList<String>();
	   List <Term> confiabilityT = null;
	   int propose = 0;
	   
	   //Get the confiability level of each worker(by the perception of truck)
	   for (Literal b: ts.getAg().getBB()) {
	     	 if (b.getFunctor().toString().equals("confiability")) {
				   confiabilityT = b.getTerms();
			   } 
		   }
	   //Transform the confiabilyTerms in interegers
	   for (int i = 0; i < confiabilityT.size(); i++)
	   {
		   confiability.add( Integer.parseInt(confiabilityT.get(i).toString()));
	   }
		   
	   //See the proposal (Time and capacity) for each worker
	   for (Literal b: ts.getAg().getBB()) {
			   if (b.getFunctor().toString().equals("proposeA")) {
				   //Get the number of agent, for to get the confiability level
				   int getCo = Integer.parseInt(b.getTerm(3).toString().substring(6, b.getTerm(3).toString().length()));
				   //build the proposal, for a formula: Capacity -0.5*time*(101-confiability)
				   propose = (int) Math.ceil(Integer.parseInt(b.getTerm(1).toString()) + (Integer.parseInt(b.getTerm(2).toString()) * (-0.5) * (101 - confiability.get(getCo))/20 ));   
				   workes.add(b.getTerm(3).toString());		   
				   proposes.add(propose);
			   }
		   }
	   
	   //Select the best propose (maximum value)
	   int win = 0;
	   int posWin = 0;
	   for (int i = 0; i < proposes.size(); i++)
	   {
		   if (proposes.get(i) > win)
		   {
			   posWin = i;
			   win = proposes.get(i);
		   }
	   }
	   
	   //add the winner in the belief base
	   ts.getAg().addBel(Literal.parseLiteral("winnerCnp("+ workes.get(posWin) +","+ win+ ")"));
	 return true;
   } 
}
