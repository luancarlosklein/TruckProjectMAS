package discharge_truck;

import java.util.Random;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class CreateMindWorker extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception 
   {
	   Random generatorCharge = new Random();
	   int capacity = generatorCharge.nextInt(10);
	 
	   ts.getAg().addBel(Literal.parseLiteral( "capacity(" + capacity + ")"));
		   
	 return true;
   } 
}
