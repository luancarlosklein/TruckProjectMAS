package entities.enums;

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

public enum WorldElements 
{
	WALL('#'),
	PASSAGE('*'),
	WORKER('W'),
	HELPER('H'),
	TRUCKER('T'),
	GARAGE('G'),
	RECHARGE_POINT('R'),
	DEPOT('D');
	
	public char content;
	
	private WorldElements(char content) 
	{
		this.content = content;
	}
	
	public char getContent()
	{
		return this.content;
	}
}