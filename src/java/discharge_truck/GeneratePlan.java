package discharge_truck;

import java.util.ArrayList;
import java.util.List;

import jason.asSemantics.*;
import jason.asSyntax.*;

import discharge_truck.Planner;
public class GeneratePlan extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	public List<String> plan = new ArrayList<String>();
	public Planner planner = new Planner();
			       
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   plan = planner.sendingPostRequest();
	   String planStr = plan.toString();
	   
	   //System.out.print("VOIIII");
	   planStr = planStr.replaceAll("\\(", "");
	   planStr = planStr.replaceAll(", ", ",");
	   planStr = planStr.replaceAll("\\)", "");
	   planStr = planStr.replaceAll(" ", "S");
	   
	   List <Term> plano = null;
	   int step = 0;
	   int lengthPlan = 0;
	   for (Literal b: ts.getAg().getBB()) {
         //ts.getLogger().info(b.toString());
		   if (b.getFunctor().toString().equals("plan")) {
			   plano = b.getTerms();
		}
		   if (b.getFunctor().toString().equals("stepPlan")) {
			   List <Term> st = b.getTerms();
			   step = Integer.parseInt(st.get(0).toString());
		   }
		   
		   if (b.getFunctor().toString().equals("lengthPlan")) {
			   List <Term> st = b.getTerms();
			   lengthPlan = Integer.parseInt(st.get(0).toString());
		   }
         }
	   
	   ts.getAg().delBel(Literal.parseLiteral("plan(_)"));
	   ts.getAg().delBel(Literal.parseLiteral("lengthPlan(" + lengthPlan +")"));
	   ts.getAg().delBel(Literal.parseLiteral("havePlan(false)"));
	   ts.getAg().addBel(Literal.parseLiteral("havePlan(true)"));
	   ts.getAg().delBel(Literal.parseLiteral("stepPlan("+ step +")"));
	   ts.getAg().addBel(Literal.parseLiteral("stepPlan("+ 0 +")"));
	   ts.getAg().addBel(Literal.parseLiteral("plan("+ planStr.substring(1, planStr.length() - 1) +")"));
	   
	   int tam = planStr.split(",").length;
	   //System.out.print(tam);
		   
	   ts.getAg().addBel(Literal.parseLiteral("lengthPlan(" + tam +")"));
	  
	   
      return true;
   }
}
