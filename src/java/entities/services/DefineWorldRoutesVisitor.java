package entities.services;

import entities.model.Artifact;
import entities.model.Truck;
import entities.model.World;
import entities.model.WorldVisitor;

public class DefineWorldRoutesVisitor implements WorldVisitor 
{
	public void visit(World world) 
	{
		for(Truck t : world.getTruckers())
			world.addRouteTo(t.getPos());
		
		for(Artifact a : world.getDepots())
			world.addRouteTo(a.getPos());
		
		for(Artifact a : world.getGarages())
			world.addRouteTo(a.getPos());
		
		for(Artifact a : world.getRechargeStops())
			world.addRouteTo(a.getPos());
	}
}