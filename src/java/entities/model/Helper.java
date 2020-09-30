package entities.model;

import entities.enums.HelperExpertiseClass;

public class Helper extends SimpleElement
{
	private Integer capacity; 		// Maximum weight that agent can carry
	private Long velocity;			// Velocity of agent. It represents how much time the help spends to give a step.
	private Double battery;			// Amount of energy of helper. The helper uses its energy for moving himself and taking boxes.
	private Double energyCost;		// Cost to move boxes from truck to depot.
	private Double dexterity;		// Skill in performing a task, in this case, don't let the boxes fall down.
	private Double failureProb;		// Probability of failure after the safety count arrives at 0.
	private Integer safety;			// Number of operations done by helper without failures (safety operations).
	
	public Helper(Integer posX, Integer posY) 
	{
		super(posX, posY);
		this.capacity = 0;
		this.velocity = 0L;
		this.battery = 0.0;
		this.energyCost = 0.0;
		this.dexterity = 0.0;
		this.failureProb = 0.0;
		this.safety = 0;
		this.setName("helper_" + id);
	}
	
	@Override
	public void setProperties() {}

	public Integer getCapacity() 
	{
		return capacity;
	}

	public void setCapacity(Integer capacity) 
	{
		this.capacity = capacity;
	}

	public Long getVelocity() 
	{
		return velocity;
	}

	public void setVelocity(Long velocity) 
	{
		this.velocity = velocity;
	}

	public Double getBattery() 
	{
		return battery;
	}

	public void setBattery(Double battery) 
	{
		this.battery = battery;
	}

	public Double getEnergyCost() 
	{
		return energyCost;
	}

	public void setEnergyCost(Double energyCost) 
	{
		this.energyCost = energyCost;
	}

	public Double getDexterity() 
	{
		return dexterity;
	}

	public void setDexterity(Double dexterity) 
	{
		this.dexterity = dexterity;
	}

	public Double getFailureProb() 
	{
		return failureProb;
	}

	public void setFailureProb(Double failureProb) 
	{
		this.failureProb = failureProb;
	}

	public Integer getSafety() 
	{
		return safety;
	}

	public void setSafety(Integer safetyCount) 
	{
		this.safety = safetyCount;
	}
	
	public HelperDurabilityClass getDurabilityClass()
	{
		if (this.failureProb > 0.7 && this.failureProb <= 1.0)
			return HelperDurabilityClass.LOW_DURABILITY;
		else if (this.failureProb > 0.4 && this.failureProb <= 0.7)
			return HelperDurabilityClass.MIDDLE_DURABILITY;
		else
			return HelperDurabilityClass.HIGH_DURABILITY;
	}
	
	public HelperExpertiseClass getExpertiseClass()
	{
		if (this.dexterity < 0.4)
			return HelperExpertiseClass.LOW_EXPERTISE;
		else if (this.dexterity >= 0.4 && this.dexterity < 0.7)
			return HelperExpertiseClass.MIDDLE_EXPERTISE;
		else
			return HelperExpertiseClass.HIGH_EXPERTISE;
	}
	
	@Override
	public String toString() 
	{
		return "Helper ["+ super.toString() + ", capacity=" + capacity 
				+ ", velocity=" + velocity + ", battery=" + battery 
				+ ", energyCost=" + energyCost + ", dexterity=" + dexterity 
				+ ", failureProb=" + failureProb + ", safetyCount=" + safety + "]";
	}
}