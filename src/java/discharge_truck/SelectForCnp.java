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
	   
	   
	   
	   //////////////////////////////////////////////
	   int qtdWorkers = 6;
	   //////////////////////////////////////////////
	   
	   List <Double> proposes = new ArrayList<Double>();
	   List <String> workes = new ArrayList<String>();
	   List <Term> imageT = new ArrayList<Term>();
	   List <Term> reputationT = new ArrayList<Term>();
	   List <Term> imageLevelT = new ArrayList<Term>();
	   List <Term> reputationLevelT = new ArrayList<Term>();
	   
	   double [][] image = new double[qtdWorkers][2];
	   double [][] reputation = new double[qtdWorkers][2];
	   double [][] imageLevel = new double[qtdWorkers][2];
	   double [][] reputationLevel = new double[qtdWorkers][2];
	   
	   double [] initial = {0,0};
	   for (int i = 0; i < qtdWorkers; i++)
	   {   
		   image[i] = initial;
		   reputation[i] = initial;
		   imageLevel[i] = initial;
		   reputationLevel[i] = initial;
	   }
	        
	   double weightCapacity = 0;
	   double weightTime = 0;
	   double weightImage = 0;
	   double weightReputation = 0;
	   
	   
	   double propose = 0;
	   
	   //Get the confiability level of each worker(by the perception of truck)
	   for (Literal b: ts.getAg().getBB()) {
	     	 if (b.getFunctor().toString().equals("reputation")) {
	     		 reputationT.add(b);
			   } 
	     	 
	     	 else if (b.getFunctor().toString().equals("image")) {
	     		imageT.add(b);
			   } 
	     	 
	    	 else if (b.getFunctor().toString().equals("reputationLevel")) {
	    		 reputationLevelT.add(b);
			   } 
	     	 
	    	 else if (b.getFunctor().toString().equals("imageLevel")) {
		     	imageLevelT.add(b);
			   } 
	    	 else if (b.getFunctor().toString().equals("valueCapacity")) {
	    		 weightCapacity = Double.parseDouble(b.getTerm(0).toString());
	    	   }
	     	 
	    	 else if (b.getFunctor().toString().equals("valueTime")) {
	    		 weightTime = Double.parseDouble(b.getTerm(0).toString());
	    	   }
	     	
	    	 else if (b.getFunctor().toString().equals("valueReputation")) {
	    		 weightReputation = Double.parseDouble(b.getTerm(0).toString());
	    	   }
	     	
	    	 else if (b.getFunctor().toString().equals("valueImage")) {
	    		 weightImage = Double.parseDouble(b.getTerm(0).toString());
	    	   }
	    	 
	    	 
	     	 
		   }
	   
	   String work;
	   int posI = 0;
	   int posR = 0;
	   //Transform the confiabilyTerms in interegers
	   for (int i = 0; i < reputationT.size(); i++)
	   {
		   
		   work = ((Literal) reputationT.get(i)).getTerm(0).toString();
		   posR = Integer.parseInt(work.substring(6, work.length())) - 1;
		   reputation[posR][0] = Double.parseDouble(((Literal) reputationT.get(i)).getTerm(1).toString());
		   reputation[posR][1] = Double.parseDouble(((Literal) reputationT.get(i)).getTerm(2).toString());
		   
		   reputationLevel[posR][0] = Double.parseDouble(((Literal) reputationLevelT.get(i)).getTerm(1).toString());
		   reputationLevel[posR][1] = Double.parseDouble(((Literal) reputationLevelT.get(i)).getTerm(2).toString());
		   
		  
		   work = ((Literal) imageT.get(i)).getTerm(0).toString();
		   posI = Integer.parseInt(work.substring(6, work.length())) -1;
		   
		   image[posI][0] = Double.parseDouble(((Literal) imageT.get(i)).getTerm(1).toString());
		   image[posI][1] = Double.parseDouble(((Literal) imageT.get(i)).getTerm(2).toString());
		   
		   imageLevel[posI][0] = Double.parseDouble(((Literal) imageLevelT.get(i)).getTerm(1).toString());
		   imageLevel[posI][1] = Double.parseDouble(((Literal) imageLevelT.get(i)).getTerm(2).toString());
		   
		   
		}
		   
	   //See the proposal (Time and capacity) for each worker
	   for (Literal b: ts.getAg().getBB()) {
			   if (b.getFunctor().toString().equals("proposeA")) {
				   //Get the number of agent, for to get the confiability level
				   int getCo = Integer.parseInt(b.getTerm(3).toString().substring(6, b.getTerm(3).toString().length())) - 1;
				   //build the proposal, for a formula: Capacity -0.5*time*(101-confiability)
				   //propose = (int) Math.ceil(Integer.parseInt(b.getTerm(1).toString()) + (Integer.parseInt(b.getTerm(2).toString()) * (-0.5)));   
				     
				   
				   //Formula propose:
				   //ValueReputation*(ReputationTime*ConfiRT + ReputationCapa*ConfiRC) +
				   //ValueImage*(imageTime*ConfiIT + imageCapa*ConfiIT)
				   propose = weightReputation*(reputation[getCo][0]*weightTime + reputation[getCo][1]*weightCapacity) + weightImage*(weightTime*image[getCo][0] + weightCapacity*image[getCo][1]);
				   
				   //System.out.println("PROPOSEEEE---> " + propose);
				   
				   //propose = reputation[getCo][0]*reputationLevel[getCo][0];
				   ///THIS IS THE REPUTATION/IMAGE TO USE IN THE FORMULA
				   //System.out.println("Reput00000:---> " + reputation[getCo][0]);
				   //System.out.println("Reput11111:---> " + image[getCo][1]);
				   //System.out.println("IMAGE00000:---> " + reputation[getCo][0]);
				   //System.out.println("IMAGE11111:---> " + image[getCo][1]);

				   
				   //System.out.println("Reput00000LEVELL:---> " + reputationLevel[getCo][0]);
				   //System.out.println("Reput11111LEVELL:---> " + imageLevel[getCo][1]);
				   //System.out.println("IMAGE00000LEVELL:---> " + reputationLevel[getCo][0]);
				   //System.out.println("IMAGE11111LEVELL:---> " + imageLevel[getCo][1]);
				   //reputation[getCo][0];
				  //reputation[getCo][1];
				  //image[getCo][0];
				  //image[getCo][1];
				   
				   workes.add(b.getTerm(3).toString());		   
				   proposes.add(propose);
			   }			
		   }
	   	 

	   //Select the best propose (maximum value)
	   double win = 0;
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
