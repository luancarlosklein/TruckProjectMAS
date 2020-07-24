package entities.model;

public class Worker extends SimpleAgent{

	private Integer capacity; 		// Maximum weight that agent can carry
	private Double battery; 		// Battery level (0% - 100%)
	private Double jumbled;			// Chance of agent drops the box on the floor (0% - 100%).
	private Integer qtdGoals;		// Number of goals of an agent (boxes to be to discharged)
	
	public Worker(Integer posX, Integer posY, Integer capacity, 
			Double battery, Double jumbled, Integer qtGoals) 
	{
		super(posX, posY);
		this.capacity = capacity;
		this.battery = battery;
		this.jumbled = jumbled;
		this.qtdGoals = qtGoals;
	}
	
	public Worker(Integer posX, Integer posY) 
	{
		super(posX, posY);
		this.capacity = 0;
		this.battery = 0.0;
		this.jumbled = 0.0;
		this.qtdGoals = 0;
	}

	public Integer getCapacity() 
	{
		return capacity;
	}

	public void setCapacity(Integer capacity) 
	{
		this.capacity = capacity;
	}

	public Double getBattery() 
	{
		return battery;
	}

	public void setBattery(Double battery) 
	{
		this.battery = battery;
	}

	public Double getJumbled() 
	{
		return jumbled;
	}

	public void setJumbled(Double jumbled) 
	{
		this.jumbled = jumbled;
	}

	public Integer getQtdGoals() 
	{
		return qtdGoals;
	}

	public void setQtdGoals(Integer qtdGoals) 
	{
		this.qtdGoals = qtdGoals;
	}

	@Override
	public String toString() 
	{
		return "Worker [" + super.toString() + ", capacity=" + capacity + ", battery=" + battery 
				+ ", jumbled=" + jumbled + ", qtdGoals=" + qtdGoals + "]";
	}
}