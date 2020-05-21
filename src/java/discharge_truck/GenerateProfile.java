package discharge_truck;


import java.util.Random;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class GenerateProfile extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	 
	   
	  Random rdm = new Random();
	  double image = rdm.nextInt(10)/10.0;
	  System.out.println("ISOOOOOOOOOOOOOOOO--> " + image);
	  double reputation = 1 - image;
	  double capacity = rdm.nextInt(10)/10.0;
	  
	  double time = 1 - capacity;
	  
	  
	  ts.getAg().delBel(Literal.parseLiteral("valueCapacity(_)"));
	  ts.getAg().delBel(Literal.parseLiteral("valueTime(_)"));
	  ts.getAg().delBel(Literal.parseLiteral("valueReputation(_)"));
	  ts.getAg().delBel(Literal.parseLiteral("valueImage(_)"));		
	  
	  
	  ts.getAg().addBel(Literal.parseLiteral("valueCapacity("+ capacity +")"));
	  ts.getAg().addBel(Literal.parseLiteral("valueTime("+ time +")"));
	  ts.getAg().addBel(Literal.parseLiteral("valueReputation("+ reputation +")"));
	  ts.getAg().addBel(Literal.parseLiteral("valueImage("+ image +")"));		  
	 return true;
   } 
}
