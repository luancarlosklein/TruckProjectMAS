package environments;

import java.util.logging.Logger;

import entities.model.World;
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
		
	}
	
	// Defining the actions that can be performed by agents
	@Override
	public boolean executeAction(String agName, Structure action) 
	{
		logger.info("Agent: " + agName + " is executing: " + action);
		return true;
	}

	@Override
	public void stop() 
	{
		super.stop();
	}
}