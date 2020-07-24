package entities.model;

import entities.services.LoadMapFromFile;
import entities.services.RandomMapGeneratorVisitor;

public class Test {

	public static void main(String[] args) {
		Worker work1 = new Worker(10, 5, 10, 0.6, 0.3, 10);
		Worker work2 = new Worker(10, 2, 8, 0.8, 0.2, 10);
		Worker work3 = new Worker(10, 3, 9, 1.0, 0.4, 10);
		
		Helper helper1 = new Helper(3, 4, 10);
		Helper helper2 = new Helper(3, 2, 5);
		Helper helper3 = new Helper(3, 3, 10);
		
		Truck truck1 = new Truck(4, 5, 100);
		Truck truck2 = new Truck(6, 8, 150);
		
		Map map = new Map(10, 10);
		
		RandomMapGeneratorVisitor vistior = new RandomMapGeneratorVisitor(3, 3, 3, 1, 2);
		map.accept(vistior);
		
		System.out.println(work1);
		System.out.println(work2);
		System.out.println(work3);
		System.out.println(helper1);
		System.out.println(helper2);
		System.out.println(helper3);
		System.out.println(truck1);
		System.out.println(truck2);
		System.out.println(map);
	}

}