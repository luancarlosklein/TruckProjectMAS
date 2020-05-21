package discharge_truck;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class CreateMindHelper extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   
	  
	   //////////////////////////////////////////////////	  
	   int qtdWorkers = 6;
	   ///////////////////////////////////////
	  
	   int id = 0;
	   for (int i = 1; i <= qtdWorkers; i++)
	   {
		   ts.getAg().addBel(Literal.parseLiteral( "plays(initiator, worker" + i + ")"));   
	   }	
	     
	   
	   for (Literal b: ts.getAg().getBB()) {			   
		   if (b.getFunctor().toString().equals("myName")) {
			   //allImpressions.add(b);
			   id = Integer.parseInt(b.getTerm(0).toString().substring(6, b.getTerm(0).toString().length()));
		   }
	   }
	   id = id - 1 + qtdWorkers;
	   ts.getAg().addBel(Literal.parseLiteral( "id(" + id + ")"));
   
	   
	 return true;
   } 
}
