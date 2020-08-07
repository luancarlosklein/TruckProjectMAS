package entities.model;

/**
 * This class implements the logic matrix that represents the routes computed by LRTA*.
 */

public class GridRoutes extends GridStructure<Integer> 
{	
	// Constructor
	public GridRoutes(Integer width, Integer height) 
	{
		super(width, height);
		this.matrix = new Integer[height][width];
	}
	
	/**
	 * This method implements the interface {@see: MapVisitor}.
	 * @param visitor: a visitor operation. 
	 */
    public void accept(GridVisitor visitor) 
    {
        visitor.visit(this);
    }
	
	public Integer[][] getMatrix()
	{
		return matrix;
	}
}