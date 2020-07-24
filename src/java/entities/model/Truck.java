package entities.model;

public class Truck extends SimpleAgent
{
	private Integer qtdThings;		//Qtd of boxes inside of the truck
	
	public Truck(Integer posX, Integer posY, Integer qtdThings) 
	{
		super(posX, posY);
		this.qtdThings = qtdThings;
	}
	
	public Truck(Integer posX, Integer posY) 
	{
		super(posX, posY);
	}

	public Integer getQtdThings() 
	{
		return qtdThings;
	}

	public void setQtdThings(Integer qtdThings) 
	{
		this.qtdThings = qtdThings;
	}

	@Override
	public String toString() 
	{
		return "Truck [" + super.toString() + ", qtdThings=" + qtdThings + "]";
	}
}
