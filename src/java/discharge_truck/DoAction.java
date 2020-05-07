package discharge_truck;

import java.util.ArrayList;
import java.util.List;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class DoAction extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	public List<String> plan = new ArrayList<String>();
	public Planner planner = new Planner();
			       
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	     
	   List <Term> plano = null;
	   int step = 0;
	   int lengthPlan = 0;
	   String dropGo = null;
	   String truckGo = null;
	   
	   //Get the relevant informations in the beliefBase
	   for (Literal b: ts.getAg().getBB()) {
		   //Get the plan
		   if (b.getFunctor().toString().equals("plan")) {
			   plano = b.getTerms();
		   }
		   //Get the step of the plan
		   else if (b.getFunctor().toString().equals("stepPlan")) {
			   List <Term> st = b.getTerms();
			   step = Integer.parseInt(st.get(0).toString());
		   }
		   //Get the size of the plann
		   else if (b.getFunctor().toString().equals("lengthPlan")) {
			   List <Term> st = b.getTerms();
			   lengthPlan = Integer.parseInt(st.get(0).toString());
		   }
		   //Get the drop to delivery
		   else if (b.getFunctor().toString().equals("drop")) {
			   List <Term> st = b.getTerms();
			   dropGo = st.get(0).toString();
		   }
		   //Get the truck to get the box 
		   else if (b.getFunctor().toString().equals("truck")) {
			   List <Term> st = b.getTerms();
			   truckGo = st.get(0).toString();
		   }
	  }
	   //Check if the plan is finished
	  if (lengthPlan <= step)
	   {
		  //If the end is true, so restart the belief base
		  //Delete the old beliefs
		   ts.getAg().delBel(Literal.parseLiteral("busy(true)"));
		   ts.getAg().delBel(Literal.parseLiteral("plan(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("agenteAjudado(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("havePlan(true)"));
		   ts.getAg().delBel(Literal.parseLiteral("carregar(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("descarregar(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("move(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("havePlan(true)"));
		   //Add the new beliefs
		   ts.getAg().addBel(Literal.parseLiteral("havePlan(false)"));
		   ts.getAg().addBel(Literal.parseLiteral("busy(false)"));
		   ts.getAg().addBel(Literal.parseLiteral("agenteAjudado(none)"));
		   ts.getAg().addBel(Literal.parseLiteral("plan(none)"));
		   return true;   
	   }
	   //Get the next action of the plan. 
	  //In the step, has a separator S (Space) to separate the places. Break this with split
	   String [] act = plano.get(step).toString().split("S");
	   //Set the next step
	   int nextStep = step + 1;
	   Literal saida;
	   
	   //If the action action is to move
	   if (act[0].equals("move"))
		{
		    ts.getAg().delBel(Literal.parseLiteral("move(_)"));
			//If it is going to the drop
		    if ((act[2].equals("drop")))
			{
		    	//Get the drop and put it in a literal
				saida = Literal.parseLiteral("move(" + dropGo +")");
			}
			//If it is going to the truck
			else if ((act[2].equals("truck")))
			{
				//Get the truck and put it in a literal 
				saida = Literal.parseLiteral("move(" + truckGo +")");
			}
			//If the place is another one
			else
			{
				//Get this place and put it in a literal (The act[1] is the current position, and the act[2] is the destiny)
				saida = Literal.parseLiteral("move(" + act[2] +")");
			}
		}
	    //If the action is not to move, like a "descarregar", just put it on a literal with value true
		else
		{
			saida = Literal.parseLiteral(act[0]+"(true)");
		}
	    //Actualize the belief base
	    //Delete the old stepPlan
	    ts.getAg().delBel(Literal.parseLiteral("stepPlan("+ step  +")"));
	    //Add the action
	    ts.getAg().addBel(saida);
	    //Add the current step (nextStep)
	    ts.getAg().addBel(Literal.parseLiteral("stepPlan("+ nextStep  +")"));
        return true;
   }
}
