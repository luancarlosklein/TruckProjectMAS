package entities.services;

import java.io.FileWriter;
import java.io.IOException;

import entities.model.Map;
import entities.model.MazeElements;

/**
 * This class implements the save operation.
 * The map and the placing of agents are stored in a file.
 * By default, the maps are saved in: /src/maps 
 */

public class SaveWorldInFile 
{
	/**
	 * This method creates the file where the map is saved.
	 * @param map: a map that describes the placing of agents and artifacts. 
	 */
	public static void save(Map map)
	{
		try 
		{
			FileWriter file = new FileWriter("maps/map.txt");
			StringBuffer sbHeader = new StringBuffer();
			StringBuffer sbBody = new StringBuffer();
			
			for(int i = 0; i < map.getWidth(); i++)
	    	{
	    		for(int j = 0; j < map.getLength(); j++)
	    		{
	    			if(map.getMatrix()[i][j] == MazeElements.PASSAGE.getContent() 
	    					|| map.getMatrix()[i][j] == MazeElements.WALL.getContent())
	    				sbBody.append(map.getMatrix()[i][j]).append(" ");
	    			else
	    			{
	    				sbHeader.append(map.getMatrix()[i][j]).append(";");
    					sbHeader.append(i).append(";").append(j).append("\n");
    					
	    				if(map.getMatrix()[i][j] == MazeElements.TRUCKER.getContent())
	    					sbBody.append(MazeElements.WALL.getContent()).append(" ");
	    				else
	    					sbBody.append(MazeElements.PASSAGE.getContent()).append(" ");
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