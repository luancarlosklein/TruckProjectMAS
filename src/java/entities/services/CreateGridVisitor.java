package entities.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.enums.Constants;
import entities.enums.WorldElements;
import entities.model.GridLayout;
import entities.model.GridRoutes;
import entities.model.GridVisitor;

/**
 * This class generates a maze using the Prim's algorithm.
 */

public class CreateGridVisitor implements GridVisitor
{
	/**
	 * This method initialize a character map randomly.
	 * @param map: a matrix of characters not initialized.
	 */
	public void visit(GridLayout layout) 
	{
		for(int y = 0; y < layout.getHeight(); y++)
			for(int x = 0; x < layout.getWidth(); x++)
				layout.getMatrix()[y][x] = WorldElements.WALL.getContent();
		
		List<int[]> frontiers = new ArrayList<int[]>();
		Random r = new Random();
		
		// Initialization seed
		int x = r.nextInt(layout.getWidth());
        int y = r.nextInt(layout.getHeight());
        frontiers.add(new int[]{x,y,x,y});
		 
        // Creating paths inside of maze
        while (!frontiers.isEmpty())
        {
            final int[] f = frontiers.remove(r.nextInt( frontiers.size()));
            x = f[2];
            y = f[3];
            
            if (layout.getMatrix()[y][x] == WorldElements.WALL.getContent())
            {
            	layout.getMatrix()[y][x] = WorldElements.PASSAGE.getContent();
                layout.getMatrix()[f[0]][f[1]] = WorldElements.PASSAGE.getContent();
                
                if (y >= 2 && layout.getMatrix()[y - 2][x] == WorldElements.WALL.getContent())
                    frontiers.add( new int[]{y - 1, x, y - 2, x});
                
                if (x >= 2 && layout.getMatrix()[y][x - 2] == WorldElements.WALL.getContent())
                    frontiers.add( new int[]{y, x - 1, y, x - 2});
                
                if (y < layout.getWidth() - 2 && layout.getMatrix()[y + 2][x] == WorldElements.WALL.getContent())
                    frontiers.add( new int[]{y + 1, x, y + 2, x});
                
                if (x < layout.getHeight() - 2 && layout.getMatrix()[y][x + 2] == WorldElements.WALL.getContent())
                    frontiers.add( new int[]{y, x + 1, y, x + 2});
            }
        }
        
        // Adjusting the borders
        for(int i = 0; i < layout.getHeight(); i++)
        	layout.getMatrix()[i][0] = WorldElements.WALL.getContent();
        
        for(int i = 0; i < layout.getHeight(); i++)
        	layout.getMatrix()[i][layout.getHeight() - 1] = WorldElements.WALL.getContent();
        
        for(int j = 0; j < layout.getWidth(); j++)
        	layout.getMatrix()[0][j] = WorldElements.WALL.getContent();
        
        for(int j = 0; j < layout.getWidth(); j++)
        	layout.getMatrix()[layout.getWidth() - 1][j] = WorldElements.WALL.getContent();
	}

	public void visit(GridRoutes routes) 
	{
		for(int y = 0; y < routes.getHeight(); y++)
		{
			for(int x = 0; x < routes.getWidth(); x++)
				routes.getMatrix()[y][x] = Constants.INFINITE.getValue();
		}
	}
}