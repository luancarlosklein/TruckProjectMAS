package entities.services;

import entities.model.Artifact;
import entities.model.Truck;
import entities.model.World;
import entities.model.WorldVisitor;

public class DefineWorldRoutesVisitor implements WorldVisitor 
{
	public void visit(World world) 
	{
		for(Truck t : world.getTruckMap().values())
			world.addRouteTo(t);
		
		for(Artifact d : world.getDepotsMap().values())
			world.addRouteTo(d);
		
		for(Artifact g : world.getGarageMap().values())
			world.addRouteTo(g);
		
		for(Artifact r : world.getRechargeMap().values())
			world.addRouteTo(r);
	}
}