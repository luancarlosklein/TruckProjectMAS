package discharge_truck;

import java.util.Random;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class CreateMindWorker extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   
	   
	   int id = 0;
	   Random generatorCharge = new Random();
	   int capacity = generatorCharge.nextInt(10);
	   //Get all impressions
	   for (Literal b: ts.getAg().getBB()) {			   
			   if (b.getFunctor().toString().equals("myName")) {
				   //allImpressions.add(b);
				   id = Integer.parseInt(b.getTerm(0).toString().substring(6, b.getTerm(0).toString().length()));
			   }
		   }
	   id = id - 1;
	   ts.getAg().addBel(Literal.parseLiteral( "id(" + id + ")"));
	   ts.getAg().addBel(Literal.parseLiteral( "capacity(" + capacity + ")"));
		   
	 return true;
   } 
}
