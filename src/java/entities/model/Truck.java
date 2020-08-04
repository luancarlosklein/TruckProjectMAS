package entities.model;

public class Truck extends SimpleElement
{
	private Integer qtdThings;		//Qtd of boxes inside of the truck
	
	public Truck(Integer posX, Integer posY, Integer qtdThings) 
	{
		super(posX, posY);
		this.qtdThings = qtdThings;
		this.setName("truck_" + id);
	}
	
	public Truck(Integer posX, Integer posY) 
	{
		super(posX, posY);
		this.qtdThings = 0;
		this.setName("truck_" + id);
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
