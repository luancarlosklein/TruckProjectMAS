package discharge_truck;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class CalculateImage extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
   @SuppressWarnings({ "unchecked", "rawtypes" })
@Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   
	   //////////////////////////////////////////////
	   //Define the workers quantity 
	   //CHANGE THIS WHEN THE NUMBER WORKS CHANGE
	   int qtdWorkers = 6;
	   //////////////////////////////////////////////
	   
	   //The workers
	   String [] workes = new String[qtdWorkers];
	  //The reputations and confiability
	   
	   double [] reputations1 = new double[qtdWorkers];
	   double [] reputations2 = new double[qtdWorkers];
	   double [] reputationsConf1 = new double[qtdWorkers];
	   double [] reputationsConf2 = new double[qtdWorkers];
	   
	   //The impressions about worker1 are in the position 0...
	   List<List> allImpressions = new ArrayList<List>();
	   
	   int hourStart = 0;
	   
	   //Get the current time
	   Calendar data = Calendar.getInstance();
	   int hora = data.get(Calendar.HOUR_OF_DAY); 
	   int min = data.get(Calendar.MINUTE);
	   int seg = data.get(Calendar.SECOND);   
	   //Transform to seconds
	   int totalTime = hora*3600 + min*60 + seg - hourStart;
		 
	   //The ITM (value to calc the confiability)
	   int itm = 27;
	   
	   //Variable aux used in the code
	   String lis;
	   String [] values;
	   double val1 = 0;
	   double val2 = 0;
	   int timei = 0;
		
	   ////////////////////////////////////////////////
	   
	   //Initiate the variables
	   for (int i = 0; i < qtdWorkers; i++)
	   {
		   workes[i] = "worker" + (i+1);
		   reputations1[i] = 0;
		   reputations2[i] = 0;
		   reputationsConf1[i] = 0;
		   reputationsConf2[i] = 0;
		   allImpressions.add(new ArrayList<Term>());
		   
		   
	   }
	   
	   
	   //Get the information in the BB
	   for (Literal b: ts.getAg().getBB()) {
			   if (b.getFunctor().toString().equals("impression")) {
				   if(b.getTerm(0).toString().equals("self"))
				   {
					   int pos = Integer.parseInt(b.getTerm(1).toString().substring(6, b.getTerm(1).toString().length())) - 1;
					   allImpressions.get(pos).add(b);				   
			        }
			   }
			   //Get the initial hour
			   else if (b.getFunctor().toString().equals("hourStart")) 
			   {
				   hourStart = Integer.parseInt(b.getTerm(0).toString()); 
			   }	   
		   }
	   
	   //Calc the grade one per one
	  for (int j = 0; j < allImpressions.size(); j++)
	  {
		  if (allImpressions.get(j).size() > 0)
		  {
			  double totaliW0 = addAll(totalTime, allImpressions.get(j));
			  for (int i = 0; i < allImpressions.get(j).size(); i++)
			  {
				 //Get the list of grades
			
				  lis = ((Literal) allImpressions.get(j).get(i)).getTerm(4).toString();
				  lis = lis.substring(1,lis.length()-1);
				  values = lis.split(",");
				  
				  //Set the two values
				  val1 = Double.parseDouble(values[0]);
				  val2 = Double.parseDouble(values[1]);
				  
				  //Get the time
				  timei = Integer.parseInt(((Literal) allImpressions.get(j).get(i)).getTerm(2).toString());
				
				  //Put the grade in the impression for the right worker
				  reputations1[j] += val1 * f(timei, totalTime) / totaliW0;
				  reputations2[j] += val2 * f(timei, totalTime) / totaliW0;
			  }
			  val1 =  rl(itm, totalTime, allImpressions.get(j), reputations1[j], 0);
			  val2 =  rl(itm, totalTime, allImpressions.get(j), reputations2[j], 1);
			  reputationsConf1[j] = val1;
			  reputationsConf2[j] = val2;
		 }
		 else
		 {
			 reputations1[j] = 1;
			 reputations2[j] = 1;
			 reputationsConf1[j] = 1;
			 reputationsConf2[j] = 1;
		 }
	 }
	  
	 //Restart the variables
	 for (int i = 0; i < allImpressions.size(); i++)
	 {
		  ts.getAg().delBel(Literal.parseLiteral("image(worker" + (i+1) +  ",_,_)"));
		  ts.getAg().delBel(Literal.parseLiteral("imageLevel(worker" + (i+1) +  ",_,_)"));
		  ts.getAg().addBel(Literal.parseLiteral("image(" + workes[i] + "," + reputations1[i] + "," + reputations2[i] + ")"));
		  ts.getAg().addBel(Literal.parseLiteral("imageLevel(" + workes[i] + "," + reputationsConf1[i] + "," + reputationsConf2[i] + ")"));
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
   
   

   public double ni(int itm, int qtdImpre)
   {
	   if (qtdImpre >= itm)
	   {
		   return 1;
	   }
	   else
	   {
		   return Math.sin(Math.PI/(2*itm) * qtdImpre);
	   }
   }
   
   public double dt (int time, List <Term> impressions, double reput, int index)
   {
	   double dtvalue = 0;
	   double sum = 0;
	   String lis;
	   String [] values;
	   double val = 0;
	   int timei = 0;
	   
	   double totali = addAll(time, impressions);
	   for (int i = 0; i < impressions.size(); i++)
	   {
			 //Get the list of grades
			  lis = ((Literal) impressions.get(i)).getTerm(4).toString();
			  lis = lis.substring(1,lis.length()-1);
			  values = lis.split(",");
			  
			  //Set the two values
			  val = Double.parseDouble(values[index]);
			  //val2 = Double.parseDouble(values[1]);
			  
			  //Get the time
			  timei = Integer.parseInt(((Literal) impressions.get(i)).getTerm(2).toString());
			
			  //Put the grade in the impression for the right worker
			  sum += Math.abs(val - reput) *  f(timei, time) / totali;
		  }
	   
	   dtvalue = 1 - sum;
	   return dtvalue;
   }
   
   
   public double rl(int itm, int time, List <Term> impressions, double reput, int index)
   {
	   
	   double niV = ni(itm, impressions.size());
	   double dtV = dt(time, impressions, reput, index);
	   double mi = 0;
	   String lis;
	   String [] values;
	   double val = 0;
	   
	   for (int i = 0; i < impressions.size(); i++)
	   {
			 //Get the list of grades
			  lis = ((Literal) impressions.get(i)).getTerm(4).toString();
			  lis = lis.substring(1,lis.length()-1);
			  values = lis.split(",");
			  
			  //Set the two values
			  val += Double.parseDouble(values[index]);	
	   }
	   
	  // System.out.println("SAIDAAAAAAAAA--> niV: "+ niV);
	  // System.out.println("SAIDAAAAAAAAA--> dtV: "+ dtV);
	   
	   mi = val/impressions.size();
	  // System.out.println("SAIDAAAAAAAAA--> mi: "+ mi);
	   return (1-mi)*niV + mi*dtV;
	   
   }
}
