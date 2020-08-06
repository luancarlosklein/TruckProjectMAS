package entities.model;

/*
 * This file is used to test temporary implementations.
 * In the final version of this project, this file must be deleted.
 */

public class Test {

	public static void main(String[] args) 
	{
		try 
		{
			World w1 = new World(20, 20);
			World w2 = new World();
			
			System.out.println(w1);
			System.out.println(w2);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}