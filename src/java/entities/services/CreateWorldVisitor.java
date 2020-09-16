package entities.services;

import java.util.Random;

import entities.enums.Constants;
import entities.enums.HelperExpertiseClass;
import entities.enums.WorldElements;
import entities.model.Artifact;
import entities.model.Helper;
import entities.model.HelperDurabilityClass;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import entities.model.WorldVisitor;
import jason.environment.grid.Location;

/**
 * This class puts the agents and artifacts on the world (randomly).
 */

public class CreateWorldVisitor implements WorldVisitor 
{	
	// THESE VARIABLES DEFINE THE AMOUNT OF AGENTS AND ARTIFACTS AT THE WORLD.
	private final double QTD_WORKERS = 0.03;
	private final double QTD_HELPERS = 0.05;
	private final double QTD_TRUCKERS = 0.03;
	private final double QTD_GARAGES = 0.01;
	private final double QTD_RECHARGES = 0.02;
	private final double QTD_DEPOTS = 0.02;
	
	private	HelperBuilder hBuilder = new HelperBuilder();
	
	/**
	 * This method defines the initial world configuration.
	 * In this process, agents and artifacts are created and placed into the map.
	 * @param world: this parameter represents a state of world.
	 */
	public void visit(World world) 
	{	
		Random rand = new Random();
		int gridSize = world.getLayout().getWidth() * world.getLayout().getHeight();
		
		// Initializing the map randomly
		world.getLayout().accept(new CreateGridVisitor());
		
		// Placing the workers
		for(int i = 0; i < QTD_WORKERS * gridSize; i++)
		{
			Location pos = getFreePosition(world);			
			Worker w = new Worker(pos.x, pos.y);
			world.getLayout().getMatrix()[pos.y][pos.x] = WorldElements.WORKER.getContent();
			world.getWorkerMap().put(w.getId(), w);
		}
		
		// Placing the helpers
		for(int i = 0; i < QTD_HELPERS * gridSize; i++)
		{
			Helper h = null;
			Location pos = getFreePosition(world);
			
			switch (rand.nextInt(9)) 
			{
				case 0:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.LOW_EXPERTISE, HelperDurabilityClass.LOW_DURABILITY);
					break;

				case 1:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.LOW_EXPERTISE, HelperDurabilityClass.MIDDLE_DURABILITY);
					break;
				
				case 2:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.LOW_EXPERTISE, HelperDurabilityClass.HIGH_DURABILITY);
					break;
					
