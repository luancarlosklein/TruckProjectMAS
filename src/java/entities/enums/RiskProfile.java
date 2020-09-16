package entities.enums;

import java.util.Random;

public enum RiskProfile 
{
	CONSERVATIVE(0.0, 0.2),
	MODERATE(0.25, 0.4),
	BALANCED(0.5, 0.7),
	AGGRESSIVE(0.75, 0.95);
	
	private Double riskLevel;
	
	private RiskProfile(Double minLevel, Double maxLevel) 
	{
		Random rand = new Random();
		this.riskLevel =  minLevel + (maxLevel - minLevel) * rand.nextDouble();
	}
	
	public double getRiskLevel()
	{
		return this.riskLevel;
	}
	
	public String getProfile()
	{
		return this.name().toLowerCase();
	}
}
