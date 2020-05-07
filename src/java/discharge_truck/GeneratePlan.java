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
	public List <Term> plano = null;
			       
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   //Generate the plan using the online planner
	   plan = planner.sendingPostRequest();
	   String planStr = plan.toString();
	   
	   //Configuration to the right form 
	   planStr = planStr.replaceAll("\\(", "");
	   planStr = planStr.replaceAll(", ", ",");
	   planStr = planStr.replaceAll("\\)", "");
	   planStr = planStr.replaceAll(" ", "S");
	   
	   int step = 0;
	   int lengthPlan = 0;
	   
	   //Get the currenty informations about the plan (now)
	   for (Literal b: ts.getAg().getBB()) {
		   //Get the plan
		   if (b.getFunctor().toString().equals("plan")) {
			   plano = b.getTerms();
		   }
		   //Get the step of the plan
		   if (b.getFunctor().toString().equals("stepPlan")) {
			   List <Term> st = b.getTerms();
			   step = Integer.parseInt(st.get(0).toString());
		   }
		   //Get the size of the plan (how many steps do it have?
		   if (b.getFunctor().toString().equals("lengthPlan")) {
			   List <Term> st = b.getTerms();
			   lengthPlan = Integer.parseInt(st.get(0).toString());
		   }
         }
	   int tam = planStr.split(",").length;
	   //Delete the currenty information about the plan
	   ts.getAg().delBel(Literal.parseLiteral("plan(_)"));
	   ts.getAg().delBel(Literal.parseLiteral("lengthPlan(" + lengthPlan +")"));
	   ts.getAg().delBel(Literal.parseLiteral("havePlan(false)"));
	   ts.getAg().delBel(Literal.parseLiteral("stepPlan("+ step +")"));
	   
	   //Put the new plan, and the new informations
	   ts.getAg().addBel(Literal.parseLiteral("havePlan(true)"));
	   ts.getAg().addBel(Literal.parseLiteral("stepPlan("+ 0 +")"));
	   ts.getAg().addBel(Literal.parseLiteral("plan("+ planStr.substring(1, planStr.length() - 1) +")"));	   
	   ts.getAg().addBel(Literal.parseLiteral("lengthPlan(" + tam +")"));   
      return true;
   }
}
