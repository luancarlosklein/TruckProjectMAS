package entities.model;

/**
 * This class implements a simple tuple(x, y).
 * It is used to represent a cell of the world.
 */

public class Position 
{
	private Integer x;
	private Integer y;
	
	public Position(Integer x, Integer y) 
	{
		this.x = x;
		this.y = y;
	}

	public Integer getX() 
	{
		return x;
	}

	public void setX(Integer x) 
	{
		this.x = x;
	}

	public Integer getY() 
	{
		return y;
	}

	public void setY(Integer y) 
	{
		this.y = y;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
}
