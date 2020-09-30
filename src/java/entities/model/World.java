package entities.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import entities.enums.WorldElements;
import entities.services.CreateGridVisitor;
import entities.services.CreateWorldVisitor;
import entities.services.DefineWorldRoutesVisitor;
import entities.services.LoadWorldVisitor;
import entities.services.SaveWorldVisitor;

/**
 * This class implements the world of agents.
 * This class is Java side for the Jason application.
 * Therefore, each agent described herein corresponds to an agent in Jason.
 */

public class World
{
	// Logic map
	private GridLayout layout;
	
	// Structures
	private Map<Integer, Worker> workerMap = new HashMap<Integer, Worker>();
	private Map<Integer, Helper> helperMap = new HashMap<Integer, Helper>();
	private Map<Integer, Truck> truckMap = new HashMap<Integer, Truck>();
	private Map<Integer, Artifact> garageMap = new HashMap<Integer, Artifact>();
	private Map<Integer, Artifact> rechargeMap = new HashMap<Integer, Artifact>();
	private Map<Integer, Artifact> depotsMap = new HashMap<Integer, Artifact>();
	private Map<Integer, GridRoutes> routes = new HashMap<Integer, GridRoutes>();
	private Queue<Truck> truckersOrder;
	
	/**
	 * This constructor creates a random world.
	 * The map is created randomly.
	 * The agents are placed on the map randomly.
	 * @param width: the width of map.
	 * @param length: the length of map.
	 * @throws Exception 
	 */
	public World(Integer width, Integer height) throws Exception 
	{
		layout = new GridLayout(width, height);
		
		this.accept(new CreateWorldVisitor());
		this.accept(new DefineWorldRoutesVisitor());
		this.accept(new SaveWorldVisitor());
		this.truckersOrder = new LinkedList<Truck>();		
	}
	
	/**
	 * This constructor creates a world from an input file.
	 * @param fileName: name of input file.
	 */
	public World() 
	{
		// Defining grid
		this.accept(new LoadWorldVisitor());
		this.accept(new DefineWorldRoutesVisitor());
		this.truckersOrder = new LinkedList<Truck>();
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
     * @param element: a target element (e.g., truck, depot, garage, ...)
     */
    public void addRouteTo(SimpleElement element)
    {
    	GridRoutes route = new GridRoutes(layout.width, layout.height);
    	route.accept(new CreateGridVisitor());
    	
    	for (int y = 0; y < layout.height; y++) 
    	{
    		for (int x = 0; x < layout.width; x++) 
    		{
    			// Calculate the Manhattan distance (used as heuristic for LRTA*)
	        	if ((layout.matrix[y][x] != WorldElements.WALL.getContent()) || (x == element.getPos().x && y == element.getPos().y))
					route.matrix[y][x] = (Math.abs(element.getPos().x - x) + Math.abs(element.getPos().y - y));
	        }
	    }
    	routes.put(element.getId(), route);
    }
    
    /**
     * Remove a route from the map of routes
     * @param element: a target element (e.g., truck, depot, garage, ...).
     * @throws Exception 
     */
    public void removeRouteTo(SimpleElement element) throws Exception
    {
    	if(routes.containsKey(element.getId())) 
    		routes.remove(element.getId());
    	else
    		throw new Exception("There is not a route to informated position!");
    }
    
    public int getNumbAgents()
    {
    	return workerMap.values().size() + helperMap.values().size() + truckMap.values().size();
    }
    
    public void setLayout(GridLayout placement) 
    {
		this.layout = placement;
	}

	public GridLayout getLayout() 
    {
    	return layout;
    }

	public Map<Integer, Worker> getWorkerMap() 
	{
		return workerMap;
	}

	public Map<Integer, Helper> getHelperMap() 
	{
		return helperMap;
	}

	public Map<Integer, Truck> getTruckMap() 
	{
		return truckMap;
	}

	public Map<Integer, Artifact> getGarageMap() 
	{
		return garageMap;
	}

	public Map<Integer, Artifact> getRechargeMap() 
	{
		return rechargeMap;
	}

	public Map<Integer, Artifact> getDepotsMap() 
	{
		return depotsMap;
	}

	public Map<Integer, GridRoutes> getRoutes() 
	{
		return routes;
	}
	
	public Truck getNextTruck() 
	{
		Truck t = truckersOrder.remove();
		t.setVisible(true);
		return t;
	}
	
	public Queue<Truck> getTruckersOrder() 
	{
		return truckersOrder;
	}

	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer();
		
		for(Worker w : workerMap.values())
			sb.append(w).append("\n");
		
		for(Helper h : helperMap.values())
			sb.append(h).append("\n");
		
		for(Truck t : truckMap.values())
			sb.append(t).append("\n");
		
		for(Artifact g : garageMap.values())
			sb.append(g).append("\n");
		
		for(Artifact r : rechargeMap.values())
			sb.append(r).append("\n");
		
		for(Artifact d : depotsMap.values())
			sb.append(d).append("\n");
		
		sb.append(layout).append("\n");
		
		for(GridRoutes route : routes.values())
			sb.append(route).append("\n");
		
		return sb.toString();
	}
}