package discharge_truck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class GenerateProposeWorkerToTruck extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;

@Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	      
	   String truck = null;
	   int myPosX = 0;
	   int myPosY = 0;
	   int type = 0;
	   int dist = 0;
	   int capacity = 0;
	   for (Literal b: ts.getAg().getBB()) {
	           //Get the truck who is call for a proposal
			   if (b.getFunctor().toString().equals("truckCNP")) {
				   truck = b.getTerms().get(0).toString();
			   }
			   else if (b.getFunctor().toString().equals("pos"))
			   {
				   //Get the current position of the agent
				   myPosX = Integer.parseInt(b.getTerms().get(0).toString());
				   myPosY = Integer.parseInt(b.getTerms().get(1).toString());   
			   }
			   else if (b.getFunctor().toString().equals("capacity"))
			   {
				   //Get the capacity
				   capacity = Integer.parseInt(b.getTerms().get(0).toString());
			   }
	   }
	  
	   //Tab2 -> truck1
	   //Tab3 -> truck2
	   //Tab4 -> truck3
	   //Each truck have the owner best path (LRTA*)
	   //Here, select the correct one
	   if (truck.equals("truck1"))
	   {
		   type = 2;
	   }
	   else if(truck.equals("truck2"))
	   {
		   type = 3; 
	   }
	   else if(truck.equals("truck3"))
	   {
		   type = 4;
	   }
	   
	   //Open the file with the correct path
	   try {
 	      FileReader arq = new FileReader("tab" + type + ".txt");
 	      BufferedReader lerArq = new BufferedReader(arq);
 	      String linha = lerArq.readLine(); 
 	      int i = 0;
 	      int stop = 0;
 	      //Browse the file 
 	      while (linha != null && stop == 0) { 
 	        String[] linhaSeparada = linha.split(",");
 	        for (int j = 0; j < linhaSeparada.length; j++) {
 	        	//If the place in the file is the same the current agent position
 	        	//Store the value of this place
 	        	//And stop the iteration
 	        	if (i == myPosY && j == myPosX)
 	        	{
 	        		stop = 1;
 	        		dist = Integer.parseInt(linhaSeparada[j]);
 	        		break;
 	        	}
 	        }
 	        linha = lerArq.readLine();
 	        i++;
 	      }
 	      arq.close();
 	      //If the file did not exist, the matrix is build from to zero
 	    	} catch (IOException e) {
 	    }
	   //Get the distance to the truck
	   int estimateTime = dist;
 	   ts.getAg().delBel(Literal.parseLiteral("myOffer(_,_)"));
   	   ts.getAg().addBel(Literal.parseLiteral("myOffer("+capacity+"," + estimateTime + ")"));
      return true;
   } 
}
