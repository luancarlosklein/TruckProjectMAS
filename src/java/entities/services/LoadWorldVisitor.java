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
			int wCount = 0, hCount = 0, tCount = 0, gCount = 0, rCount = 0, dCount = 0;
			
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
					{
						Worker w = new Worker(x, y);
						w.setName("W" + wCount++);
						world.getWorkerMap().put(w.getId(), w);
					}
					
					else if(type == MapElements.HELPER.getContent())
					{
						Helper h = new Helper(x, y);
						h.setName("H" + hCount++);
						world.getHelperMap().put(h.getId(), h);
					}
					
					else if(type == MapElements.TRUCKER.getContent())
					{
						Truck t = new Truck(x, y);
						t.setName("T" + tCount++);
						world.getTruckMap().put(t.getId(), t);
					}
					
					else if(type == MapElements.GARAGE.getContent())
					{
						Artifact g = new Artifact(x, y, MapElements.GARAGE);
						g.setName("Garage" + gCount++);
						world.getGarageMap().put(g.getId(), g);
					}
					
					else if(type == MapElements.RECHARGE_POINT.getContent())
					{
						Artifact r = new Artifact(x, y, MapElements.RECHARGE_POINT);
						r.setName("Recharge" + rCount++);
						world.getRechargeMap().put(r.getId(), r);
					}
					
					else if(type == MapElements.DEPOT.getContent())
					{
						Artifact d = new Artifact(x, y, MapElements.DEPOT);
						d.setName("Depot" + dCount++);
						world.getDepotsMap().put(d.getId(), d);
					}
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