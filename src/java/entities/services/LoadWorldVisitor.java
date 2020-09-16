package entities.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.enums.WorldElements;
import entities.model.Artifact;
import entities.model.GridLayout;
import entities.model.Helper;
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
					WorldElements type = WorldElements.valueOf(fields[0]);
					
					int y = Integer.parseInt(fields[1]);
					int x = Integer.parseInt(fields[2]);
					
					switch (type) 
					{
						case WORKER:
							Worker w = new Worker(x, y);				
							world.getWorkerMap().put(w.getId(), w);	
							break;

						case HELPER:
							Helper h = new Helper(x, y);
							h.setBattery(Double.parseDouble(fields[3]));
							h.setEnergyCost(Double.parseDouble(fields[4]));
							h.setCapacity(Integer.parseInt(fields[5]));
							h.setVelocity(Long.parseLong(fields[6]));
							h.setDexterity(Double.parseDouble(fields[7]));
							h.setFailureProb(Double.parseDouble(fields[8]));
							h.setSafety(Integer.parseInt(fields[9]));
							world.getHelperMap().put(h.getId(), h);
							break;
						
						case TRUCKER:
							Truck t = new Truck(x, y);
							world.getTruckMap().put(t.getId(), t);
							break;
						
						case GARAGE:
							Artifact g = new Artifact(x, y, WorldElements.GARAGE);
							world.getGarageMap().put(g.getId(), g);
							break;
							
						case RECHARGE_POINT:
							Artifact r = new Artifact(x, y, WorldElements.RECHARGE_POINT);
							world.getRechargeMap().put(r.getId(), r);
							break;
							
						case DEPOT:
							Artifact d = new Artifact(x, y, WorldElements.DEPOT);
							world.getDepotsMap().put(d.getId(), d);
							break;
						default:
							throw new Exception("Element is not valid: " + type);
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}