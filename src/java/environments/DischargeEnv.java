package environments;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import entities.model.Artifact;
import entities.model.Helper;
import entities.model.SimpleElement;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

public class DischargeEnv extends Environment
{
	public static AtomicInteger cnpid = new AtomicInteger();
	public static WorldModel model;
	private File debugFiles = new File("debugger/");
	
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
			
			for(File file: debugFiles.listFiles()) 
			    if (!file.isDirectory()) 
			        file.delete();
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
	private void startPercepts()
	{		
		// Start manager
		clearPercepts("manager");
		
		// Start truckers
		int count = 0;
		
		for(Truck t : model.getWorld().getTruckMap().values())
		{
			if(count < model.getWorld().getTruckMap().values().size() / 2.0)
			{
				t.setVisible(true);
				count++;
			}
			else
			{
				t.setVisible(false);
				model.getWorld().getTruckersOrder().add(t);
			}
			addPercept("manager", Literal.parseLiteral("add_trucker(" + t.getName() + ")"));
		}
		
		// Start helpers
		for(Helper h : model.getWorld().getHelperMap().values())
			addPercept("manager", Literal.parseLiteral("add_helper("+ h.getName() +")"));
		
		// Start workers
		for(Worker w : model.getWorld().getWorkerMap().values())
			addPercept("manager", Literal.parseLiteral("add_worker("+ w.getName() +")"));
	}
	
	// update perceptions
	private void updatePercepts(SimpleElement agent) 
	{					
		clearPercepts(agent.getName());
		boolean isOverlapped = false;
		
		for(Truck t : model.getWorld().getTruckMap().values())
		{
			if (agent.getPos().equals(t.getPos())) 
			{
				addPercept(agent.getName(), Literal.parseLiteral("at(" + t.getName() + ")"));
                isOverlapped = true;
			}
		}
		
		for(Artifact g : model.getWorld().getGarageMap().values())
		{
			if (agent.getPos().equals(g.getPos())) 
			{
				addPercept(agent.getName(), Literal.parseLiteral("at(" + g.getName() + ")"));
                isOverlapped = true;
			}
		}
		
		for(Artifact r : model.getWorld().getRechargeMap().values())
		{
			if (agent.getPos().equals(r.getPos())) 
			{
				addPercept(agent.getName(), Literal.parseLiteral("at(" + r.getName() + ")"));			
                isOverlapped = true;
			}
		}
		
		for(Artifact d : model.getWorld().getDepotsMap().values())
		{
			if (agent.getPos().equals(d.getPos())) 
			{
				addPercept(agent.getName(), Literal.parseLiteral("at(" + d.getName() + ")"));
                isOverlapped = true;
			}
		}
		
		if(!isOverlapped)
			addPercept(agent.getName(), Literal.parseLiteral("at(somewhere)"));
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
        		updatePercepts(model.getElement(agentId));
        		return true;
        	}
        	return false;
        }
        if (action.getFunctor().equals("move_worker")) 
        {
        	Worker worker = model.getWorld().getWorkerMap().get(Integer.parseInt(agName.split("_")[1]));
        	model.moveWorker(worker);
        }
        return true;
	}

	@Override
	public void stop() 
	{
		super.stop();
	}
}