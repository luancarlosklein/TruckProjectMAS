package entities.model;

import entities.enums.WorldElements;

/**
 * This class implements the logic matrix that defines the placement of agents.
 */

public class GridLayout extends GridStructure<Character> 
{	
	// Constructor
	public GridLayout(Integer width, Integer height) 
	{
		super(width, height);
		this.matrix = new Character[height][width];
	}
	
	/**
	 * This method implements the interface {@see: MapVisitor}.
	 * @param visitor: a visitor operation. 
	 */
    public void accept(GridVisitor visitor) 
    {
        visitor.visit(this);
    }
	
	public Character[][] getMatrix()
	{
		return matrix;
	}
	
	public boolean isObstacle(int x, int y)
	{
		return this.matrix[y][x] == WorldElements.WALL.content;
	}
}