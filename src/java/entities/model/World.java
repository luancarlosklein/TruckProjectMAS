package entities.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import entities.services.CreateMapVisitor;
import entities.services.CreateWorldVisitor;
import entities.services.DefineWorldRoutesVisitor;
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
	// Logic map
	private MapPlacing placement;
	
	// Structures
	private List<Worker> workers = new ArrayList<Worker>();
	private List<Helper> helpers = new ArrayList<Helper>();
	private List<Truck> truckers = new ArrayList<Truck>();
	private List<Artifact> garages = new ArrayList<Artifact>();
	private List<Artifact> rechargeStops = new ArrayList<Artifact>();
	private List<Artifact> depots = new ArrayList<Artifact>();
	private Map<Location, MapRouting> routes = new HashMap<Location, MapRouting>();
	
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
		
		this.accept(new CreateWorldVisitor());
		this.accept(new DefineWorldRoutesVisitor());
		this.accept(new SaveWorldVisitor());
	}
	
	/**
	 * This constructor creates a world from an input file.
	 * @param fileName: name of input file.
	 */
	public World() 
	{
		this.accept(new LoadWorldVisitor());
		this.accept(new DefineWorldRoutesVisitor());
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
     * This method creates a route to an interest point for worker agents.
     * @param pos: location of interest point (e.g., truck, depot, ...)
     */
    public void addRouteTo(Location pos)
    {
    	MapRouting route = new MapRouting(placement.width, placement.length);
    	route.accept(new CreateMapVisitor());
    	
    	for (int i = 0; i < placement.width; i++) 
    	{
    		for (int j = 0; j < placement.length; j++) 
    		{
    			// Calculate the Manhattan distance (used as heuristic for LRTA*)
	        	if (placement.matrix[i][j] != MapElements.WALL.getContent())
					route.matrix[i][j] = (double) (Math.abs(pos.x - j) + Math.abs(pos.y - i));
	        }
	    }
    	routes.put(pos, route);
    }
    
    /**
     * Remove a route from the map of routes
     * @param pos: location to be removed (a old interest point).
     * @throws Exception 
     */
    public void removeRouteTo(Location pos) throws Exception
    {
    	if(routes.containsKey(pos)) 
    		routes.remove(pos);
    	else
    		throw new Exception("There is not a route to informated position!");
    }
    
    public int getNumbAgents()
    {
    	return workers.size() + helpers.size() + truckers.size();
    }
    
    public void setPlacement(MapPlacing placement) 
    {
		this.placement = placement;
	}

	public MapPlacing getPlacement() 
    {
    	return placement;
    }

	public List<Worker> getWorkers() 
	{
		return workers;
	}

	public List<Helper> getHelpers() 
	{
		return helpers;
	}

	public List<Truck> getTruckers() 
	{
		return truckers;
	}

	public List<Artifact> getGarages() 
	{
		return garages;
	}

	public List<Artifact> getRechargeStops() 
	{
		return rechargeStops;
	}

	public List<Artifact> getDepots() 
	{
		return depots;
	}

	public Map<Location, MapRouting> getRoutes() 
	{
		return routes;
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
		
		for(Artifact g : garages)
			sb.append(g).append("\n");
		
		for(Artifact r : rechargeStops)
			sb.append(r).append("\n");
		
		for(Artifact d : depots)
			sb.append(d).append("\n");
		
		sb.append(placement).append("\n");
		
		for(MapRouting route : routes.values())
			sb.append(route).append("\n");
		
		return sb.toString();
	}
}