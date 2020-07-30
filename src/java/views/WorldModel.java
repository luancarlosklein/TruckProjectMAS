package views;

import entities.model.Artifact;
import entities.model.Helper;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import jason.environment.grid.GridWorldModel;

public class WorldModel extends GridWorldModel
{
	public WorldModel(World world) 
	{
		super(world.getPlacement().getWidth(), world.getPlacement().getLength(), world.getNumbAgents());
		
		for(Worker w : world.getWorkers())
			setAgPos(w.getId(), w.getPos().x, w.getPos().y);
		
		for(Helper h : world.getHelpers())
			setAgPos(h.getId(), h.getPos().x, h.getPos().y);
		
		for(Truck t : world.getTruckers())
			setAgPos(t.getId(), t.getPos().x, t.getPos().y);
		
		for(Artifact a : world.getGarages())
			add(a.getId(), a.getPos().x, a.getPos().y);
		
		for(Artifact a : world.getRechargeStops())
			add(a.getId(), a.getPos().x, a.getPos().y);
		
		for(Artifact a : world.getDepots())
			add(a.getId(), a.getPos().x, a.getPos().y);
	}
}
