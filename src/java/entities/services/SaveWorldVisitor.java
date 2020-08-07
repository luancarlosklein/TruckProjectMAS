package entities.services;

import java.io.FileWriter;
import java.io.IOException;

import entities.model.WorldElements;
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
			
			for(int i = 0; i < world.getLayout().getHeight(); i++)
	    	{
	    		for(int j = 0; j < world.getLayout().getWidth(); j++)
	    		{
	    			if(world.getLayout().getMatrix()[i][j] == WorldElements.PASSAGE.getContent() 
	    					|| world.getLayout().getMatrix()[i][j] == WorldElements.WALL.getContent())
	    				sbBody.append(world.getLayout().getMatrix()[i][j]).append(" ");
	    			else
	    			{
	    				sbHeader.append(world.getLayout().getMatrix()[i][j]).append(";");
    					sbHeader.append(i).append(";").append(j).append("\n");
    					
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