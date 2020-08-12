package entities.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.model.Artifact;
import entities.model.Helper;
import entities.model.WorldElements;
import entities.model.GridLayout;
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
					int y = Integer.parseInt(fields[1]);
					int x = Integer.parseInt(fields[2]);
					
					if(type == WorldElements.WORKER.getContent())
					{
						Worker w = new Worker(x, y);				
						world.getWorkerMap().put(w.getId(), w);
					}
					
					else if(type == WorldElements.HELPER.getContent())
					{
						Helper h = new Helper(x, y);
						world.getHelperMap().put(h.getId(), h);
					}
					
					else if(type == WorldElements.TRUCKER.getContent())
					{
						Truck t = new Truck(x, y);
						world.getTruckMap().put(t.getId(), t);
					}
					
					else if(type == WorldElements.GARAGE.getContent())
					{
						Artifact g = new Artifact(x, y, WorldElements.GARAGE);
						world.getGarageMap().put(g.getId(), g);
					}
					
					else if(type == WorldElements.RECHARGE_POINT.getContent())
					{
						Artifact r = new Artifact(x, y, WorldElements.RECHARGE_POINT);
						world.getRechargeMap().put(r.getId(), r);
					}
					
					else if(type == WorldElements.DEPOT.getContent())
					{
						Artifact d = new Artifact(x, y, WorldElements.DEPOT);
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
			int height = lines.size();
			
			world.setLayout(new GridLayout(width, height));
			
			for(int i = 0; i < height; i++)
			{
				String cells[] = lines.get(i).split(" ");
				
				for(int j = 0; j < width; j++)
					world.getLayout().getMatrix()[i][j] = cells[j].charAt(0);
			}
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("An error has occurred to open the file.");
			e.printStackTrace();
		}
	}
}