package entities.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.model.Constants;
import entities.model.MapPlacing;
import entities.model.MapRouting;
import entities.model.MapVisitor;
import entities.model.MapElements;

/**
 * This class generates a maze using the Prim's algorithm.
 */

public class CreateMapVisitor implements MapVisitor
{
	/**
	 * This method initialize a character map randomly.
	 * @param map: a matrix of characters not initialized.
	 */
	public void visit(MapPlacing map) 
	{
		for(int i = 0; i < map.getWidth(); i++)
			for(int j = 0; j < map.getLength(); j++)
				map.getMatrix()[i][j] = MapElements.WALL.getContent();
		
		List<int[]> frontiers = new ArrayList<int[]>();
		Random r = new Random();
		
		// Initialization seed
		int x = r.nextInt(map.getWidth());
        int y = r.nextInt(map.getLength());
        frontiers.add(new int[]{x,y,x,y});
		 
        // Creating paths inside of maze
        while (!frontiers.isEmpty())
        {
            final int[] f = frontiers.remove(r.nextInt( frontiers.size()));
            x = f[2];
            y = f[3];
            
            if (map.getMatrix()[x][y] == MapElements.WALL.getContent())
            {
            	map.getMatrix()[x][y] = MapElements.PASSAGE.getContent();
                map.getMatrix()[f[0]][f[1]] = MapElements.PASSAGE.getContent();
                
                if (x >= 2 && map.getMatrix()[x - 2][y] == MapElements.WALL.getContent())
                    frontiers.add( new int[]{x - 1, y, x - 2, y});
                
                if (y >= 2 && map.getMatrix()[x][y - 2] == MapElements.WALL.getContent())
                    frontiers.add( new int[]{x, y - 1, x, y - 2});
                
                if (x < map.getWidth() - 2 && map.getMatrix()[x + 2][y] == MapElements.WALL.getContent())
                    frontiers.add( new int[]{x + 1, y, x + 2, y});
                
                if ( y < map.getLength() - 2 && map.getMatrix()[x][y+2] == MapElements.WALL.getContent())
                    frontiers.add( new int[]{x, y + 1, x, y + 2});
            }
        }
        
        // Adjusting the borders
        for(int i = 0; i < map.getWidth(); i++)
        	map.getMatrix()[i][0] = MapElements.WALL.getContent();
        
        for(int i = 0; i < map.getWidth(); i++)
        	map.getMatrix()[i][map.getWidth() - 1] = MapElements.WALL.getContent();
        
        for(int j = 0; j < map.getLength(); j++)
        	map.getMatrix()[0][j] = MapElements.WALL.getContent();
        
        for(int j = 0; j < map.getLength(); j++)
        	map.getMatrix()[map.getLength() - 1][j] = MapElements.WALL.getContent();
	}

	public void visit(MapRouting map) 
	{
		for(int i = 0; i < map.getWidth(); i++)
		{
			for(int j = 0; j < map.getLength(); j++)
				map.getMatrix()[i][j] = (double) Constants.INFINITE.getValue();
		}
	}
}