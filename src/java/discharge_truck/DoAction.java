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
	   String dropGo = null;
	   String truckGo = null;
	   
	   for (Literal b: ts.getAg().getBB()) {
         //ts.getLogger().info(b.toString());
		   if (b.getFunctor().toString().equals("plan")) {
			   plano = b.getTerms();
		   }
		   else if (b.getFunctor().toString().equals("stepPlan")) {
			   List <Term> st = b.getTerms();
			   step = Integer.parseInt(st.get(0).toString());
		   }
		   
		   else if (b.getFunctor().toString().equals("lengthPlan")) {
			   List <Term> st = b.getTerms();
			   lengthPlan = Integer.parseInt(st.get(0).toString());
		   }
		   
		   else if (b.getFunctor().toString().equals("drop")) {
			   List <Term> st = b.getTerms();
			   dropGo = st.get(0).toString();
		   }
		   
		   else if (b.getFunctor().toString().equals("truck")) {
			   List <Term> st = b.getTerms();
			   truckGo = st.get(0).toString();
		   }
		   
		   
	  }
	  if (lengthPlan <= step)
	   {
		   //System.out.println("AGENTE: " + ts.getAg() + "          ACABOUUUUUUUUUUU O PLANOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO Passo: " + step );
		   ts.getAg().delBel(Literal.parseLiteral("busy(true)"));
		   ts.getAg().delBel(Literal.parseLiteral("plan(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("agenteAjudado(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("havePlan(true)"));
		   ts.getAg().delBel(Literal.parseLiteral("carregar(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("descarregar(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("move(_)"));
		   ts.getAg().delBel(Literal.parseLiteral("havePlan(true)"));
		   ts.getAg().addBel(Literal.parseLiteral("havePlan(false)"));
		   ts.getAg().addBel(Literal.parseLiteral("busy(false)"));
		   ts.getAg().addBel(Literal.parseLiteral("agenteAjudado(none)"));
		   ts.getAg().addBel(Literal.parseLiteral("plan(none)"));
		   return true;   
	   }
	   String [] act = plano.get(step).toString().split("S");
	   int nextStep = step + 1;
	   Literal saida;
	   //System.out.println(act);
	   if (act[0].equals("move"))
		{
		    ts.getAg().delBel(Literal.parseLiteral("move(_)"));
			if ((act[2].equals("drop")))
			{
				
				//System.out.println("ADICIOU PRO DROP AGORAAAAAA--->>>>>>>>>" + "move(" + dropGo +")");
				saida = Literal.parseLiteral("move(" + dropGo +")");
			}
			
			else if ((act[2].equals("truck")))
			{
				
				//System.out.println("ADICIOU PRO Truck AGORAAAAAA--->>>>>>>>>" + "move(" + truckGo +")");
				saida = Literal.parseLiteral("move(" + truckGo +")");
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
