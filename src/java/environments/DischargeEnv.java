package environments;

import java.util.Random;

import entities.model.Artifact;
import entities.model.Helper;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import views.WordViewer;
import views.WorldModel;

public class DischargeEnv extends Environment
{
	public static WorldModel model;
	
	@Override
	public void init(String[] args) 
	{
		super.init(args);
		try 
		{	
			model = new WorldModel(new World());
			
			if(args.length == 1 && args[0].equals("gui")) 
			{ 
				WordViewer view  = new WordViewer(model, "Discharge Truck", 700);
				model.setView(view);
			}
			updatePercepts();
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		System.out.println("\n--------------------- STARTING JASON APPLICATION --------------------\n");
		
		startPercepts();
	}
	
	// Setting the initial perceptions
	public void startPercepts()
	{
		Random rand = new Random();
		
		// Start manager
		clearPercepts("manager");	
		
		// Start truckers
		for(Truck t : model.getWorld().getTruckMap().values())
		{
			addPercept("manager", Literal.parseLiteral("add_trucker(" + t.getName() + ")"));
			
			clearPercepts(t.getName());
			addPercept(t.getName(), Literal.parseLiteral("id(" + t.getId() + ")"));			
			addPercept(t.getName(), Literal.parseLiteral("pos(" + t.getPos().x + ", " + t.getPos().y + ")"));
			addPercept(t.getName(), Literal.parseLiteral("qtdThings(" + t.getQtdThings() + ")"));
		}
		
		// Start workers
		for(Worker w : model.getWorld().getWorkerMap().values())
		{
			addPercept("manager", Literal.parseLiteral("add_worker("+ w.getName() +")"));
			
			clearPercepts(w.getName());
			addPercept(w.getName(), Literal.parseLiteral("id(" + w.getId() + ")"));
			addPercept(w.getName(), Literal.parseLiteral("pos(" + w.getPos().x + "," + w.getPos().y +")"));
			addPercept(w.getName(), Literal.parseLiteral("batery(" + w.getBattery() +")"));
			addPercept(w.getName(), Literal.parseLiteral("qtdGoal(" + w.getQtdGoals() +")"));
			addPercept(w.getName(), Literal.parseLiteral("jumbled(" + w.getJumbled() +")"));
			addPercept(w.getName(), Literal.parseLiteral("truckStatus(full)"));
			addPercept(w.getName(), Literal.parseLiteral("hand_in(none)"));
			addPercept(w.getName(), Literal.parseLiteral("dropLocal(none)"));
			addPercept(w.getName(), Literal.parseLiteral("qtdDischarge(0)"));
			addPercept(w.getName(), Literal.parseLiteral("capacityHelper(0)"));
			addPercept(w.getName(), Literal.parseLiteral("helper(true)"));
			addPercept(w.getName(), Literal.parseLiteral("busy(false)"));
			addPercept(w.getName(), Literal.parseLiteral("time(0)"));
			
			for(Truck t : model.getWorld().getTruckMap().values())
			{
				addPercept(w.getName(), Literal.parseLiteral("plays(initiator," + t.getName() + ")"));
				addPercept(w.getName(), Literal.parseLiteral("posTruck(" + t.getPos().x + "," + t.getPos().y + ")"));
				addPercept(w.getName(), Literal.parseLiteral("qtdTruck(" + t.getName() + "," + t.getQtdThings() + ")"));
				
				if(rand.nextDouble() <= 0.6)
					addPercept(w.getName(), Literal.parseLiteral("truckGet(" + t.getName() + ")"));	
			}
		}
		
		// Start helpers
		for(Helper h : model.getWorld().getHelperMap().values())
		{
			addPercept("manager", Literal.parseLiteral("add_helper("+ h.getName() +")"));
			
			clearPercepts(h.getName());
			addPercept(h.getName(), Literal.parseLiteral("id(" + h.getId() + ")"));
			addPercept(h.getName(), Literal.parseLiteral("pos(" + h.getPos().x + "," + h.getPos().y +")"));
		}
	}
	
	// update perceptions
	public void updatePercepts() 
	{					
		// update the location of each worker
		for(Worker w : model.getWorld().getWorkerMap().values())
		{
			clearPercepts(w.getName());
			boolean isOverlapped = false;
			
			for(Truck t : model.getWorld().getTruckMap().values())
			{
				if (w.getPos().equals(t.getPos())) 
				{
					addPercept(w.getName(), Literal.parseLiteral("at(" + t.getName() + ")")); 
	                isOverlapped = true;
				}
			}
			
			for(Artifact g : model.getWorld().getGarageMap().values())
			{
				if (w.getPos().equals(g.getPos())) 
				{
					addPercept(w.getName(), Literal.parseLiteral("at(" + g.getName() + ")")); 
	                isOverlapped = true;
				}
			}
			
			for(Artifact r : model.getWorld().getRechargeMap().values())
			{
				if (w.getPos().equals(r.getPos())) 
				{
					addPercept(w.getName(), Literal.parseLiteral("at(" + r.getName() + ")")); 
	                isOverlapped = true;
				}
			}
			
			for(Artifact d : model.getWorld().getDepotsMap().values())
			{
				if (w.getPos().equals(d.getPos())) 
				{
					addPercept(w.getName(), Literal.parseLiteral("at(" + d.getName() + ")")); 
	                isOverlapped = true;
				}
			}
			
			if(!isOverlapped)
				addPercept(w.getName(), Literal.parseLiteral("at(somewhere)"));
		}		
		
		// update the location of each helper
		for(Helper h : model.getWorld().getHelperMap().values())
		{	
			clearPercepts(h.getName());
			boolean isOverlapped = false;
			
			for(Worker w : model.getWorld().getWorkerMap().values())
			{
				for(Truck t : model.getWorld().getTruckMap().values())
				{					
					if(w.getPos().equals(t.getPos()))
					{
						addPercept(h.getName(), Literal.parseLiteral("at(" + t.getName() + ")"));
						isOverlapped = true;
					}
				}
				
				for(Artifact g : model.getWorld().getGarageMap().values())
				{
					if (w.getPos().equals(g.getPos())) 
					{
						addPercept(h.getName(), Literal.parseLiteral("at(" + g.getName() + ")")); 
		                isOverlapped = true;
					}
				}
				
				for(Artifact r : model.getWorld().getRechargeMap().values())
				{
					if (w.getPos().equals(r.getPos())) 
					{
						addPercept(h.getName(), Literal.parseLiteral("at(" + r.getName() + ")")); 
		                isOverlapped = true;
					}
				}
				
				for(Artifact d : model.getWorld().getDepotsMap().values())
				{
					if (w.getPos().equals(d.getPos())) 
					{
						addPercept(h.getName(), Literal.parseLiteral("at(" + d.getName() + ")")); 
		                isOverlapped = true;
					}
				}
				
				if(!isOverlapped)
					addPercept(h.getName(), Literal.parseLiteral("at(somewhere)"));
			}
		}
	}
	
	// Defining the actions that can be performed by agents
	@Override
	public boolean executeAction(String agName, Structure action) 
	{	
        if (action.getFunctor().equals("move_towards")) 
        {
        	int agentId = Integer.parseInt(agName.split("_")[1]);
        	int targetId = Integer.parseInt(action.getTerm(0).toString().split("_")[1]);
        	
        	if(model.moveTowards(agentId, model.getElement(targetId).getPos()))
        	{
        		updatePercepts();
                try {Thread.sleep(400);} 
                catch (InterruptedException e) 
                {
                	System.out.println(e.getMessage());
					e.printStackTrace();
				}
        		return true;
        	}
        	return false;
        }
        return true;
	}

	@Override
	public void stop() 
	{
		super.stop();
	}
}