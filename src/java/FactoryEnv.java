// Environment code for project unloading_truck

import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import discharge_truck.FactoryModel;
import discharge_truck.FactoryView;



public class FactoryEnv extends Environment {
	//The location percepts 
	public static final Literal ag = Literal.parseLiteral("at(garage)");
	public static final Literal ad1 = Literal.parseLiteral("at(drop1)");
	public static final Literal ad2 = Literal.parseLiteral("at(drop2)");
	public static final Literal at = Literal.parseLiteral("at(truck)");
	public static final Literal at2 = Literal.parseLiteral("at(truck2)");
	public static final Literal at3 = Literal.parseLiteral("at(truck3)");
	public static final Literal asw = Literal.parseLiteral("at(somewhere)");
	
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
    	Location lworker;
    	int place;
    	int agent = 0;
    	int weigth;
    	String drop;
    	//Atualize the percepts for all agents
    	while (agent < FactoryModel.qtdAgents)
    	{
    		clearPercepts("worker" + agent);
    		lworker = model.getAgPos(agent);
    		place = 0;
    		 // add agent location to its percepts
    		//Check if the agent is in one of the principal places, and add to it the information about this place
            if (lworker.equals(model.ltruck)) {
                addPercept("worker" + agent, at);
               
                
                //If the truck is empty, generate a new
                if(model.qtdTruck == 0)
                {
                	model.qtdTruck = model.generateNewTruck(model.truckCargo, model.truckCargoDrop);
                }
                
                //Get the box
                weigth = model.truckCargo.remove();
                drop = model.truckCargoDrop.remove();
                model.qtdTruck -= 1;
                
                //Informes to agent the informations about the box
                addPercept("worker" + agent, Literal.parseLiteral("box("+ weigth + "," + drop + ")"));
                addPercept("worker" + agent, Literal.parseLiteral("qtdTruck("+ model.qtdTruck +")"));
                
                place = 1;   
            } 
            if (lworker.equals(model.lgarage)) {
                addPercept("worker" + agent, ag);
                place = 1;
            }
            if (lworker.equals(model.ldrop1)) {
                addPercept("worker" + agent, ad1);
                place = 1;
            }
            if (lworker.equals(model.ldrop2)) {
                addPercept("worker" + agent, ad2);
                place = 1;
            }
            if (place == 0)
            {
            	addPercept("worker" + agent, asw);
            }
            agent += 1;
    	}
    	
    	if (model.stepPlan < model.plan.size())
    	{
    		String acao = model.plan.get(model.stepPlan);
    		acao = acao.substring(1, acao.length() - 1);
    		String[] act = acao.split(" ");
    		Literal saida;
    		boolean i = false;
    		if (act[0].equals("move"))
    		{
    			if ((act[2].equals("drop")))
    			{
    				i = true;
    				System.out.println("ADICIOU PRO DROP AGORAAAAAA--->>>>>>>>>" + "move(" + act[2] +"1)");
    				saida = Literal.parseLiteral("move(" + act[2] +"1)");
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
    		addPercept("worker11",saida);
    		addPercept("worker11",Literal.parseLiteral("step(" + model.stepPlan +")"));
    		model.plan.set(model.stepPlan, "done");
    	}
    }
    
    @Override
    public boolean executeAction(String ag, Structure action) {
        //System.out.println("["+ag+"] doing: "+action);
        boolean result = false;
        if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            int code = Integer.parseInt(action.getTerm(1).toString());
            Location dest = null;
            
            if (l.equals("drop1")) {
                dest = model.ldrop1;
            } else if (l.equals("truck")) {
                dest = model.ltruck;
            } else if (l.equals("truck2")) {
                dest = model.ltruck2;
            } else if (l.equals("truck3")) {
                dest = model.ltruck3;
            }else if (l.equals("garage")) {
                dest = model.lgarage;
            } else if (l.equals("drop2")) {
                dest = model.ldrop2;
            }     
            result = model.moveTowards(dest, code);
        }
        else if(action.getFunctor().equals("generatePlan"))
        {
        	int id = Integer.parseInt(action.getTerm(0).toString());
        	try {
				result = model.generatePlan(id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        else if(action.getFunctor().equals("nextStep"))
        {
        	int id = Integer.parseInt(action.getTerm(0).toString());
        	try {
				result = model.nextStep(id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        	
        if (result) {
            updatePercepts();
            try { Thread.sleep(350); } catch (Exception e) {}
        }
        return result;
    }
}
