package environments;

import java.util.logging.Logger;

import jason.asSyntax.Structure;
import jason.environment.Environment;

public class Temp extends Environment
{	
	private Logger logger = Logger.getLogger("Log messages for Class: " + this.getClass().getName());
	
	@Override
	public void init(String[] args) 
	{
		super.init(args);
		
		try 
		{	

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