package views;

import entities.model.World;
import jason.environment.grid.GridWorldModel;

public class WorldView extends GridWorldModel
{
	
	
	protected WorldView(World world) 
	{
		super(world.getMap().getWidth(), world.getMap().getLength(), world.getNumbAgents());
	}

}
