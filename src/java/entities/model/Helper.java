package entities.model;

public class Helper extends SimpleElement
{
	private Integer capacity; 		// Maximum weight that agent can carry
	private Double energyCost;		// Cost to move boxes from truck to depot
	private Double failureProb;		// Probability of failure after the safety count arrives at 0
	private Integer safetyCount;	// Number of operations done by helper without failures (safety operations)
	
	public Helper(Integer posX, Integer posY, Integer capacity) 
	{
		super(posX, posY);
		this.capacity = capacity;
		this.energyCost = 0.0;
		this.failureProb = 0.0;
		this.safetyCount = 0;
		this.setName("helper_" + id);
	}
	
	public Helper(Integer posX, Integer posY) 
	{
		super(posX, posY);
		this.capacity = 0;
		this.energyCost = 0.0;
		this.failureProb = 0.0;
		this.safetyCount = 0;
		this.setName("helper_" + id);
	}

	public Integer getCapacity() 
	{
		return capacity;
	}

	public void setCapacity(Integer capacity) 
	{
		this.capacity = capacity;
	}

	public Double getEnergyCost() 
	{
		return energyCost;
	}

	public void setEnergyCost(Double energyCost) 
	{
		this.energyCost = energyCost;
	}

	public Double getFailureProb() 
	{
		return failureProb;
	}

	public void setFailureProb(Double failureProb) 
	{
		this.failureProb = failureProb;
	}

	public Integer getSafetyCount() 
	{
		return safetyCount;
	}

	public void setSafetyCount(Integer safetyCount) 
	{
		this.safetyCount = safetyCount;
	}

	@Override
	public String toString() 
	{
		return "Helper ["+ super.toString() + ", capacity=" + capacity + "]";
	}
}