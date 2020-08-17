package entities.model;

import java.util.Random;

public class Worker extends SimpleElement
{	
	private WorkerType specialization;
	
	public Worker(Integer posX, Integer posY) 
	{
		super(posX, posY);
		this.setName("worker_" + id);
		
		Random rand = new Random();
		
		switch (rand.nextInt(3)) 
		{
			case 0:
				this.specialization = WorkerType.FRAGILE_SPECIALIZATION;
				break;
			case 1:
				this.specialization = WorkerType.COMMON_SPECIALIZATION;
				break;
			default:
				this.specialization = WorkerType.DUAL_SPECIALIZATION;
		}			
	}
	
	public WorkerType getSpecialization() 
	{
		return specialization;
	}

	public void setSpecialization(WorkerType specialization) 
	{
		this.specialization = specialization;
	}

	@Override
	public String toString() 
	{
		return "Worker ["+ super.toString() +"specialization=" + specialization + "]";
	}
}