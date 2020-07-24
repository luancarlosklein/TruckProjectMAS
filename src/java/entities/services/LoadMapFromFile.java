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
 * This class load from a file the map where the agents are inserted on
 */

public class LoadMapFromFile 
{
	private String fileName;
	private List<Worker> workes;
	private List<Helper> helpers;
	private List<Truck> truckers;
	private List<Position> garages;
	private List<Position> rechargeStops;

	public LoadMapFromFile(String fileName) 
	{
		this.fileName = fileName;
		workes = new ArrayList<Worker>();
		helpers = new ArrayList<Helper>();
		truckers = new ArrayList<Truck>();
		garages = new ArrayList<Position>();
		rechargeStops = new ArrayList<Position>();
	}
	
	public Map load() 
	{	
		Map map = null;
		
		try 
		{
			File myObj = new File(fileName);
			Scanner file = new Scanner(myObj);
			boolean header = true;
			List<String> lines = new ArrayList<String>();
			
			while (file.hasNextLine()) 
			{
				String data = file.nextLine();
				
				if(!data.equals("$") && header) 
				{
					String fields[] = data.split(";");
					String type = fields[0];
					int x = Integer.parseInt(fields[1]);
					int y = Integer.parseInt(fields[2]);
					
					if(type.equals(MazeElements.WORKER.getContent()))
						workes.add(new Worker(x, y));
					else if(type.equals(MazeElements.HELPER.getContent()))
						helpers.add(new Helper(x, y));
					else if(type.equals(MazeElements.TRUCKER.getContent()))
						truckers.add(new Truck(x, y));
					else if(type.equals(MazeElements.GARAGE.getContent()))
						garages.add(new Position(x, y));
					else if(type.equals(MazeElements.RECHARGE_POINT.getContent()))
						rechargeStops.add(new Position(x, y));
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
				String cells[] = lines.get(i).split("");
				
				for(int j = 0; j < length; j++)
					map.getMatrix()[i][j] = cells[j].charAt(0);
			}
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("An error occur to open the file.");
			e.printStackTrace();
		}
		return map;
	}

	public List<Worker> getWorkes(){return workes;}
	public List<Helper> getHelpers(){return helpers;}
	public List<Truck> getTruckers() {return truckers;}
	public List<Position> getGarages(){return garages;}
	public List<Position> getRechargeStops(){return rechargeStops;}
}
