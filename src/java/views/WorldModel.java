package views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import entities.model.Artifact;
import entities.model.Constants;
import entities.model.Helper;
import entities.model.MapElements;
import entities.model.MapRouting;
import entities.model.SimpleElement;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

/**
 *  This class define whose elements will be drew by the viewer.
 *  The viewer is shown when the option 'gui' is enabled in the class {@see DischargeEnv}.
 */

public class WorldModel extends GridWorldModel
{
	private World world;
	private Map<Integer, Integer> idMapping;
    private int stepCount;
	
	public WorldModel(World world)
	{
		super(world.getPlacement().getWidth(), world.getPlacement().getLength(), world.getNumbAgents());
		this.world = world;
		this.idMapping = new HashMap<Integer, Integer>();
		this.stepCount = 0;
		
		// Adding agents to the model
		int code = 0;
		for(Worker w : world.getWorkerMap().values())
		{
			setAgPos(code, w.getPos().x, w.getPos().y);
			idMapping.put(code++, w.getId());
		}
		
		for(Helper h : world.getHelperMap().values())
		{
			setAgPos(code, h.getPos().x, h.getPos().y);
			idMapping.put(code++, h.getId());
		}
		
		for(Truck t : world.getTruckMap().values())
		{
			setAgPos(code, t.getPos().x, t.getPos().y);
			idMapping.put(code++, t.getId());
		}
		
		// Adding the artifacts that will be drew
		for(Artifact g : world.getGarageMap().values())
			add(Constants.GARAGE.getValue(), g.getPos().x, g.getPos().y);
		
		for(Artifact r : world.getRechargeMap().values())
			add(Constants.RECHARGE.getValue(), r.getPos().x, r.getPos().y);
		
		for(Artifact d : world.getDepotsMap().values())
			add(Constants.DEPOT.getValue(), d.getPos().x, d.getPos().y);
	}
	
	/**
	 * This method finds the positions in the Map that are obstacles
	 * @return a list of obstacles (WALL)
	 */
	public List<Location> getObstacles()
	{
		List<Location> obstacles = new ArrayList<Location>();
		
		for(int i = 0; i < world.getPlacement().getWidth(); i++)
		{
			for(int j = 0; j < world.getPlacement().getLength(); j++)
				if(world.getPlacement().getMatrix()[i][j] == MapElements.WALL.getContent())
					obstacles.add(new Location(i, j));
		}
		
		for(Truck t : world.getTruckMap().values())
			obstacles.remove(obstacles.indexOf(t.getPos()));
		
		return obstacles;
	}

	/**
	 * This method finds the positions of agents
	 * @return a list of agents' positions
	 */
	public List<Location> getAgentsPositions()
	{
		List<Location> positions = new ArrayList<Location>();
		
		for(Worker w : world.getWorkerMap().values())
			positions.add(w.getPos());
		
		for(Helper h : world.getHelperMap().values())
			positions.add(h.getPos());
		
		for(Truck t : world.getTruckMap().values())
			positions.add(t.getPos());
		
		return positions;
	}
	
	/**
	 * This method searches for a element by its id.
	 * @param id: id of searched element.
	 * @return a SimpleElement.
	 */
	public SimpleElement getElement(int id)
	{	
		if(world.getWorkerMap().containsKey(id))
			return world.getWorkerMap().get(id);
		
		else if(world.getHelperMap().containsKey(id))
			return world.getHelperMap().get(id);
		
		else if(world.getTruckMap().containsKey(id))
			return world.getTruckMap().get(id);
		
		else if(world.getGarageMap().containsKey(id))
			return world.getGarageMap().get(id);
		
		else if(world.getDepotsMap().containsKey(id))
			return world.getDepotsMap().get(id);
		
		else if(world.getRechargeMap().containsKey(id))
			return world.getRechargeMap().get(id);
		
		else throw new Error("Informed id is not valid!");
	}
	
