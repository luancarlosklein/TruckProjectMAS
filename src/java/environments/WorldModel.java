package environments;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.enums.Constants;
import entities.enums.WorldElements;
import entities.model.Artifact;
import entities.model.GridRoutes;
import entities.model.Helper;
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
	private Map<Integer, Integer> codeMapping;
	private List<Location> obstacles;
	
    private int stepCount;
	
	public WorldModel(World world)
	{
		super(world.getLayout().getWidth() + 1, world.getLayout().getHeight() + 1, world.getNumbAgents());
		this.world = world;
		this.idMapping = new HashMap<Integer, Integer>();
		this.codeMapping = new HashMap<Integer, Integer>();
		this.obstacles = computeObstacles();
		this.stepCount = 0;
		
		// Adding agents to the model
		int code = 0;
		for(Worker w : world.getWorkerMap().values())
		{
			setAgPos(code, w.getPos().x, w.getPos().y);
			idMapping.put(w.getId(), code);
			codeMapping.put(code++, w.getId());
		}
		
		for(Helper h : world.getHelperMap().values())
		{
			setAgPos(code, h.getPos().x, h.getPos().y);
			idMapping.put(h.getId(), code);
			codeMapping.put(code++, h.getId());
		}
		
		for(Truck t : world.getTruckMap().values())
		{
			setAgPos(code, t.getPos().x, t.getPos().y);
			idMapping.put(t.getId(), code);
			codeMapping.put(code++, t.getId());
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
	public List<Location> computeObstacles()
	{
		List<Location> obstacles = new ArrayList<Location>();
		
		for(Truck t : world.getTruckMap().values())
			world.getLayout().getMatrix()[t.getPos().y][t.getPos().x] = WorldElements.PASSAGE.getContent();
		
		for(int i = 0; i < world.getLayout().getHeight(); i++)
		{
			for(int j = 0; j < world.getLayout().getWidth(); j++)
				if(world.getLayout().getMatrix()[i][j] == WorldElements.WALL.getContent())
					obstacles.add(new Location(i, j));
		}
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
	 * @code agentId: id of agent in .
	 * @param tarPos: the target position of agent.
	 */
	public boolean moveTowards(int agentId, Location tarPos) 
	{
		SimpleElement agent = getElement(agentId);
		GridRoutes currentRoute = null;
		SimpleElement currentTarget = null;
		List<Integer> values = new ArrayList<Integer>();
		List<Location> possibilities = new ArrayList<Location>();        
        
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
		if(world.getLayout().isObstacle(agent.getPos().x, agent.getPos().y))
			throw new Error("Agent " + agent.getName() + " jumps over the wall, it is going to the " + currentTarget.getName());
		
		if(currentTarget == null || currentRoute == null)
			throw new Error("Target doesn't exist, or it is impossible to find a route to the target.");
		
//		//-----------------------------------------------
//		System.out.println(currentRoute);
//        System.out.println("agent pos: " + agent.getPos());
//        //-----------------------------------------------
        
		// Computing the new direction of agent
        int x = agent.getPos().x;
        int y = agent.getPos().y;
        
		if ((y - 1) >= 0 && !world.getLayout().isObstacle(x, y - 1))
		{
			possibilities.add(new Location(x, y - 1));
			values.add(currentRoute.getMatrix()[y - 1][x]);
		}
		if((y + 1) < world.getLayout().getHeight() && !world.getLayout().isObstacle(x, y + 1))
		{
			possibilities.add(new Location(x, y + 1));
			values.add(currentRoute.getMatrix()[y + 1][x]);
		}
		if((x - 1) >= 0 && !world.getLayout().isObstacle(x - 1, y))
		{
			possibilities.add(new Location(x - 1, y));
			values.add(currentRoute.getMatrix()[y][x - 1]);
		}
		if((x + 1) < world.getLayout().getWidth() && !world.getLayout().isObstacle(x + 1, y))
		{
			possibilities.add(new Location(x + 1, y));
			values.add(currentRoute.getMatrix()[y][x + 1]);
		}
		
		// Finding the best possibilities
		if(!possibilities.isEmpty())
		{
			int minVal = values.get(0);
			Location minPos = possibilities.get(0);
			
			for(int i = 0; i < possibilities.size(); i++)
			{
				if(values.get(i) < minVal)
	        	{
	        		minVal = values.get(i);
	        		minPos = possibilities.get(i);
	        	}
			}
			
	        // In this moment, considering the LRTA*, the estimations of distance are shared among agents. 
	        if (currentRoute.getMatrix()[agent.getPos().y][agent.getPos().x] > 0)
	        	currentRoute.getMatrix()[agent.getPos().y][agent.getPos().x] = minVal + 1;
			
	        // Updating the agent position
	        setAgPos(getIdMapping().get(agent.getId()), minPos);
	        agent.setPos(minPos);
		}
       
        if (view != null) 
        {
        	for(Truck t : world.getTruckMap().values())
        	{
        		if(agent.getPos() != t.getPos())
        			setAgPos(getIdMapping().get(t.getId()), t.getPos());
        	}

        	for(Worker w : world.getWorkerMap().values())
        	{
        		if(agent.getPos() != w.getPos())
        			setAgPos(getIdMapping().get(w.getId()), w.getPos());
        	}
        	
        	for(Helper h : world.getHelperMap().values())
        	{
        		if(agent.getPos() != h.getPos())
        			setAgPos(getIdMapping().get(h.getId()), h.getPos());
        	}
        	
        	for(Artifact g : world.getGarageMap().values())
        		view.update(g.getPos().x, g.getPos().y);
        	
        	for(Artifact r : world.getRechargeMap().values())
        		view.update(r.getPos().x, r.getPos().y);
        	
        	for(Artifact d : world.getDepotsMap().values())
        		view.update(d.getPos().x, d.getPos().y);
        }
        return true;
	}
	
	/**
	 * An action, it is used to move an agent over the grid.
	 * @code agentId: id of agent in .
	 * @param tarPos: the target position of agent.
	 */
	public boolean moveWorker(Worker worker)
	{
		Location pos = getFreePos();
		
		while((pos.x <= 0 || pos.x >= width - 2) || (pos.y <= 0 || pos.y >= height - 2))
		{
			pos = getFreePos();
		}
		
		setAgPos(getIdMapping().get(worker.getId()), pos);
		worker.setPos(pos);
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
	
	public Map<Integer, Integer> codeMapping() 
	{
		return codeMapping;
	}

	public int getStepCount() 
	{
		return stepCount;
	}

	public List<Location> getObstacles() 
	{
		return obstacles;
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			World w1 = new World(10, 10);
			World w2 = new World();
			
			System.out.println(w1);
			System.out.println(w2);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}