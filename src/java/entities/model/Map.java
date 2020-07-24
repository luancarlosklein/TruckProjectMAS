package entities.model;

/**
 * This class implements the logic matrix used to allocate the agents and artifacts.
 * This logic matrix represents the world where the agents are inserted on.
 */

public class Map 
{
	private Integer width;
	private Integer length;
	private char matrix[][];
	
	public Map(Integer width, Integer length) 
	{
		this.width = width;
		this.length = length;
		matrix = new char[width][length];
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < length; j++)
				matrix[i][j] = MazeElements.WALL.getContent();
	}

	public Integer getWidth(){return width;}
	public Integer getLength(){return length;}
	public char[][] getMatrix(){return matrix;}
	
	/**
	 * Interface used by the design pattern Visitor.
	 * The idea is separating model and operations.
	 */
    public void accept(MapVisitor visitor) 
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
}
