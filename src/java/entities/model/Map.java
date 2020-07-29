package entities.model;

public abstract class Map<T> 
{
	protected Integer width;
	protected Integer length;
	protected T matrix[][];
	
	public Map(Integer width, Integer length) {
		super();
		this.width = width;
		this.length = length;
		this.matrix = null;
	}

	public Integer getWidth()
	{
		return width;
	}
	
	public Integer getLength()
	{
		return length;
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