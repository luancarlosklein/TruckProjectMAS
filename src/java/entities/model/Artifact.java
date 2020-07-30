package entities.model;

/**
 * This class implements a simple artifact, an object which is used by agents.
 */
public class Artifact extends SimpleElement
{
	private Boolean actived;
	
	public Artifact(Integer posX, Integer posY, MapElements type) 
	{
		super(posX, posY);
		this.actived = true;
		
		switch (type)
		{
			case GARAGE:
				this.name = "Garage";
				break;
				
			case RECHARGE_POINT:
				this.name = "Recharge";
				break;
				
			case DEPOT:
				this.name = "Depot";
				break;
				
			default:
				throw new Error("Type of artifact is not valid!");
		}
	}

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
