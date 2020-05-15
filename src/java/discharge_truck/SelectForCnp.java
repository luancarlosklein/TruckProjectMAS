package discharge_truck;

import java.util.ArrayList;
import java.util.List;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class SelectForCnp extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   List <Integer> proposes = new ArrayList<Integer>();
	   //List <Double> image = new ArrayList<Double>();
	   //List <Double> reputation = new ArrayList<Double>();
	   List <String> workes = new ArrayList<String>();
	   List <Term> imageT = new ArrayList<Term>();
	   List <Term> reputationT = new ArrayList<Term>();
	   
	   
	   double [][] image = { {0,0},{0,0},{0,0}  };
	   double [][] reputation = { {0,0},{0,0},{0,0}  };
	   
	   
	   int propose = 0;
	   
	   //Get the confiability level of each worker(by the perception of truck)
	   for (Literal b: ts.getAg().getBB()) {
	     	 if (b.getFunctor().toString().equals("reputation")) {
	     		 reputationT.add(b);
			   } 
	     	 else if (b.getFunctor().toString().equals("image")) {
	     		imageT.add(b);
			   } 
		   }
	   
	   String work;
	   int posI = 0;
	   int posR = 0;
	   //Transform the confiabilyTerms in interegers
	   for (int i = 0; i < reputationT.size(); i++)
	   {
		   
		   work = ((Literal) reputationT.get(i)).getTerm(0).toString();
		   posR = Integer.parseInt(work.substring(6, work.length()));
		   reputation[posR][0] = Double.parseDouble(((Literal) reputationT.get(i)).getTerm(1).toString());
		   reputation[posR][1] = Double.parseDouble(((Literal) reputationT.get(i)).getTerm(2).toString());
		  
		   work = ((Literal) imageT.get(i)).getTerm(0).toString();
		   posI = Integer.parseInt(work.substring(6, work.length()));
		   
		   reputation[posI][0] = Double.parseDouble(((Literal) imageT.get(i)).getTerm(1).toString());
		   reputation[posI][1] = Double.parseDouble(((Literal) imageT.get(i)).getTerm(2).toString());	 
		}
		   
	   //See the proposal (Time and capacity) for each worker
	   for (Literal b: ts.getAg().getBB()) {
			   if (b.getFunctor().toString().equals("proposeA")) {
				   //Get the number of agent, for to get the confiability level
				   int getCo = Integer.parseInt(b.getTerm(3).toString().substring(6, b.getTerm(3).toString().length()));
				   //build the proposal, for a formula: Capacity -0.5*time*(101-confiability)
				   propose = (int) Math.ceil(Integer.parseInt(b.getTerm(1).toString()) + (Integer.parseInt(b.getTerm(2).toString()) * (-0.5)));   
				     
				   ///THIS IS THE REPUTATION/IMAGE TO USE IN THE FORMULA
				   System.out.println("Reput00000:---> " + reputation[getCo][0]);
				   System.out.println("Reput11111:---> " + image[getCo][1]);
				   System.out.println("IMAGE00000:---> " + reputation[getCo][0]);
				   System.out.println("IMAGE11111:---> " + image[getCo][1]);
				  //reputation[getCo][0];
				  //reputation[getCo][1];
				  //image[getCo][0];
				  //image[getCo][1];
				   
				   workes.add(b.getTerm(3).toString());		   
				   proposes.add(propose);
			   }			
		   }
	   	 

	   //Select the best propose (maximum value)
	   int win = 0;
	   int posWin = 0;
	   for (int i = 0; i < proposes.size(); i++)
	   {
		   if (proposes.get(i) > win)
		   {
			   posWin = i;
			   win = proposes.get(i);
		   }
	   }
	   
	   //add the winner in the belief base
	   ts.getAg().addBel(Literal.parseLiteral("winnerCnp("+ workes.get(posWin) +","+ win+ ")"));
	 return true;
   } 
}
