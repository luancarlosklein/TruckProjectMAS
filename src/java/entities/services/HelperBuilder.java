package entities.services;

import java.util.Random;

import entities.enums.HelperExpertiseClass;
import entities.model.Helper;
import entities.model.HelperDurabilityClass;

/*
 * This class implements the Design Pattern Builder for Helpers entities
 */

public class HelperBuilder 
{
	private Double dexterity;
	private Double failureProb;
	private Integer safety;
	
	public HelperBuilder(){}
	
	/*
	 * This method builds a Helper according with specification
	 * @param posX: position x of helper
	 * @param posY: position y of helper
	 * @param capacity: capacity of helper (number of boxes he can carry)
	 * @return a initialized helper
	 */
	public Helper build(int posX, int posY, int capacity, HelperExpertiseClass expertiseLevel, HelperDurabilityClass durabilityLevel)
	{
		// Default initialization
		Helper helper = new Helper(posX, posY);
		helper.setCapacity(capacity);
		helper.setBattery(1.0);
		
		// Fields initialization based on helper's capacity
		helper.setVelocity((long) (capacity * 100));		// Each extra box reduces the velocity at 100 milliseconds
		helper.setEnergyCost(capacity / 10.0);				// Each extra box increases the energy cost at 10%
		
		// Fields initialization based on the builder configuration
		setHelperProfile(expertiseLevel, durabilityLevel);
		
		helper.setDexterity(this.dexterity);
		helper.setFailureProb(this.failureProb);
		helper.setSafety(this.safety); 
		
		return helper;
	}
	
	/**
	 * This method initializes the levels of expertise and durability of a helper.
	 * @param expertiseLevel: helper's dexterity. It is associated with the helper's chance of dropping a box.
	 * @param durabilityLevel: helper's durability. The helper's fault tolerance.
	 */
	private void setHelperProfile(HelperExpertiseClass expertiseLevel, HelperDurabilityClass durabilityLevel)
	{
		Random rand = new Random();
		
		switch (expertiseLevel) 
		{
			// The level of expertise of helper is between 0% and 39%. 
			case LOW_EXPERTISE:
				this.dexterity = 0.0 + (0.4 - 0.0) * rand.nextDouble();
				break;
			
			// The level of expertise of helper is between 40% and 69%.
			case MIDDLE_EXPERTISE:
				this.dexterity = 0.4 + (0.7 - 0.4) * rand.nextDouble();
				break;
			
				// The level of expertise of helper is between 70% and 100%.
			case HIGH_EXPERTISE:
				this.dexterity = 0.7 + (1.0 - 0.7) * rand.nextDouble();
				break;
	
			default:
				throw new Error("The helper's expertise class is invalid.");
		}
		
		switch (durabilityLevel) 
		{
			/*
			 *  The failure probability is between 70% and 100%.
			 *  The helper can perform 5 operations before a failure. 
			 */
			case LOW_DURABILITY:
				this.failureProb = 0.7 + (1.0 - 0.7) * rand.nextDouble();
				this.safety = 5;
				break;
			
			/*
			 *  The failure probability is between 40% and 69%.
			 *  The helper can perform 10 operations before a failure. 
			 */
			case MIDDLE_DURABILITY:
				this.failureProb = 0.4 + (7.0 - 0.4) * rand.nextDouble();
				this.safety = 10;
				break;
				
			/*
			 *  The failure probability is between 0% and 39%.
			 *  The helper can perform 15 operations before a failure. 
			 */
			case HIGH_DURABILITY:
				this.failureProb = 0.0 + (4.0 - 0.0) * rand.nextDouble();
				this.safety = 15;
				break;

			default:
				throw new Error("The helper's durability class is invalid.");
		}
	}
}