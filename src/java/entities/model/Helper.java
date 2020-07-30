package entities.model;

public class Helper extends SimpleElement
{
	private Integer capacity; 		// Maximum weight that agent can carry
	
	public Helper(Integer posX, Integer posY, Integer capacity) 
	{
		super(posX, posY);
		this.capacity = capacity;
	}
	
	public Helper(Integer posX, Integer posY) 
	{
		super(posX, posY);
		this.capacity = 0;
	}

	public Integer getCapacity() 
	{
		return capacity;
	}

	public void setCapacity(Integer capacity) 
	{
		this.capacity = capacity;
	}

	@Override
	public String toString() 
	{
		return "Helper ["+ super.toString() + ", capacity=" + capacity + "]";
	}
}
