package views;

import entities.model.World;
import jason.environment.grid.GridWorldModel;

public class WorldView extends GridWorldModel
{
	
	
	protected WorldView(World world) 
	{
		super(world.getPlacement().getWidth(), world.getPlacement().getLength(), world.getNumbAgents());
	}

}
