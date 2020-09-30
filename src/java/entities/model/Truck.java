package entities.model;

import java.util.Random;

import entities.enums.CargoType;

public class Truck extends SimpleElement
{
	private Integer qtdThings;		// Amount of boxes inside of the truck
	private Boolean discharged;		// Informs when the truck is discharge
	private CargoType cargoType;	// Defines the type of cargo transported by the truck
	private Long unloadTime;		// The unloading time. How much time can be spend to unload the truck. 
	
	public Truck(Integer posX, Integer posY, Integer qtdThings, CargoType cargoType, Long unloadTime) 
	{
		super(posX, posY);
		this.qtdThings = qtdThings;
		this.setName("truck_" + id);
		this.cargoType = cargoType;
		this.discharged = qtdThings <= 0;
		this.unloadTime = unloadTime;
	}
	
	public Truck(Integer posX, Integer posY) 
	{	
		super(posX, posY);
		this.setName("truck_" + id);
		visible = false;
		unloadTime = 100000l;
		cargoType = CargoType.COMMON;
		
		Random rand = new Random();		
		if(rand.nextBoolean())
		{
			this.cargoType = CargoType.FRAGILE;
		}
		setQtdThings(rand.nextInt(15));
	}
	
	@Override
	public void setProperties() 
	{
		visible = false;
		unloadTime = 100000l;
		cargoType = CargoType.COMMON;
		
		Random rand = new Random();		
		if(rand.nextBoolean())
		{
			this.cargoType = CargoType.FRAGILE;
		}
		setQtdThings(rand.nextInt(15));
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

	public Long getUnloadTime() 
	{
		return unloadTime;
	}

	public void setUnloadTime(Long unloadTime) 
	{
		this.unloadTime = unloadTime;
	}

	@Override
	public String toString() 
	{
		return "Truck [" + super.toString() 
		+ ", qtdThings=" + qtdThings + ", discharged=" + discharged 
		+ ", cargoType=" + cargoType + ", unloadTime=" + unloadTime + "]";
	}
}
