package entities.services;

import java.io.FileWriter;
import java.io.IOException;

import entities.model.MapElements;
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
			
			for(int i = 0; i < world.getPlacement().getWidth(); i++)
	    	{
	    		for(int j = 0; j < world.getPlacement().getLength(); j++)
	    		{
	    			if(world.getPlacement().getMatrix()[i][j] == MapElements.PASSAGE.getContent() 
	    					|| world.getPlacement().getMatrix()[i][j] == MapElements.WALL.getContent())
	    				sbBody.append(world.getPlacement().getMatrix()[i][j]).append(" ");
	    			else
	    			{
	    				sbHeader.append(world.getPlacement().getMatrix()[i][j]).append(";");
    					sbHeader.append(i).append(";").append(j).append("\n");
    					
	    				if(world.getPlacement().getMatrix()[i][j] == MapElements.TRUCKER.getContent())
	    					sbBody.append(MapElements.WALL.getContent()).append(" ");
	    				else
	    					sbBody.append(MapElements.PASSAGE.getContent()).append(" ");
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