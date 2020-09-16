package entities.services;

import java.io.FileWriter;
import java.io.IOException;

import entities.enums.WorldElements;
import entities.model.Artifact;
import entities.model.Helper;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import entities.model.WorldVisitor;

/**
 * This saves the state of the world in a file.
 * By default, the files are stored in: /src/maps 
 */

public class SaveWorldVisitor implements WorldVisitor 
{
	public void visit(World world) 
	{
		try 
		{
			FileWriter file = new FileWriter("maps/map.txt");
			StringBuffer sbHeader = new StringBuffer();
			StringBuffer sbBody = new StringBuffer();
			
			for(Worker w : world.getWorkerMap().values())
			{
				sbHeader.append(WorldElements.WORKER).append(";");
				sbHeader.append(w.getPos().y).append(";");
				sbHeader.append(w.getPos().x).append("\n");
			}
			
			for(Helper h : world.getHelperMap().values())
			{
				sbHeader.append(WorldElements.HELPER).append(";");
				sbHeader.append(h.getPos().y).append(";");
				sbHeader.append(h.getPos().x).append(";");
				sbHeader.append(h.getBattery()).append(";");
				sbHeader.append(h.getEnergyCost()).append(";");
				sbHeader.append(h.getCapacity()).append(";");
				sbHeader.append(h.getVelocity()).append(";");
				sbHeader.append(h.getDexterity()).append(";");
				sbHeader.append(h.getFailureProb()).append(";");
				sbHeader.append(h.getSafety()).append("\n");
			}
			
			for(Truck t : world.getTruckMap().values())
			{
				sbHeader.append(WorldElements.TRUCKER).append(";");
				sbHeader.append(t.getPos().y).append(";");
				sbHeader.append(t.getPos().x).append("\n");
			}
			
			for(Artifact g : world.getGarageMap().values())
			{
				sbHeader.append(WorldElements.GARAGE).append(";");
				sbHeader.append(g.getPos().y).append(";");
				sbHeader.append(g.getPos().x).append("\n");
			}
			
			for(Artifact d : world.getDepotsMap().values())
			{
				sbHeader.append(WorldElements.DEPOT).append(";");
				sbHeader.append(d.getPos().y).append(";");
				sbHeader.append(d.getPos().x).append("\n");
			}
			
			for(Artifact r : world.getRechargeMap().values())
			{
				sbHeader.append(WorldElements.RECHARGE_POINT).append(";");
				sbHeader.append(r.getPos().y).append(";");
				sbHeader.append(r.getPos().x).append("\n");
			}
			
			for(int i = 0; i < world.getLayout().getHeight(); i++)
	    	{
	    		for(int j = 0; j < world.getLayout().getWidth(); j++)
	    		{
	    			if(world.getLayout().getMatrix()[i][j] == WorldElements.PASSAGE.getContent() 
	    					|| world.getLayout().getMatrix()[i][j] == WorldElements.WALL.getContent())
	    				sbBody.append(world.getLayout().getMatrix()[i][j]).append(" ");
	    			else
	    			{
	    				if(world.getLayout().getMatrix()[i][j] == WorldElements.TRUCKER.getContent())
	    					sbBody.append(WorldElements.WALL.getContent()).append(" ");
	    				else
	    					sbBody.append(WorldElements.PASSAGE.getContent()).append(" ");
	    			}
	    		}
	    		sbBody.append("\n");
	    	}			
			file.write(sbHeader.toString());
			file.write("$\n");
			file.write(sbBody.toString());
			file.close();
		} 
		catch (IOException e) 
		{
			System.out.println("An error occurred to save the file.");
			e.printStackTrace();
		}
	}
}