package entities.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.model.Helper;
import entities.model.Map;
import entities.model.MazeElements;
import entities.model.Position;
import entities.model.Truck;
import entities.model.Worker;

/**
 * This class loads a map from a file.
 * In loading process the agents and artifacts are put on the map. 
 */

public class LoadWorldFromFile 
{	
	private Map map;
	private List<Worker> workerList;
	private List<Helper> helperList;
	private List<Truck> truckerList;
	private List<Position> garageList;
	private List<Position> rechargeList;
	
	public LoadWorldFromFile() 
	{
		map = null;
		workerList = new ArrayList<Worker>();
		helperList = new ArrayList<Helper>();
		truckerList = new ArrayList<Truck>();
		garageList = new ArrayList<Position>();
		rechargeList = new ArrayList<Position>();
	}
	
	/**
	 * This method open the input file.
	 * @param fileName: name of input file (default path: src/maps/file.txt)
	 */
	public void load(String fileName) 
	{	
		try 
		{
			Scanner file = new Scanner(new File(fileName));
			List<String> lines = new ArrayList<String>();
			boolean header = true;
			
			while (file.hasNextLine()) 
			{
				String data = file.nextLine();
				
				if(!data.equals("$") && header) 
				{
					String fields[] = data.split(";");
					char type = fields[0].charAt(0);
					int x = Integer.parseInt(fields[1]);
					int y = Integer.parseInt(fields[2]);
					
					if(type == MazeElements.WORKER.getContent())
						workerList.add(new Worker(x, y));
					
					else if(type == MazeElements.HELPER.getContent())
						helperList.add(new Helper(x, y));
					
					else if(type == MazeElements.TRUCKER.getContent())
						truckerList.add(new Truck(x, y));
					
					else if(type == MazeElements.GARAGE.getContent())
						garageList.add(new Position(x, y));
					
					else if(type == MazeElements.RECHARGE_POINT.getContent())
						rechargeList.add(new Position(x, y));
				}
				else
					header = false;
				
				if(!data.equals("$") && !header)
					lines.add(data);
			}
			file.close();
			
			int width = lines.get(0).split(" ").length;
			int length = lines.size();
			
			map = new Map(width, length);
			
			for(int i = 0; i < width; i++)
			{
				String cells[] = lines.get(i).split(" ");
				
				for(int j = 0; j < length; j++)
					map.getMatrix()[i][j] = cells[j].charAt(0);
			}
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("An error has occurred to open the file.");
			e.printStackTrace();
		}
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public Worker[] getWorkes() 
	{
		return workerList.toArray(new Worker[workerList.size()]);
	}

	public Helper[] getHelpers() 
	{
		return helperList.toArray(new Helper[helperList.size()]);
	}

	public Truck[] getTruckers() 
	{
		return truckerList.toArray(new Truck[truckerList.size()]);
	}

	public Position[] getGarages() 
	{
		return garageList.toArray(new Position[garageList.size()]);
	}

	public Position[] getRechargeStops() 
	{
		return rechargeList.toArray(new Position[rechargeList.size()]);
	}
}