package entities.model;

import java.util.ArrayList;
import java.util.List;

import entities.services.CreateMapVisitor;
import entities.services.CreateWorldVisitor;
import entities.services.LoadWorldVisitor;
import entities.services.SaveWorldVisitor;
import jason.environment.grid.Location;

/**
 * This class implements the world of agents.
 * This class is Java side for the Jason application.
 * Therefore, each agent described herein corresponds to an agent in Jason.
 */

public class World
{
	private MapPlacing placement;
	private List<Worker> workers;
	private List<Helper> helpers;
	private List<Truck> truckers;
	private List<Location> garages;
	private List<Location> rechargeStops;
	private List<Location> depots;
	
	private List<MapRouting> routes;
	private List<Location> obstacles;
	
	/**
	 * This constructor creates a random world.
	 * The map is created randomly.
	 * The agents are placed on the map randomly.
	 * @param width: the width of map.
	 * @param length: the length of map.
	 * @throws Exception 
	 */
	public World(Integer width, Integer length) throws Exception 
	{
		placement = new MapPlacing(width, length);
		workers = new ArrayList<Worker>();
		helpers = new ArrayList<Helper>();
		truckers = new ArrayList<Truck>();
		garages = new ArrayList<Location>();
		rechargeStops = new ArrayList<Location>();
		depots = new ArrayList<Location>();
		routes = new ArrayList<MapRouting>();
		obstacles = new ArrayList<Location>();
		
		placement.accept(new CreateMapVisitor());
		this.accept(new CreateWorldVisitor());
		this.accept(new SaveWorldVisitor());
		loadObstacles();
	}
	
	/**
	 * This constructor creates a world from an input file.
	 * @param fileName: name of input file.
	 */
	public World() 
	{
		this.accept(new LoadWorldVisitor());
		obstacles = new ArrayList<Location>();
		loadObstacles();
	}
	
	/**
	 * Interface used by design pattern Visitor.
	 * The idea is separating the operations of the class  into service operations.
	 * @param WorldVisitor: an operation that operates over the world. 
	 */
    public void accept(WorldVisitor visitor) 
    {
        visitor.visit(this);
    }

    /**
     * Find obstacles on the map and put each one of them (its location) in a list
     */
    private void loadObstacles()
    {
    	for(int i = 0; i < placement.getWidth(); i++)
    	{
    		for(int j = 0; j < placement.getLength(); j++)
    			if(placement.getMatrix()[i][j] == MazeElements.WALL.getContent())
    				obstacles.add(new Location(i, j));
    	}
    }
    
    public int getNumbAgents()
    {
    	return workers.size() + helpers.size() + truckers.size();
    }

	public MapPlacing getPlacement() 
	{
		return placement;
	}

	public void setPlacement(MapPlacing placement) 
	{
		this.placement = placement;
	}

	public List<Worker> getWorkers() 
	{
		return workers;
	}

	public void setWorkers(List<Worker> workers) 
	{
		this.workers = workers;
	}

	public List<Helper> getHelpers() 
	{
		return helpers;
	}

	public void setHelpers(List<Helper> helpers) 
	{
		this.helpers = helpers;
	}

	public List<Truck> getTruckers() 
	{
		return truckers;
	}

	public void setTruckers(List<Truck> truckers) 
	{
		this.truckers = truckers;
	}

	public List<Location> getGarages() 
	{
		return garages;
	}

	public void setGarages(List<Location> garages) 
	{
		this.garages = garages;
	}

	public List<Location> getRechargeStops() 
	{
		return rechargeStops;
	}

	public void setRechargeStops(List<Location> rechargeStops) 
	{
		this.rechargeStops = rechargeStops;
	}

	public List<Location> getDepots() 
	{
		return depots;
	}

	public void setDepots(List<Location> depots) 
	{
		this.depots = depots;
	}

	public List<MapRouting> getRoutes() 
	{
		return routes;
	}

	public void setRoutes(List<MapRouting> routes) 
	{
		this.routes = routes;
	}

	public List<Location> getObstacles() 
	{
		return obstacles;
	}

	public void setObstacles(List<Location> obstacles) 
	{
		this.obstacles = obstacles;
	}

	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer();
		
		for(Worker w : workers)
			sb.append(w).append("\n");
		
		for(Helper h : helpers)
			sb.append(h).append("\n");
		
		for(Truck t : truckers)
			sb.append(t).append("\n");
		
		for(Location g : garages)
			sb.append(g).append("\n");
		
		for(Location r : rechargeStops)
			sb.append(r).append("\n");
		
		sb.append(placement);
		return sb.toString();
	}
}