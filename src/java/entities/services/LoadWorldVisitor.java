package entities.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.model.Helper;
import entities.model.MapPlacing;
import entities.model.MazeElements;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import entities.model.WorldVisitor;

import jason.environment.grid.Location;

/**
 * This class loads a world from a file.
 * In loading process the agents and artifacts are put on the world. 
 */

public class LoadWorldVisitor implements WorldVisitor 
{
	public void visit(World world) 
	{
		try 
		{
			Scanner file = new Scanner(new File("maps/map.txt"));
			List<String> lines = new ArrayList<String>();
			boolean header = true;
			
			world.setWorkers(new ArrayList<Worker>());
			world.setHelpers(new ArrayList<Helper>());
			world.setTruckers(new ArrayList<Truck>());
			world.setGarages(new ArrayList<Location>());
			world.setRechargeStops(new ArrayList<Location>());
			world.setDepots(new ArrayList<Location>());
			
			
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
						world.getWorkers().add(new Worker(x, y));
					
					else if(type == MazeElements.HELPER.getContent())
						world.getHelpers().add(new Helper(x, y));
					
					else if(type == MazeElements.TRUCKER.getContent())
						world.getTruckers().add(new Truck(x, y));
					
					else if(type == MazeElements.GARAGE.getContent())
						world.getGarages().add(new Location(x, y));
					
					else if(type == MazeElements.RECHARGE_POINT.getContent())
						world.getRechargeStops().add(new Location(x, y));
					
					else if(type == MazeElements.DEPOT.getContent())
						world.getRechargeStops().add(new Location(x, y));
				}
				else
					header = false;
				
				if(!data.equals("$") && !header)
					lines.add(data);
			}
			file.close();
			
			int width = lines.get(0).split(" ").length;
			int length = lines.size();
			
			world.setPlacement(new MapPlacing(width, length));
			
			for(int i = 0; i < width; i++)
			{
				String cells[] = lines.get(i).split(" ");
				
				for(int j = 0; j < length; j++)
					world.getPlacement().getMatrix()[i][j] = cells[j].charAt(0);
			}
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("An error has occurred to open the file.");
			e.printStackTrace();
		}
	}
}