package entities.enums;

public enum Constants 
{
	INFINITE(9999),
	PLACEMENT_ATTEMPTS(1000),
	WORKER(1),
	HELPER(2),
	TRUCK(4),
	GARAGE(8),
	DEPOT(16),
	RECHARGE(32);
	
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
