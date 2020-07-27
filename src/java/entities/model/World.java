package entities.model;

import entities.services.LoadWorldFromFile;
import entities.services.PathCreationVisitor;
import entities.services.RandomPlacingVisitor;
import entities.services.SaveWorldInFile;

/**
 * This class implements the world of agents.
 * This class is Java side for the Jason application.
 * Therefore, each agent described herein corresponds to an agent in Jason.
 */

public class World 
{	
	private Map map;
	private Worker[] workers;
	private Helper[] helpers;
	private Truck[] truckers;
	private Position[] garages;
	private Position[] rechargeStops;
	
	/**
	 * This constructor creates a random world.
	 * The map is created randomly.
	 * The agents are placed on the map randomly.
	 * @param width: the width of map.
	 * @param length: the length of map.
	 * @throws Exception 
	 */
	public World(Integer width, Integer length) throws Exception 
	{
		map = new Map(width, length);
		map.accept(new PathCreationVisitor());
		
		workers = new Worker[3];
		helpers = new Helper[3];
		truckers = new Truck[3];
		garages = new Position[1];
		rechargeStops = new Position[2];
		map.accept(new RandomPlacingVisitor(workers, helpers, truckers, garages, rechargeStops));
		
		SaveWorldInFile.save(map);
	}
	
	/**
	 * This constructor creates a world from an input file.
	 * @param fileName: name of input file.
	 */
	public World(String fileName) 
	{
		LoadWorldFromFile loader = new LoadWorldFromFile();
		loader.load(fileName);
		
		map = loader.getMap();
		workers = loader.getWorkes();
		helpers = loader.getHelpers();
		truckers = loader.getTruckers();
		garages = loader.getGarages();
		rechargeStops = loader.getRechargeStops();
	}
	
	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer();
		
		for(Worker w : workers)
			sb.append(w).append("\n");
		
		for(Helper h : helpers)
			sb.append(h).append("\n");
		
		for(Truck t : truckers)
			sb.append(t).append("\n");
		
		for(Position g : garages)
			sb.append(g).append("\n");
		
		for(Position r : rechargeStops)
			sb.append(r).append("\n");
		
		sb.append(map);
		return sb.toString();
	}
}