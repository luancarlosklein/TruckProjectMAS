package discharge_truck;

import discharge_truck.FactoryModel;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class GetBoxTruck extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	FactoryModel model;
	 
			       
   @Override
   public Object execute(TransitionSystem ts, 
                         Unifier un, 
                         Term[] args) throws Exception {
	   
	   model = new FactoryModel();
	   //If the truck is empty, generate a new
       if(model.qtdTruck == 0)
       {
       	model.qtdTruck = model.generateNewTruck(model.truckCargo, model.truckCargoDrop);
       }
       
       //Get the box
       int weigth = model.truckCargo.remove();
       String drop = model.truckCargoDrop.remove();
       model.qtdTruck -= 1;
       
       ts.getAg().delBel(Literal.parseLiteral("box(_,_)"));
       ts.getAg().delBel(Literal.parseLiteral("qtdTruck(_)"));
       ts.getAg().addBel(Literal.parseLiteral("box(" + weigth + "," + drop +")"));
       ts.getAg().addBel(Literal.parseLiteral("qtdTruck("+ model.qtdTruck +")"));
       
      
	   
      return true;
   }
}
