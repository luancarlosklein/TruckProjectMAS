package discharge_truck;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class CreateMindTruck extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   
	   
	   ////////////////////////////////// 
	   int qtdWorkers = 6;
  	   /////////////////////////////////////////
	   
	   for (int i = 1; i <= qtdWorkers; i++)
	   {

		   ts.getAg().addBel(Literal.parseLiteral( "image(worker" + i + ", 1, 1)"));
		   ts.getAg().addBel(Literal.parseLiteral( "imageLevel(worker" + i + ", 1, 1)"));
		   ts.getAg().addBel(Literal.parseLiteral( "reputation(worker" + i + ", 1, 1)"));
		   ts.getAg().addBel(Literal.parseLiteral( "reputationLevel(worker" + i + ", 1, 1)"));
		      
	   }		  
	 return true;
   } 
}
