package entities.model;

import entities.enums.WorldElements;

/**
 * This class implements a simple artifact, an object which is used by agents.
 */
public class Artifact extends SimpleElement
{
	private Boolean actived;
	
	public Artifact(Integer posX, Integer posY, WorldElements type) 
	{
		super(posX, posY);
		this.actived = true;
		
		switch (type)
		{
			case GARAGE:
				this.name = "garage_" + this.getId();
				break;
				
			case RECHARGE_POINT:
				this.name = "recharge_" + this.getId();
				break;
				
			case DEPOT:
				this.name = "depot_" + this.getId();
				break;
				
			default:
				throw new Error("Type of artifact is not valid!");
		}
	}
	
	@Override
	public void setProperties() {}

	public Boolean getActived() 
	{
		return actived;
	}

	public void setActived(Boolean actived) 
	{
		this.actived = actived;
	}

	@Override
	public String toString() 
	{
		return "Artifact [actived=" + actived + ", id=" + id + ", name=" + name + ", pos=" + pos + "]";
	}
}
