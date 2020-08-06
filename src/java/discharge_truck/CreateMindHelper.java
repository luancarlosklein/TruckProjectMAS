package discharge_truck;

import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class CreateMindHelper extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception 
   {  
	   for (Worker w : DischargeEnv.model.getWorld().getWorkerMap().values())
	   {
		   ts.getAg().addBel(Literal.parseLiteral( "plays(initiator," + w.getName() + ")"));   
	   }	
	   
	 return true;
   } 
}
