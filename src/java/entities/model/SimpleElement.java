package entities.model;

import java.util.concurrent.atomic.AtomicInteger;
import jason.environment.grid.Location;

/*
 * This class implements the main components of an agent.
 * It is inherited by the Worker, Helper, and Truck classes.
 * @see{Worker, Helper, and Truck}
 */

public abstract class SimpleElement 
{
	private static AtomicInteger seqId = new AtomicInteger();

	protected Integer id;
	protected String name;
	protected Location pos;
	protected Boolean visible;
	
	public SimpleElement(Integer posX, Integer posY) 
	{
		this.id = seqId.getAndIncrement() + 1;
		this.pos = new Location(posX, posY);
		this.name = null;
		this.visible = false;
	}
	
	public abstract void setProperties();

	public Integer getId() 
	{
		return id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public Location getPos() 
	{
		return pos;
	}

	public void setPos(Location pos) {
		this.pos.x = pos.x;
		this.pos.y = pos.y;
	}

	public Boolean isVisible() 
	{
		return visible;
	}

	public void setVisible(Boolean visible) 
	{
		this.visible = visible;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleElement other = (SimpleElement) obj;
		if (id == null) 
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() 
	{
		return "id=" + id + ", name=" + name + ", visible=" + visible
				+ ", posX=" + pos.x + "posX=" + pos.y;
	}	
}