package entities.model;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SimpleAgent 
{
	private static AtomicInteger seqId = new AtomicInteger();

	private Integer id;
	private String name;
	private Position pos;
	
	public SimpleAgent(Integer posX, Integer posY) 
	{
		super();
		this.id = seqId.getAndIncrement();
		this.pos = new Position(posX, posY);
		this.name = null;
	}

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

	public Integer getPosX() 
	{
		return pos.getX();
	}

	public void setPosX(Integer posX) 
	{
		this.pos.setX(posX);
	}

	public Integer getPosY() 
	{
		return pos.getY();
	}

	public void setPosY(Integer posY) 
	{
		this.pos.setY(posY);
	}

	@Override
	public String toString() 
	{
		return "id=" + id + ", name=" + name + ", posX=" 
				+ pos.getX() + "posX=" + pos.getY();
	}
	
	
}
