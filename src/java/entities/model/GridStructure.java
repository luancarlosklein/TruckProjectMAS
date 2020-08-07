package entities.model;

public abstract class GridStructure<T> 
{
	protected Integer width;
	protected Integer height;
	protected T matrix[][];
	
	public GridStructure(Integer width, Integer height) {
		super();
		this.width = width;
		this.height = height;
		this.matrix = null;
	}

	public Integer getWidth()
	{
		return width;
	}
	
	public Integer getHeight()
	{
		return height;
	}
	
	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("Grid [width=" + width);
		sb.append(", height=" + height).append("]\n");
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				sb.append(matrix[y][x]).append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}