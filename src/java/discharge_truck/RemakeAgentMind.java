package discharge_truck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class RemakeAgentMind extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   List <Term> allImpressions = new ArrayList<Term>();
	 
	   //Get all impressions
	   for (Literal b: ts.getAg().getBB()) {			   
			   if (b.getFunctor().toString().equals("impression")) {
				   allImpressions.add(b);
			   }
		   }
	   
	   //Delete all impressions in the agent mind
	   for (int i = 0; i < allImpressions.size(); i++)
		  {
			 ts.getAg().delBel(Literal.parseLiteral(allImpressions.get(i).toString()));
		  }
	  
	  //The percent of impressions recovered
	  double indexFoggeten = 0.1;
	  //Define all many impressions to put back
	  int finalSize =  (int)(allImpressions.size() * indexFoggeten);
	  int placeRemove = 0;
	  Random rdm = new Random();
	  
	  //Remove some impressions of the list
	  while (allImpressions.size() >  finalSize)
	  {
		  placeRemove = rdm.nextInt(allImpressions.size() - 1);
		  allImpressions.remove(placeRemove);
	  }
	  	  
	  //Add back the old impressions
	  for (int i = 0; i < allImpressions.size(); i++)
	  {
		 ts.getAg().addBel(Literal.parseLiteral(allImpressions.get(i).toString()));
	     System.out.println(allImpressions.get(i));
	  }
		  
	 return true;
   } 
}
