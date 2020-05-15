package discharge_truck;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class CalculateImage extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   //Initiate the workers
	   String [] workes = new String[3];
	   workes[0] = "worker0";
	   workes[1] = "worker1";
	   workes[2] = "worker2";
	   
	   
	   //Initiate the reputation value
	 //There are two reputations: reputations1-> time, reputations2->capacity
	   double [] reputations1 = new double[3];
	   reputations1[0] = 0;
	   reputations1[1] = 0;
	   reputations1[2] = 0;
	   double [] reputations2 = new double[3];
	   reputations2[0] = 0;
	   reputations2[1] = 0;
	   reputations2[2] = 0;
	   
	   //List of terms
	   List <Term> allImpressions = new ArrayList<Term>();
	   
	   List <Term> impressionsW0 = new ArrayList<Term>();
	   List <Term> impressionsW1 = new ArrayList<Term>();
	   List <Term> impressionsW2 = new ArrayList<Term>();

	   int hourStart = 0;
	   
	   
	   ///
	   
	   //Get the information in the BB
	   for (Literal b: ts.getAg().getBB()) {
			   if (b.getFunctor().toString().equals("impression")) {
				  
				   if(b.getTerm(0).toString().equals("self"))
				   {
					   
					   allImpressions.add(b);
					  
				   	   if (b.getTerm(1).toString().equals("worker0"))
				   	   {
				   		  impressionsW0.add(b);   
				   	   }
				   	   else if (b.getTerm(1).toString().equals("worker1"))
				   	   {
				   		  impressionsW1.add(b);   
				   	   }
				   	   else if (b.getTerm(1).toString().equals("worker2"))
				   	   {
				   		  impressionsW2.add(b);   
				   	   }
				   }  
			   }
			   //Get the initial hour
			   else if (b.getFunctor().toString().equals("hourStart")) 
			   {
				   hourStart = Integer.parseInt(b.getTerm(0).toString()); 
			   }	   
		   }
	   
	  //Get the current time
	  Calendar data = Calendar.getInstance();
	  int hora = data.get(Calendar.HOUR_OF_DAY); 
	  int min = data.get(Calendar.MINUTE);
	  int seg = data.get(Calendar.SECOND);
	   
	  //Transform to seconds
	  int totalTime = hora*3600 + min*60 + seg - hourStart;
	   
	  //Calc the divisor in the formula to calculate the grade
	 // double totali = addAll(totalTime, allImpressions);
	   
	   
	  String lis;
	  String [] values;
	  double val1 = 0;
	  double val2 = 0;
	  int timei = 0;
	
	  
	  if(impressionsW0.size() > 0)
	  {
		  
		  double totaliW0 = addAll(totalTime, impressionsW0);
		  for (int i = 0; i < impressionsW0.size(); i++)
		  {
			 //Get the list of grades
		
			  lis = ((Literal) impressionsW0.get(i)).getTerm(4).toString();
			  lis = lis.substring(1,lis.length()-1);
			  values = lis.split(",");
			  
			  //Set the two values
			  val1 = Double.parseDouble(values[0]);
			  val2 = Double.parseDouble(values[1]);
			  
			  //Get the time
			  timei = Integer.parseInt(((Literal) impressionsW0.get(i)).getTerm(2).toString());
			
			  //Put the grade in the impression for the right worker
			  reputations1[0] += val1 * f(timei, totalTime) / totaliW0;
			  reputations2[0] += val2 * f(timei, totalTime) / totaliW0;
		  }
	  }
	  
	  if(impressionsW1.size() > 0)
	  {
		  
		  
		  double totaliW1 = addAll(totalTime, impressionsW1);
		  for (int i = 0; i < impressionsW1.size(); i++)
		  {
			 //Get the list of grades
			  lis = ((Literal) impressionsW1.get(i)).getTerm(4).toString();
			  lis = lis.substring(1,lis.length()-1);
			  values = lis.split(",");
			  
			  //Set the two values
			  val1 = Double.parseDouble(values[0]);
			  val2 = Double.parseDouble(values[1]);
			  
			  //Get the time
			  timei = Integer.parseInt(((Literal) impressionsW1.get(i)).getTerm(2).toString());
			
			  //Put the grade in the impression for the right worker
			  reputations1[1] += val1 * f(timei, totalTime) / totaliW1;
			  reputations2[1] += val2 * f(timei, totalTime) / totaliW1;
		  }
		  
	  }
	  
	  if(impressionsW2.size() > 0)
	  {	  
		  double totaliW2 = addAll(totalTime, impressionsW2);
		  for (int i = 0; i < impressionsW2.size(); i++)
		  {
			 //Get the list of grades
			  lis = ((Literal) impressionsW2.get(i)).getTerm(4).toString();
			  lis = lis.substring(1,lis.length()-1);
			  values = lis.split(",");
			  
			  //Set the two values
			  val1 = Double.parseDouble(values[0]);
			  val2 = Double.parseDouble(values[1]);
			  
			  //Get the time
			  timei = Integer.parseInt(((Literal) impressionsW2.get(i)).getTerm(2).toString());
			
			  //Put the grade in the impression for the right worker
			  reputations1[2] += val1 * f(timei, totalTime) / totaliW2;
			  reputations2[2] += val2 * f(timei, totalTime) / totaliW2;
		  }
		  
	  }
	   
	  //Delete the old reputations
	  ts.getAg().delBel(Literal.parseLiteral("image(worker0,_,_)"));
	  ts.getAg().delBel(Literal.parseLiteral("image(worker1,_,_)"));
	  ts.getAg().delBel(Literal.parseLiteral("image(worker2,_,_)"));
	  int i = 0;
	  while (i < 3)
	  {
		  ts.getAg().addBel(Literal.parseLiteral("image(" + workes[i] + "," + reputations1[i] + "," + reputations2[i] + ")"));
		  i += 1;
	  }
	  
	 return true;
   } 
   
   //The function f
   public double f(double timei, double time)
   {
	   return timei/time;
   }
   
   //Add all time (using the function f) in the same variable
   public double addAll(int time, List <Term> impre )
   {
	   double cont = 0;
	   for (int i = 0; i < impre.size(); i++)
	   {
		 cont += f(Integer.parseInt(((Literal) impre.get(i)).getTerm(2).toString()) ,time);
	   }
	  
	   return cont;
   }
}
