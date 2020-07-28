package entities.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.model.Map;
import entities.model.MapVisitor;
import entities.model.MazeElements;

/**
 * This class generates a maze using the Prim's algorithm.
 */

public class CreateMapVisitor implements MapVisitor
{
	/**
	 * This method access a map and creates paths inside of it.
	 * @param map: a map that describes the placing of agents and artifacts.
	 */
	public void visit(Map map) 
	{
		for(int i = 0; i < map.getWidth(); i++)
			for(int j = 0; j < map.getLength(); j++)
				map.getMatrix()[i][j] = MazeElements.WALL.getContent();
		
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
            
            if (map.getMatrix()[x][y] == MazeElements.WALL.getContent())
            {
            	map.getMatrix()[x][y] = MazeElements.PASSAGE.getContent();
                map.getMatrix()[f[0]][f[1]] = MazeElements.PASSAGE.getContent();
                
                if (x >= 2 && map.getMatrix()[x - 2][y] == MazeElements.WALL.getContent())
                    frontiers.add( new int[]{x - 1, y, x - 2, y});
                
                if (y >= 2 && map.getMatrix()[x][y - 2] == MazeElements.WALL.getContent())
                    frontiers.add( new int[]{x, y - 1, x, y - 2});
                
                if (x < map.getWidth() - 2 && map.getMatrix()[x + 2][y] == MazeElements.WALL.getContent())
                    frontiers.add( new int[]{x + 1, y, x + 2, y});
                
                if ( y < map.getLength() - 2 && map.getMatrix()[x][y+2] == MazeElements.WALL.getContent())
                    frontiers.add( new int[]{x, y + 1, x, y + 2});
            }
        }
        
        // Adjusting the borders
        for(int i = 0; i < map.getWidth(); i++)
        	map.getMatrix()[i][0] = MazeElements.WALL.getContent();
        
        for(int i = 0; i < map.getWidth(); i++)
        	map.getMatrix()[i][map.getWidth() - 1] = MazeElements.WALL.getContent();
        
        for(int j = 0; j < map.getLength(); j++)
        	map.getMatrix()[0][j] = MazeElements.WALL.getContent();
        
        for(int j = 0; j < map.getLength(); j++)
        	map.getMatrix()[map.getLength() - 1][j] = MazeElements.WALL.getContent();	
	}
}