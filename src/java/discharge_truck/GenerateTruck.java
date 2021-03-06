package discharge_truck;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import discharge_truck.FactoryModel;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class GenerateTruck extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	FactoryModel model;
	 
	 public Queue<Integer> truckCargo = new LinkedList<Integer>();
	 public Queue<String> truckCargoDrop = new LinkedList<String>();
	 public Queue<String> boxes = new LinkedList<String>();
	 public int qtdTruck = 0;
	    
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	
	   
	  truckCargo = new LinkedList<Integer>();
	  truckCargoDrop = new LinkedList<String>();
	  boxes = new LinkedList<String>();
	  qtdTruck = 0;
	   
	Random generatorCharge = new Random();
   	int qtd = 0;
   	while (qtd < 5)
   	{
   		qtd = generatorCharge.nextInt(6);
   	}
   	int aux = 0;
   	String drop = null;
   	int weight = 0;
   	while (aux < qtd)
   	{
   		//The weights and the place to drop is random
   		weight = generatorCharge.nextInt(10);
   		if(generatorCharge.nextInt(100) > 50)
   		{
   			drop = "drop1";
   		}
   		else
   		{
   			drop = "drop2";
   		}
   		boxes.add("box(" + weight + ", "+ drop +")");
   		aux += 1;
   	}	
   	  ts.getAg().delBel(Literal.parseLiteral("truckloadCurrently(_)"));
   	  ts.getAg().delBel(Literal.parseLiteral("truckload(_)"));
   	  ts.getAg().delBel(Literal.parseLiteral("qtdThings(_)"));

      // ts.getAg().delBel(Literal.parseLiteral("qtdTruck(_)"));
       //ts.getAg().addBel(Literal.parseLiteral("boxDelivered(true)"));
      ts.getAg().addBel(Literal.parseLiteral("truckloadCurrently("+ boxes +")"));
      ts.getAg().addBel(Literal.parseLiteral("truckload("+ boxes +")"));
      ts.getAg().addBel(Literal.parseLiteral("qtdThings("+ qtd +")"));
      
      return true;
   } 
}