				case 3:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.MIDDLE_EXPERTISE, HelperDurabilityClass.LOW_DURABILITY);
					break;
					
				case 4:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.MIDDLE_EXPERTISE, HelperDurabilityClass.MIDDLE_DURABILITY);
					break;
					
				case 5:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.MIDDLE_EXPERTISE, HelperDurabilityClass.HIGH_DURABILITY);
					break;
					
				case 6:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.HIGH_EXPERTISE, HelperDurabilityClass.LOW_DURABILITY);
					break;
					
				case 7:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.HIGH_EXPERTISE, HelperDurabilityClass.MIDDLE_DURABILITY);
					break;
					
				case 8:
					h = hBuilder.build(pos.x, pos.y, rand.nextInt(5) + 1, HelperExpertiseClass.HIGH_EXPERTISE, HelperDurabilityClass.HIGH_DURABILITY);
					break;
			}
			
			world.getLayout().getMatrix()[pos.y][pos.x] = WorldElements.HELPER.getContent();
			world.getHelperMap().put(h.getId(), h);
		}
		
		// Placing the garages
		for(int i = 0; i < QTD_GARAGES * gridSize; i++)
		{
			Location pos = getFreePosition(world);
			Artifact g = new Artifact(pos.x, pos.y, WorldElements.GARAGE);			
			world.getLayout().getMatrix()[pos.y][pos.x] = WorldElements.GARAGE.getContent();
			world.getGarageMap().put(g.getId(), g);
		}
		
		// Placing the recharge points
		for(int i = 0; i < QTD_RECHARGES * gridSize; i++)
		{
			Location pos = getFreePosition(world);
			Artifact r = new Artifact(pos.x, pos.y, WorldElements.RECHARGE_POINT);
			world.getLayout().getMatrix()[pos.y][pos.x] = WorldElements.RECHARGE_POINT.getContent();
			world.getRechargeMap().put(r.getId(), r);
		}
		
		// Placing the depots
		for(int i = 0; i < QTD_DEPOTS * gridSize; i++)
		{
			Location pos = getFreePosition(world);
			Artifact d = new Artifact(pos.x, pos.y, WorldElements.DEPOT);
			world.getLayout().getMatrix()[pos.y][pos.x] = WorldElements.DEPOT.getContent();
			world.getDepotsMap().put(d.getId(), d);
		}
 		
		// Placing the truckers
		for(int i = 0; i < QTD_TRUCKERS * gridSize; i++)
		{
			int x = -1;
			int y = -1;
			
			for(int aux = 0; aux < world.getLayout().getHeight(); aux++)
			{	
				if(world.getLayout().getMatrix()[aux][1] == WorldElements.PASSAGE.getContent() 
						&& world.getLayout().getMatrix()[aux][0] == WorldElements.WALL.getContent())
				{
					world.getLayout().getMatrix()[aux][0] = WorldElements.TRUCKER.getContent();
					x = 0;
					y = aux;
					break;
				}
				else if(world.getLayout().getMatrix()[aux][world.getLayout().getWidth() - 2] == WorldElements.PASSAGE.getContent() 
						&& world.getLayout().getMatrix()[aux][world.getLayout().getWidth() - 1] == WorldElements.WALL.getContent())
				{
					world.getLayout().getMatrix()[aux][world.getLayout().getWidth() - 1] = WorldElements.TRUCKER.getContent();
					x = world.getLayout().getWidth() - 1;
					y = aux;
					break;
				}
				else if(world.getLayout().getMatrix()[1][aux] == WorldElements.PASSAGE.getContent() 
						&& world.getLayout().getMatrix()[0][aux] == WorldElements.WALL.getContent())
				{
					world.getLayout().getMatrix()[0][aux] = WorldElements.TRUCKER.getContent();
					x = aux;
					y = 0;
					break;
				}
				else if(world.getLayout().getMatrix()[world.getLayout().getHeight() - 2][aux] == WorldElements.PASSAGE.getContent() 
						&& world.getLayout().getMatrix()[world.getLayout().getHeight() - 1][aux] == WorldElements.WALL.getContent())
				{
					world.getLayout().getMatrix()[world.getLayout().getHeight() - 1][aux] = WorldElements.TRUCKER.getContent();
					x = aux;
					y = world.getLayout().getHeight() - 1;
					break;
				}
			}
			if(x == -1 || y == -1)
				throw new Error("One or more truckers were not placed");
			else
			{
				Truck t = new Truck(x, y);
				world.getTruckMap().put(t.getId(), t);
			}
		}
	}
	
	/**
	 * Find randomly a free position to place an element.
	 * @param world: this parameter represents a state of world 
	 * @return a Location(x, y)
	 * 
	 */
	private Location getFreePosition(World world)
	{
		int attempts = 0;
		
		Random rand = new Random();
		int x = rand.nextInt(world.getLayout().getWidth());
		int y = rand.nextInt(world.getLayout().getHeight());
		
		while(world.getLayout().getMatrix()[y][x] != WorldElements.PASSAGE.getContent())
		{
			x = rand.nextInt(world.getLayout().getWidth());
			y = rand.nextInt(world.getLayout().getHeight());
			
			if(attempts++ > Constants.PLACEMENT_ATTEMPTS.getValue())
				throw new Error("The number of positioning attempts exceeded the allowed limit.");
		}
		return new Location(x, y);
	}
}