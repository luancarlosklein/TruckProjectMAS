package environments;

import java.util.logging.Logger;

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
	private Logger logger = Logger.getLogger("Log messages for Class: " + this.getClass().getName());
	private WorldModel model;
	
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
		
		updatePercepts();
	}
	
	// Setting the initial perceptions
	public void updatePercepts() 
	{	
		// Start manager
		clearPercepts("manager");
		
		// Start workers
		for(Worker w : model.getWorld().getWorkerMap().values())
		{
			addPercept("manager", Literal.parseLiteral("add_worker("+ w.getName() +")"));
			
			clearPercepts(w.getName());
			addPercept(w.getName(), Literal.parseLiteral("pos(" + w.getPos().x + "," + w.getPos().y +")"));
			
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
		
		// Start helpers
		for(Helper h : model.getWorld().getHelperMap().values())
		{
			addPercept("manager", Literal.parseLiteral("add_helper("+ h.getName() +")"));
			
			clearPercepts(h.getName());
			addPercept(h.getName(), Literal.parseLiteral("pos(" + h.getPos().x + "," + h.getPos().y +")"));
			
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
		
		// Start truckers
		for(Truck t : model.getWorld().getTruckMap().values())
			addPercept("manager", Literal.parseLiteral("add_trucker("+ t.getName() +")"));
	}
	
	// Defining the actions that can be performed by agents
	@Override
	public boolean executeAction(String agName, Structure action) 
	{
        boolean result = false;
        
        if (action.getFunctor().equals("move_towards")) 
        {
            int code = Integer.parseInt(action.getTerm(1).toString());                 
            result = model.moveTowards(model.getElement(model.getIdMapping().get(code)).getPos(), code);
        }	
        if (result) 
        {
            updatePercepts();
            try { Thread.sleep(400); } catch (Exception e) {}
        }
        return result;
	}

	@Override
	public void stop() 
	{
		super.stop();
	}
}