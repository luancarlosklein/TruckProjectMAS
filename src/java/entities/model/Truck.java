package entities.model;

import java.util.Random;

public class Truck extends SimpleElement
{
	private Integer qtdThings;		// Amount of boxes inside of the truck
	private Boolean discharged;		// Informs when the truck is discharge
	private CargoType cargoType;	// Defines the type of cargo transported by the truck
	private Boolean visible;		// Defines when the truck will be shown on the screen
	
	public Truck(Integer posX, Integer posY, Integer qtdThings, CargoType cargoType) 
	{
		super(posX, posY);
		this.qtdThings = qtdThings;
		this.setName("truck_" + id);
		this.cargoType = cargoType;
		this.discharged = qtdThings <= 0;
		this.visible = true;
	}
	
	public Truck(Integer posX, Integer posY) 
	{	
		super(posX, posY);
		this.qtdThings = 0;
		this.setName("truck_" + id);
		this.visible = true;
		this.cargoType = CargoType.COMMON;
		
		Random rand = new Random();
		
		if(rand.nextBoolean())
			this.cargoType = CargoType.FRAGILE;
	}

	public Integer getQtdThings() 
	{
		return qtdThings;
	}

	public void setQtdThings(Integer qtdThings) 
	{
		this.qtdThings = qtdThings;
		this.discharged = qtdThings <= 0;
	}

	public Boolean isDischarged() 
	{
		return discharged;
	}

	public CargoType getCargoType() 
	{
		return cargoType;
	}

	public void setCargoType(CargoType cargoType) 
	{
		this.cargoType = cargoType;
	}
	
	public Boolean getVisible() 
	{
		return visible;
	}

	public void setVisible(Boolean visible) 
	{
		this.visible = visible;
	}

	@Override
	public String toString() 
	{
		return "Truck [" + super.toString() + ", qtdThings=" + qtdThings + ", discharged=" + discharged + ", cargoType=" + cargoType + "]";
	}
}
