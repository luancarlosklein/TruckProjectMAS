package entities.model;

/**
 * This class specifies the elements that can be put on a maze.
 * '*': free position (PASSAGE).
 * '#': wall
 * 'W': worker
 * 'H': helper
 * 'T': truck
 * 'G': garage
 * 'R': recharge point
 */

public enum MazeElements 
{
	WALL('#'),
	PASSAGE('*'),
	WORKER('W'),
	HELPER('H'),
	TRUCKER('T'),
	GARAGE('G'),
	RECHARGE_POINT('R'),
	DEPOT('D');
	
	private char content;
	
	private MazeElements(char content) 
	{
		this.content = content;
	}
	
	public char getContent()
	{
		return this.content;
	}
}
