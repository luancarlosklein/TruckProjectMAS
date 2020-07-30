package entities.services;

import java.util.Random;

import entities.model.Artifact;
import entities.model.Constants;
import entities.model.Helper;
import entities.model.MapElements;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import entities.model.WorldVisitor;

/**
 * This class puts the agents and artifacts on the world (randomly).
 */

public class CreateWorldVisitor implements WorldVisitor 
{	
	/**
	 * This method defines the initial world configuration.
	 * In this process, agents and artifacts are created and placed into the map.
	 * @param world: this parameter represents the initial state of world.
	 */
	public void visit(World world) 
	{
		Random r = new Random();
		
		// THESE VARIABLES DEFINE THE AMOUNT OF AGENTS AND ARTIFACTS OF THE WORLD.
		int gridSize = world.getPlacement().getWidth() * world.getPlacement().getLength();
		int qtdWorkers = (int) (gridSize * 0.03);
		int qtdHelpers = (int) (gridSize * 0.05);
		int qtdTruckers = (int) (gridSize * 0.03);
		int qtdGarages = (int) (gridSize * 0.01);
		int qtdRecharges = (int) (gridSize * 0.02);
		int qtdDepots = (int) (gridSize * 0.02);

		// These variables control the number of times that attempts of random placement can occur. 
		int workersAttempts = 0;
		int helpersAttempts = 0;
		int garagesAttempts = 0;
		int rechargesAttempts = 0;
		int depotsAttempts = 0;
		
		// Initializing the map randomly
		world.getPlacement().accept(new CreateMapVisitor());
		
		// Placing the workers
		for(int i = 0; i < qtdWorkers; i++)
		{
			int x = r.nextInt(world.getPlacement().getWidth());
			int y = r.nextInt(world.getPlacement().getLength());
			
			while(world.getPlacement().getMatrix()[x][y] != MapElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getPlacement().getWidth());
				y = r.nextInt(world.getPlacement().getLength());
				
				if(workersAttempts++ > Constants.PLACEMENT_ATTEMPTS.getValue())
					throw new Error("The number of positioning attempts exceeded the allowed limit for workers.");
			}
			world.getPlacement().getMatrix()[x][y] = MapElements.WORKER.getContent();
			world.getWorkers().add(new Worker(x, y));
		}
		
		// Placing the helpers
		for(int i = 0; i < qtdHelpers; i++)
		{
			int x = r.nextInt(world.getPlacement().getWidth());
			int y = r.nextInt(world.getPlacement().getLength());
			
			while(world.getPlacement().getMatrix()[x][y] != MapElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getPlacement().getWidth());
				y = r.nextInt(world.getPlacement().getLength());
				
				if(helpersAttempts++ > Constants.PLACEMENT_ATTEMPTS.getValue())
					throw new Error("The number of positioning attempts exceeded the allowed limit for helpers.");
			}
			world.getPlacement().getMatrix()[x][y] = MapElements.HELPER.getContent();
			world.getHelpers().add(new Helper(x, y));
		}
		
		// Placing the garages
		for(int i = 0; i < qtdGarages; i++)
		{
			int x = r.nextInt(world.getPlacement().getWidth());
			int y = r.nextInt(world.getPlacement().getLength());
			
			while(world.getPlacement().getMatrix()[x][y] != MapElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getPlacement().getWidth());
				y = r.nextInt(world.getPlacement().getLength());
				
				if(garagesAttempts++ > Constants.PLACEMENT_ATTEMPTS.getValue())
					throw new Error("The number of positioning attempts exceeded the allowed limit for garages.");
			}
			world.getPlacement().getMatrix()[x][y] = MapElements.GARAGE.getContent();
			world.getGarages().add(new Artifact(x, y, MapElements.GARAGE));
		}
		
		// Placing the recharge points
		for(int i = 0; i < qtdRecharges; i++)
		{
			int x = r.nextInt(world.getPlacement().getWidth());
			int y = r.nextInt(world.getPlacement().getLength());
			
			while(world.getPlacement().getMatrix()[x][y] != MapElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getPlacement().getWidth());
				y = r.nextInt(world.getPlacement().getLength());
				
				if(rechargesAttempts++ > Constants.PLACEMENT_ATTEMPTS.getValue())
					throw new Error("The number of positioning attempts exceeded the allowed limit for recharge stops.");
			}
			world.getPlacement().getMatrix()[x][y] = MapElements.RECHARGE_POINT.getContent();
			world.getGarages().add(new Artifact(x, y, MapElements.RECHARGE_POINT));
		}
		
		// Placing the depots
		for(int i = 0; i < qtdDepots; i++)
		{
			int x = r.nextInt(world.getPlacement().getWidth());
			int y = r.nextInt(world.getPlacement().getLength());
			
			while(world.getPlacement().getMatrix()[x][y] != MapElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getPlacement().getWidth());
				y = r.nextInt(world.getPlacement().getLength());
				
				if(depotsAttempts++ > Constants.PLACEMENT_ATTEMPTS.getValue())
					throw new Error("The number of positioning attempts exceeded the allowed limit for depots.");
			}
			world.getPlacement().getMatrix()[x][y] = MapElements.DEPOT.getContent();
			world.getGarages().add(new Artifact(x, y, MapElements.DEPOT));
		}
 		
		// Placing the truckers
		for(int i = 0; i < qtdTruckers; i++)
		{
			int x = -1;
			int y = -1;
			
			for(int aux = 0; aux < world.getPlacement().getWidth(); aux++)
			{	
				if(world.getPlacement().getMatrix()[aux][1] == MapElements.PASSAGE.getContent() 
						&& world.getPlacement().getMatrix()[aux][0] == MapElements.WALL.getContent())
				{
					world.getPlacement().getMatrix()[aux][0] = MapElements.TRUCKER.getContent();
					x = aux;
					y = 0;
					break;
				}
				else if(world.getPlacement().getMatrix()[aux][world.getPlacement().getLength() - 2] == MapElements.PASSAGE.getContent() 
						&& world.getPlacement().getMatrix()[aux][world.getPlacement().getLength() - 1] == MapElements.WALL.getContent())
				{
					world.getPlacement().getMatrix()[aux][world.getPlacement().getLength() - 1] = MapElements.TRUCKER.getContent();
					x = aux;
					y = world.getPlacement().getLength() - 1;
					break;
				}
				else if(world.getPlacement().getMatrix()[1][aux] == MapElements.PASSAGE.getContent() 
						&& world.getPlacement().getMatrix()[0][aux] == MapElements.WALL.getContent())
				{
					world.getPlacement().getMatrix()[0][aux] = MapElements.TRUCKER.getContent();
					x = 0;
					y = aux;
					break;
				}
				else if(world.getPlacement().getMatrix()[world.getPlacement().getWidth() - 2][aux] == MapElements.PASSAGE.getContent() 
						&& world.getPlacement().getMatrix()[world.getPlacement().getWidth() - 1][aux] == MapElements.WALL.getContent())
				{
					world.getPlacement().getMatrix()[world.getPlacement().getWidth() - 1][aux] = MapElements.TRUCKER.getContent();
					x = world.getPlacement().getWidth() - 1;
					y = aux;
					break;
				}
			}
			if(x == -1 || y == -1)
				throw new Error("One or more truckers were not placed");
			else
				world.getTruckers().add(new Truck(x, y));
		}
	}
}