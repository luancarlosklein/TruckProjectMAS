// Environment code for project unloading_truck

import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import java.util.Random;

import discharge_truck.FactoryModel;
import discharge_truck.FactoryView;

public class FactoryEnv extends Environment {
	public static final Literal ag = Literal.parseLiteral("at(worker,garage)");
	public static final Literal ad1 = Literal.parseLiteral("at(worker,drop1)");
	public static final Literal ad2 = Literal.parseLiteral("at(worker,drop2)");
	public static final Literal at = Literal.parseLiteral("at(worker,truck)");
	public static final Literal asw = Literal.parseLiteral("at(worker, somewhere)");
    FactoryModel model; // the model of the grid
    
    @Override
    public void init(String[] args) {
        model = new FactoryModel();
        if (args.length == 1 && args[0].equals("gui")) { 
        	FactoryView view  = new FactoryView(model);
            model.setView(view);
        }
        updatePercepts();
    }
    
    /** creates the agents percepts based on the HouseModel */
    void updatePercepts() {
    	Random generatorCharge = new Random();
    	clearPercepts("worker");
    	Location lworker = model.getAgPos(0);
    	 // add agent location to its percepts
        if (lworker.equals(model.ltruck)) {
            addPercept("worker", at);
            int weigth = generatorCharge.nextInt(10);
            if(generatorCharge.nextInt(10) > 4)
            {
            	addPercept("worker", Literal.parseLiteral("box("+ weigth +", drop1)"));
            }
            else
            {
            	addPercept("worker", Literal.parseLiteral("box("+ weigth +", drop2)"));
            }
        }
        if (lworker.equals(model.lgarage)) {
            addPercept("worker", ag);
        }
        if (lworker.equals(model.ldrop1)) {
            addPercept("worker", ad1);
        }
        if (lworker.equals(model.ldrop2)) {
            addPercept("worker", ad2);
        }
        else
        {
        	addPercept("worker", asw);
        }
     
    }
    
    @Override
    public boolean executeAction(String ag, Structure action) {
        System.out.println("["+ag+"] doing: "+action);
        boolean result = false;
        if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
            
            if (l.equals("drop1")) {
                dest = model.ldrop1;
            } else if (l.equals("truck")) {
                dest = model.ltruck;
            }else if (l.equals("garage")) {
                dest = model.lgarage;
            } else if (l.equals("drop2")) {
                dest = model.ldrop2;
            }     
            result = model.moveTowards(dest);
        }
        if (result) {
            updatePercepts();
            try { Thread.sleep(40); } catch (Exception e) {}
        }
        return result;
    }
}
