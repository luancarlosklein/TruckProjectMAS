package entities.services;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.model.Map;
import entities.model.MapVisitor;
import entities.model.MazeElements;

/**
 * This class places the agents and artifacts on the map randomly.
 */

public class RandomMapGeneratorVisitor implements MapVisitor 
{	
	private Integer qtdWorkers;
	private Integer qtdHelpers;
	private Integer qtdTruckers;
	private Integer qtdGarages;
	private Integer qtdRechargePoints;
	
	public RandomMapGeneratorVisitor(Integer qtdWorkers, Integer qtdHelpers, 
			Integer qtdTruckers, Integer qtdGarages, Integer qtdRechargePoints) 
	{
		this.qtdWorkers = qtdWorkers;
		this.qtdHelpers = qtdHelpers;
		this.qtdTruckers = qtdTruckers;
		this.qtdGarages = qtdGarages;
		this.qtdRechargePoints = qtdRechargePoints;
	}
	
	public void visit(Map map) 
	{
		createPaths(map);
		placingElements(map);
		saveMapInFile(map);
	}
	
	private void createPaths(Map map)
	{
		List<int[]> frontiers = new ArrayList<int[]>();
		Random r = new Random();
		
		// Initialization seed
		int x = r.nextInt(map.getWidth());
        int y = r.nextInt(map.getLength());
        frontiers.add(new int[]{x,y,x,y});
		 
        // Creating the paths inside of maze
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
	
	private void placingElements(Map map)
	{
		Random r = new Random();
		
		// Placing the workers
		for(int i = 0; i < qtdWorkers; i++)
		{
			int x = r.nextInt(map.getWidth());
			int y = r.nextInt(map.getLength());
			
			while(map.getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(map.getWidth());
				y = r.nextInt(map.getLength());
			}
			
			map.getMatrix()[x][y] = MazeElements.WORKER.getContent();
		}
		
		// Placing the helpers
		for(int i = 0; i < qtdHelpers; i++)
		{
			int x = r.nextInt(map.getWidth());
			int y = r.nextInt(map.getLength());
			
			while(map.getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(map.getWidth());
				y = r.nextInt(map.getLength());
			}
			
			map.getMatrix()[x][y] = MazeElements.HELPER.getContent();
		}
		
		// Placing the garages
		for(int i = 0; i < qtdGarages; i++)
		{
			int x = r.nextInt(map.getWidth());
			int y = r.nextInt(map.getLength());
			
			while(map.getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(map.getWidth());
				y = r.nextInt(map.getLength());
			}
			
			map.getMatrix()[x][y] = MazeElements.GARAGE.getContent();
		}
		
		// Placing the recharge points
		for(int i = 0; i < qtdRechargePoints; i++)
		{
			int x = r.nextInt(map.getWidth());
			int y = r.nextInt(map.getLength());
			
			while(map.getMatrix()[x][y] != MazeElements.PASSAGE.getContent())
			{
				x = r.nextInt(map.getWidth());
				y = r.nextInt(map.getLength());
			}
			
			map.getMatrix()[x][y] = MazeElements.RECHARGE_POINT.getContent();
		}
		
		// Placing the truckers
		for(int i = 0; i < qtdTruckers; i++)
		{
			for(int j = 0; j < map.getWidth(); i++)
			{
				int x = r.nextInt(map.getWidth());
				int y = r.nextInt(map.getLength());
				
				if(map.getMatrix()[x][1] == MazeElements.PASSAGE.getContent() 
						&& map.getMatrix()[x][0] == MazeElements.WALL.getContent())
				{
					map.getMatrix()[x][0] = MazeElements.TRUCKER.getContent();
					break;
				}
				else if(map.getMatrix()[x][map.getLength() - 2] == MazeElements.PASSAGE.getContent() 
						&& map.getMatrix()[x][map.getLength() - 1] == MazeElements.WALL.getContent())
				{
					map.getMatrix()[x][map.getLength() - 1] = MazeElements.TRUCKER.getContent();
					break;
				}
				else if(map.getMatrix()[1][y] == MazeElements.PASSAGE.getContent() 
						&& map.getMatrix()[0][y] == MazeElements.WALL.getContent())
				{
					map.getMatrix()[0][y] = MazeElements.TRUCKER.getContent();
					break;
				}
				else if(map.getMatrix()[map.getWidth() - 2][y] == MazeElements.PASSAGE.getContent() 
						&& map.getMatrix()[map.getWidth() - 1][y] == MazeElements.WALL.getContent())
				{
					map.getMatrix()[map.getWidth() - 1][y] = MazeElements.TRUCKER.getContent();
					break;
				}
			}
		}
	}
	
	private void saveMapInFile(Map map)
	{
		try 
		{
			StringBuffer sbHeader = new StringBuffer();
			StringBuffer sbBody = new StringBuffer();
			
			FileWriter file = new FileWriter("map.txt");
			
			for(int i = 0; i < map.getWidth(); i++)
	    	{
	    		for(int j = 0; j < map.getLength(); j++)
	    		{
	    			if(map.getMatrix()[i][j] == MazeElements.PASSAGE.getContent() 
	    					|| map.getMatrix()[i][j] == MazeElements.WALL.getContent())
	    			{
	    				sbBody.append(map.getMatrix()[i][j]).append(" ");
	    			}
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
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}