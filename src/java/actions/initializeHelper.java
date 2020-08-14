// Internal action code for project discharge_truck

package actions;

import java.util.Map;

import entities.model.Artifact;
import entities.model.Helper;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class initializeHelper extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();
	private Map<Integer, Artifact> garageMap = DischargeEnv.model.getWorld().getGarageMap();
	private Map<Integer, Artifact> rechargeMap = DischargeEnv.model.getWorld().getRechargeMap();
	
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: helper's name
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Helper h = helperMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	
    	ts.getAg().addBel(Literal.parseLiteral("pos(" + h.getPos().x + "," + h.getPos().y +")"));
    	ts.getAg().addBel(Literal.parseLiteral("capacity(" + h.getCapacity() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("velocity(" + h.getVelocity() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("battery(" + h.getBattery() + ")"));    	
    	ts.getAg().addBel(Literal.parseLiteral("energy_cost(" + h.getEnergyCost() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("failure_prob(" + h.getFailureProb() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("safety_count(" + h.getSafetyCount() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("dexterity(" + h.getDexterity() +")"));
    	
    	for(Artifact g: garageMap.values())
    		ts.getAg().addBel(Literal.parseLiteral("garage(" + g.getName() +")"));
    	
    	for(Artifact r: rechargeMap.values())
    		ts.getAg().addBel(Literal.parseLiteral("recharge(" + r.getName() +")"));
    		
        return true;
    }
}