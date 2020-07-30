package entities.model;

public enum Constants 
{
	INFINITE(9999),
	PLACEMENT_ATTEMPTS(1000);
	
	public int value;
	
	private Constants(int value) 
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return this.value;
	}
}