	/**
	 * An action, it is used to move the agents on the grid.
	 * @param tarPos: the target position of agent.
	 * @code code: the code of agent on the grid.
	 */
	public boolean moveTowards(Location tarPos, int code) 
	{
		Location currentPos = getAgPos(code);
		MapRouting currentRoute = null;
		SimpleElement currentTarget = null;
		SimpleElement currentAgent = getElement(idMapping.get(code));
		List<Integer> indexes = new ArrayList<Integer>();
		Location[] possibilities = {new Location(-1, -1), new Location(-1, -1), new Location(-1, -1), new Location(-1, -1)};
        double[] values = {100000.0, 10000.0, 10000.0, 10000.0};
        Random rand = new Random();
		
        // Getting the route to target position
		for(Truck t : world.getTruckMap().values())
		{
			if(t.getPos().equals(tarPos))
			{
				currentRoute = world.getRoutes().get(t.getId());
				currentTarget = t;
				break;
			}
		}
		
		if(currentTarget == null)
		{
			for(Artifact g : world.getGarageMap().values())
			{
				if(g.getPos().equals(tarPos))
				{
					currentRoute = world.getRoutes().get(g.getId());
					currentTarget = g;
					break;
				}
			}			
		}
		
		if(currentTarget == null)
		{
			for(Artifact r : world.getRechargeMap().values())
			{
				if(r.getPos().equals(tarPos))
				{
					currentRoute = world.getRoutes().get(r.getId());
					currentTarget = r;
					break;
				}
			}			
		}
		
		if(currentTarget == null)
		{
			for(Artifact d : world.getDepotsMap().values())
			{
				if(d.getPos().equals(tarPos))
				{
					currentRoute = world.getRoutes().get(d.getId());
					currentTarget = d;
					stepCount++;
					break;
				}
			}			
		}
		
		// Checking possible error situations
		if(getObstacles().contains(currentPos))
			throw new Error("Agent " + code + " jumps over the wall, it is going to " + currentTarget.getName());
		
		if(currentTarget == null || currentRoute == null)
			throw new Error("Target doesn't exist, or it is impossible to find a route to target.");
		
		// Computing the new direction of agent
		if ((currentPos.y - 1) >= 0)
		{
			possibilities[0] = new Location(currentPos.x, currentPos.y - 1);
	   	 	values[0] = currentRoute.getMatrix()[currentPos.x][currentPos.y - 1];
		}
		if((currentPos.y + 1) < world.getPlacement().getLength())
		{
			possibilities[1] = new Location(currentPos.x, currentPos.y + 1);
       	 	values[1] = currentRoute.getMatrix()[currentPos.x][currentPos.y + 1];
		}
		if((currentPos.x - 1) >= 0)
		{
			possibilities[2] = new Location(currentPos.x - 1, currentPos.y);
       	 	values[2] = currentRoute.getMatrix()[currentPos.x - 1][currentPos.y];
		}
		if((currentPos.x + 1) >= world.getPlacement().getWidth())
		{
			possibilities[3] = new Location(currentPos.x + 1, currentPos.y);
       	 	values[3] = currentRoute.getMatrix()[currentPos.x + 1][currentPos.y];
		}
		
		// Finding the best possibilities
        double minVal = values[0];
        Location minPos = possibilities[0];
        
        for(int i = 0; i < 4; i++)
        {
        	if(values[i] < minVal)
        	{
        		indexes = new ArrayList<Integer>();
        		indexes.add(i);
        		minVal = values[i];
        		minPos = possibilities[i];
        	}	
        	if(values[i] == minVal)
        	{
        		indexes.add(i);
        		minVal = values[i];
        		minPos = possibilities[i];
        	}
        }
        
        // Selecting a random direction
        int index = rand.nextInt(indexes.size());
        minVal = values[index];
		minPos = possibilities[index];
		
        // In this moment, considering the LRTA*, the estimations of distance are shared among agents. 
        if (currentRoute.getMatrix()[currentPos.x][currentPos.y] >= 0)
        	currentRoute.getMatrix()[currentPos.x][currentPos.y] = minVal + 1;
		
        // Updating the agent position
        setAgPos(code, minPos);
        currentAgent.setPos(minPos);
       
        if (view != null) 
        {
        	for(Truck t : world.getTruckMap().values())
        		view.update(t.getPos().x, t.getPos().y);
        	
        	for(Artifact g : world.getGarageMap().values())
        		view.update(g.getPos().x, g.getPos().y);
        	
        	for(Artifact r : world.getRechargeMap().values())
        		view.update(r.getPos().x, r.getPos().y);
        	
        	for(Artifact d : world.getDepotsMap().values())
        		view.update(d.getPos().x, d.getPos().y);
        }
        return true;
	}

	public World getWorld() 
	{
		return world;
	}

	public Map<Integer, Integer> getIdMapping() 
	{
		return idMapping;
	}

	public int getStepCount() 
	{
		return stepCount;
	}
}