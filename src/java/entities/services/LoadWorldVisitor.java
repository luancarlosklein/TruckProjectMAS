package entities.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.model.Artifact;
import entities.model.Helper;
import entities.model.MapElements;
import entities.model.MapPlacing;
import entities.model.Truck;
import entities.model.Worker;
import entities.model.World;
import entities.model.WorldVisitor;

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
			
			while (file.hasNextLine()) 
			{
				String data = file.nextLine();
				
				if(!data.equals("$") && header) 
				{
					String fields[] = data.split(";");
					char type = fields[0].charAt(0);
					int x = Integer.parseInt(fields[1]);
					int y = Integer.parseInt(fields[2]);
					
					if(type == MapElements.WORKER.getContent())
						world.getWorkers().add(new Worker(x, y));
					
					else if(type == MapElements.HELPER.getContent())
						world.getHelpers().add(new Helper(x, y));
					
					else if(type == MapElements.TRUCKER.getContent())
						world.getTruckers().add(new Truck(x, y));
					
					else if(type == MapElements.GARAGE.getContent())
						world.getGarages().add(new Artifact(x, y, MapElements.GARAGE));
					
					else if(type == MapElements.RECHARGE_POINT.getContent())
						world.getGarages().add(new Artifact(x, y, MapElements.RECHARGE_POINT));
					
					else if(type == MapElements.DEPOT.getContent())
						world.getGarages().add(new Artifact(x, y, MapElements.DEPOT));
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