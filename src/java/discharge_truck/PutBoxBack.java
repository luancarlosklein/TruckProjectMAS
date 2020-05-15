package discharge_truck;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class PutBoxBack extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
	    
   @SuppressWarnings("unchecked")
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   Queue<String> truckCargoDrop = new LinkedList<String>();
	   String boxPutBack = null;
	   List <Term> st = null;
	   String terms = null;
	   //Get the box it is to put back in the truckload
	   for (Literal b: ts.getAg().getBB()) {
		   	   //Get the box
			   if (b.getFunctor().toString().equals("boxPutBack")) {
				   boxPutBack = "box(" + b.getTerms().get(0).toString() + ", " + b.getTerms().get(1).toString() + ")";
				   //Store the data
				   System.out.println("ENTROU PRA COLOCAR DE VOLTAAAAAAA: -> " +  boxPutBack);
				   terms = b.getTerms().get(0).toString() + ", " + b.getTerms().get(1).toString() + ","+ b.getTerms().get(2).toString();
			   //Get the truckload
			   }
			   else if (b.getFunctor().toString().equals("truckloadCurrently")) {
				   st = b.getTerms();
			   }
	   }
	   st = (List<Term>) st.get(0);
	   
	   //Put all the current truck in another list
	   for (int i = 0; i < st.size(); i++)
	   {
		   truckCargoDrop.add(st.get(i).toString());
	   }
	   System.out.println("CARGA ATUALLLLLLLLLLLLLLLLLLLLLLLLLL: -> " +  truckCargoDrop);
	   
	   //Put the box inside of the same list
	   truckCargoDrop.add(boxPutBack);	   
	   System.out.println("CARGA ANOVAAAAAAAAAAAAAAAAAAAAAAA: -> " +  truckCargoDrop);
	   
	  //Delete this boxPutBack, and the current truckload
	  ts.getAg().delBel(Literal.parseLiteral("boxPutBack("+ terms +")"));
   	  ts.getAg().delBel(Literal.parseLiteral("truckloadCurrently(_)"));
   	  //Add the new truckload
      ts.getAg().addBel(Literal.parseLiteral("truckloadCurrently("+ truckCargoDrop +")"));  
      return true;
   } 
}
