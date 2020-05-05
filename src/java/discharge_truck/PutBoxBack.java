package discharge_truck;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class PutBoxBack extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	public Queue<String> truckCargoDrop = new LinkedList<String>();
	    
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   truckCargoDrop = new LinkedList<String>();
	   String boxPutBack = null;
	   List <Term> st = null;
	   for (Literal b: ts.getAg().getBB()) {
	         //ts.getLogger().info(b.toString());
			   if (b.getFunctor().toString().equals("boxPutBack")) {
				   boxPutBack = "box(" + b.getTerms().get(0).toString() + ", " + b.getTerms().get(1).toString() + ")";
				   System.out.print("YUPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPSSSSSSSSSSSSS");
			   }
			   else if (b.getFunctor().toString().equals("truckloadCurrently")) {
				   st = b.getTerms();
			   }
	   }
	   st = (List<Term>) st.get(0);
	   
	   for (int i = 0; i < st.size(); i++)
	   {
		   truckCargoDrop.add(st.get(i).toString());
	   }
	   truckCargoDrop.add(boxPutBack);	   
	   
	  ts.getAg().delBel(Literal.parseLiteral("boxPutBack(_,_,_)"));
   	  ts.getAg().delBel(Literal.parseLiteral("truckloadCurrently(_)"));
      ts.getAg().addBel(Literal.parseLiteral("truckloadCurrently("+ truckCargoDrop +")"));
      
      
      return true;
   } 
}
