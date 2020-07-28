package entities.services;

import java.util.Random;

import entities.model.Helper;
import entities.model.MazeElements;
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
	private final int PLACEMENT_ATTEMPTS = 1000; 
	
	/**
	 * This method defines the initial world configuration.
	 * In this process, agents and artifacts are created and placed into the map.
	 * @param world: this parameter represents the initial state of world.
	 */
	public void visit(World world) 
	{
		Random r = new Random();
		
		// THESE VARIABLES DEFINE THE AMOUNT OF AGENTS AND ARTIFACTS OF THE WORLD.
		int gridSize = world.getMap().getWidth() * world.getMap().getLength();
		int qtdWorkers = (int) (gridSize * 0.03);
		int qtdHelpers = (int) (gridSize * 0.05);
		int qtdTruckers = (int) (gridSize * 0.03);
		int qtdGarages = (int) (gridSize * 0.01);
		int qtdRecharges = (int) (gridSize * 0.02);

		// These variables control the number of times that attempts of random placement can occur. 
		int workersAttempts = 0;
		int helpersAttempts = 0;
		int garagesAttempts = 0;
		int rechargesAttempts = 0;
		
		// Placing the workers
		for(int i = 0; i < qtdWorkers; i++)
		{
			int x = r.nextInt(world.getMap().getWidth());
			int y = r.nextInt(world.getMap().getLength());
			
			while(world.getMap().getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getMap().getWidth());
				y = r.nextInt(world.getMap().getLength());
				
				if(workersAttempts++ > PLACEMENT_ATTEMPTS)
					throw new Error("The number of positioning attempts exceeded the allowed limit for workers.");
			}
			world.getMap().getMatrix()[x][y] = MazeElements.WORKER.getContent();
			world.getWorkers().add(new Worker(x, y));
		}
		
		// Placing the helpers
		for(int i = 0; i < qtdHelpers; i++)
		{
			int x = r.nextInt(world.getMap().getWidth());
			int y = r.nextInt(world.getMap().getLength());
			
			while(world.getMap().getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getMap().getWidth());
				y = r.nextInt(world.getMap().getLength());
				
				if(helpersAttempts++ > PLACEMENT_ATTEMPTS)
					throw new Error("The number of positioning attempts exceeded the allowed limit for helpers.");
			}
			world.getMap().getMatrix()[x][y] = MazeElements.HELPER.getContent();
			world.getHelpers().add(new Helper(x, y));
		}
		
		// Placing the garages
		for(int i = 0; i < qtdGarages; i++)
		{
			int x = r.nextInt(world.getMap().getWidth());
			int y = r.nextInt(world.getMap().getLength());
			
			while(world.getMap().getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getMap().getWidth());
				y = r.nextInt(world.getMap().getLength());
				
				if(garagesAttempts++ > PLACEMENT_ATTEMPTS)
					throw new Error("The number of positioning attempts exceeded the allowed limit for garages.");
			}
			world.getMap().getMatrix()[x][y] = MazeElements.GARAGE.getContent();
			world.getGarages().add(new Location(x, y));
		}
		
		// Placing the recharge points
		for(int i = 0; i < qtdRecharges; i++)
		{
			int x = r.nextInt(world.getMap().getWidth());
			int y = r.nextInt(world.getMap().getLength());
			
			while(world.getMap().getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(world.getMap().getWidth());
				y = r.nextInt(world.getMap().getLength());
				
				if(rechargesAttempts++ > PLACEMENT_ATTEMPTS)
					throw new Error("The number of positioning attempts exceeded the allowed limit for recharge stops.");
			}
			world.getMap().getMatrix()[x][y] = MazeElements.RECHARGE_POINT.getContent();
			world.getRechargeStops().add(new Location(x, y));
		}
 		
		// Placing the truckers
		for(int i = 0; i < qtdTruckers; i++)
		{
			int x = -1;
			int y = -1;
			
			for(int aux = 0; aux < world.getMap().getWidth(); aux++)
			{	
				if(world.getMap().getMatrix()[aux][1] == MazeElements.PASSAGE.getContent() 
						&& world.getMap().getMatrix()[aux][0] == MazeElements.WALL.getContent())
				{
					world.getMap().getMatrix()[aux][0] = MazeElements.TRUCKER.getContent();
					x = aux;
					y = 0;
					break;
				}
				else if(world.getMap().getMatrix()[aux][world.getMap().getLength() - 2] == MazeElements.PASSAGE.getContent() 
						&& world.getMap().getMatrix()[aux][world.getMap().getLength() - 1] == MazeElements.WALL.getContent())
				{
					world.getMap().getMatrix()[aux][world.getMap().getLength() - 1] = MazeElements.TRUCKER.getContent();
					x = aux;
					y = world.getMap().getLength() - 1;
					break;
				}
				else if(world.getMap().getMatrix()[1][aux] == MazeElements.PASSAGE.getContent() 
						&& world.getMap().getMatrix()[0][aux] == MazeElements.WALL.getContent())
				{
					world.getMap().getMatrix()[0][aux] = MazeElements.TRUCKER.getContent();
					x = 0;
					y = aux;
					break;
				}
				else if(world.getMap().getMatrix()[world.getMap().getWidth() - 2][aux] == MazeElements.PASSAGE.getContent() 
						&& world.getMap().getMatrix()[world.getMap().getWidth() - 1][aux] == MazeElements.WALL.getContent())
				{
					world.getMap().getMatrix()[world.getMap().getWidth() - 1][aux] = MazeElements.TRUCKER.getContent();
					x = world.getMap().getWidth() - 1;
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