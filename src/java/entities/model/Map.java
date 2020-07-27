package entities.model;

/**
 * This class implements the logic matrix used in the placement of agents and artifacts.
 */

public class Map 
{
	private Integer width;
	private Integer length;
	private char matrix[][];
	
	// Constructor
	public Map(Integer width, Integer length) 
	{
		this.width = width;
		this.length = length;
		matrix = new char[width][length];
	}
	
	/**
	 * Interface used by design pattern Visitor.
	 * The idea is separating the operations of the class  into service operations.
	 * @param MapVisitor: an operation that operates over the map. 
	 * @throws Exception 
	 */
    public void accept(MapVisitor visitor) throws Exception 
    {
        visitor.visit(this);
    }

	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("Map [width=" + width);
		sb.append(", length=" + length).append("]\n");
		
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < length; j++)
			{
				sb.append(matrix[i][j]).append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public Integer getWidth()
	{
		return width;
	}
	
	public Integer getLength()
	{
		return length;
	}
	
	public char[][] getMatrix()
	{
		return matrix;
	}
}