package entities.model;

/**
 * This class implements the logic matrix that represents the routes computed by LRTA*.
 */

public class MapRouting extends Map<Double> 
{	
	// Constructor
	public MapRouting(Integer width, Integer length) 
	{
		super(width, length);
		this.matrix = new Double[width][length];
	}
	
	/**
	 * This method implements the interface {@see: MapVisitor}.
	 * @param visitor: a visitor operation. 
	 */
    public void accept(MapVisitor visitor) 
    {
        visitor.visit(this);
    }
	
	public Double[][] getMatrix()
	{
		return matrix;
	}
}