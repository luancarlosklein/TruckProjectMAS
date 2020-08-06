package entities.model;

/**
 * This class implements the logic matrix that defines the placement of agents.
 */

public class MapPlacing extends Map<Character> 
{	
	// Constructor
	public MapPlacing(Integer width, Integer length) 
	{
		super(width, length);
		this.matrix = new Character[width][length];
	}
	
	/**
	 * This method implements the interface {@see: MapVisitor}.
	 * @param visitor: a visitor operation. 
	 */
    public void accept(MapVisitor visitor) 
    {
        visitor.visit(this);
    }
	
	public Character[][] getMatrix()
	{
		return matrix;
	}
	
	public boolean isObstacle(int x, int y)
	{
		return this.matrix[x][y] == MapElements.WALL.content;
	}
}