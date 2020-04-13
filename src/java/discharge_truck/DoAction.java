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
	   
	   // System.out.print(planStr);
	   
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
	  if (lengthPlan <= step)
	   {
		  System.out.print("Passo: " + step);
		   
		   ts.getAg().delBel(Literal.parseLiteral("havePlan(true)"));
		   ts.getAg().delBel(Literal.parseLiteral("carregar(X)"));
		   ts.getAg().delBel(Literal.parseLiteral("descarregar(X)"));
		   ts.getAg().delBel(Literal.parseLiteral("move(X)"));
		   ts.getAg().delBel(Literal.parseLiteral("havePlan(true)"));
		   ts.getAg().addBel(Literal.parseLiteral("havePlan(false)"));
		   return true;   
	   }
	   String [] act = plano.get(step).toString().split("S");
	   int nextStep = step + 1;
	   Literal saida;
	   //System.out.println(act);
	   if (act[0].equals("move"))
		{
		    ts.getAg().delBel(Literal.parseLiteral("move(X)"));
			if ((act[2].equals("drop")))
			{
				
				System.out.println("ADICIOU PRO DROP AGORAAAAAA--->>>>>>>>>" + "move(" + act[2] +"1)");
				saida = Literal.parseLiteral("move(" + act[2] +"1)");
			}
			else
			{
				saida = Literal.parseLiteral("move(" + act[2] +")");
			}
		}

		else
		{
			saida = Literal.parseLiteral(act[0]+"(true)");
		}
	   ts.getAg().delBel(Literal.parseLiteral("stepPlan("+ step  +")"));
	   ts.getAg().addBel(saida);
	   ts.getAg().addBel(Literal.parseLiteral("stepPlan("+ nextStep  +")"));
	     
	   
	   
      return true;
   }
}
