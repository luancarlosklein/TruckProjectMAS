package views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.model.Artifact;
import entities.model.Constants;
import entities.model.Helper;
import entities.model.MapElements;
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
	
	public WorldModel(World world)
	{
		super(world.getPlacement().getWidth(), world.getPlacement().getLength(), world.getNumbAgents());
		this.world = world;
		this.idMapping = new HashMap<Integer, Integer>();
		
		// Adding agents to the model
		int i = 0;
		for(Worker w : world.getWorkerMap().values())
		{
			setAgPos(i, w.getPos().x, w.getPos().y);
			idMapping.put(i++, w.getId());
		}
		
		for(Helper h : world.getHelperMap().values())
		{
			setAgPos(i, h.getPos().x, h.getPos().y);
			idMapping.put(i++, h.getId());
		}
		
		for(Truck t : world.getTruckMap().values())
		{
			setAgPos(i, t.getPos().x, t.getPos().y);
			idMapping.put(i++, t.getId());
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

	public World getWorld() 
	{
		return world;
	}

	public Map<Integer, Integer> getIdMapping() 
	{
		return idMapping;
	}
}