package entities.services;

import java.util.Random;

import entities.model.Helper;
import entities.model.Map;
import entities.model.MapVisitor;
import entities.model.MazeElements;
import entities.model.Position;
import entities.model.Truck;
import entities.model.Worker;

/**
 * This class places the agents and artifacts on the map randomly.
 */

public class RandomPlacingVisitor implements MapVisitor 
{	
	private final int PLACEMENT_ATTEMPTS = 1000;
	
	private Worker workes[];
	private Helper helpers[];
	private Truck truckers[];
	private Position garages[];
	private Position rechargeStops[];
	
	// Constructor
	public RandomPlacingVisitor(Worker[] workes, 
			Helper[] helpers, Truck[] truckers, 
			Position[] garages, Position[] rechargeStops) 
	{
		this.workes = workes;
		this.helpers = helpers;
		this.truckers = truckers;
		this.garages = garages;
		this.rechargeStops = rechargeStops;
	}
	
	/**
	 * This method access a map and places the agents and artifacts on the grid.
	 * @param map: a map that describes the placing of agents and artifacts.
	 */
	public void visit(Map map) 
	{
		Random r = new Random();
		int placedWorkers = 0;
		int placedHelpers = 0;
		int placedGarages = 0;
		int placedRecharge = 0;
		
		// Placing the workers
		for(int i = 0; i < workes.length; i++)
		{
			int x = r.nextInt(map.getWidth());
			int y = r.nextInt(map.getLength());
			
			while(map.getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(map.getWidth());
				y = r.nextInt(map.getLength());
				
				placedWorkers++;
				
				if(placedWorkers > PLACEMENT_ATTEMPTS)
					throw new Error("The number of positioning attempts exceeded the allowed limit for workers.");
			}
			
			map.getMatrix()[x][y] = MazeElements.WORKER.getContent();
			workes[i] = new Worker(x, y);
		}
		
		// Placing the helpers
		for(int i = 0; i < helpers.length; i++)
		{
			int x = r.nextInt(map.getWidth());
			int y = r.nextInt(map.getLength());
			
			while(map.getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(map.getWidth());
				y = r.nextInt(map.getLength());
				
				placedHelpers++;
				
				if(placedHelpers > PLACEMENT_ATTEMPTS)
					throw new Error("The number of positioning attempts exceeded the allowed limit for helpers.");
			}
			
			map.getMatrix()[x][y] = MazeElements.HELPER.getContent();
			helpers[i] = new Helper(x, y);
		}
		
		// Placing the garages
		for(int i = 0; i < garages.length; i++)
		{
			int x = r.nextInt(map.getWidth());
			int y = r.nextInt(map.getLength());
			
			while(map.getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(map.getWidth());
				y = r.nextInt(map.getLength());
				
				placedGarages++;
				
				if(placedGarages > PLACEMENT_ATTEMPTS)
					throw new Error("The number of positioning attempts exceeded the allowed limit for garages.");
			}
			
			map.getMatrix()[x][y] = MazeElements.GARAGE.getContent();
			garages[i] = new Position(x, y);
		}
		
		// Placing the recharge points
		for(int i = 0; i < rechargeStops.length; i++)
		{
			int x = r.nextInt(map.getWidth());
			int y = r.nextInt(map.getLength());
			
			while(map.getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(map.getWidth());
				y = r.nextInt(map.getLength());
				
				placedRecharge++;
				
				if(placedRecharge > PLACEMENT_ATTEMPTS)
					throw new Error("The number of positioning attempts exceeded the allowed limit for recharge stops.");
			}
			
			map.getMatrix()[x][y] = MazeElements.RECHARGE_POINT.getContent();
			rechargeStops[i] = new Position(x, y);
		}
 		
		// Placing the truckers
		for(int i = 0; i < truckers.length; i++)
		{
			int x = -1;
			int y = -1;
			
			for(int aux = 0; aux < map.getWidth(); aux++)
			{	
				if(map.getMatrix()[aux][1] == MazeElements.PASSAGE.getContent() 
						&& map.getMatrix()[aux][0] == MazeElements.WALL.getContent())
				{
					map.getMatrix()[aux][0] = MazeElements.TRUCKER.getContent();
					x = aux;
					y = 0;
					break;
				}
				else if(map.getMatrix()[aux][map.getLength() - 2] == MazeElements.PASSAGE.getContent() 
						&& map.getMatrix()[aux][map.getLength() - 1] == MazeElements.WALL.getContent())
				{
					map.getMatrix()[aux][map.getLength() - 1] = MazeElements.TRUCKER.getContent();
					x = aux;
					y = map.getLength() - 1;
					break;
				}
				else if(map.getMatrix()[1][aux] == MazeElements.PASSAGE.getContent() 
						&& map.getMatrix()[0][aux] == MazeElements.WALL.getContent())
				{
					map.getMatrix()[0][aux] = MazeElements.TRUCKER.getContent();
					x = 0;
					y = aux;
					break;
				}
				else if(map.getMatrix()[map.getWidth() - 2][aux] == MazeElements.PASSAGE.getContent() 
						&& map.getMatrix()[map.getWidth() - 1][aux] == MazeElements.WALL.getContent())
				{
					map.getMatrix()[map.getWidth() - 1][aux] = MazeElements.TRUCKER.getContent();
					x = map.getWidth() - 1;
					y = aux;
					break;
				}
			}
			if(x == -1 || y == -1)
				throw new Error("One or more truckers were not placed");
			else
				truckers[i] = new Truck(x, y);
		}
	}
}