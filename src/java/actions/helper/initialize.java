package actions.helper;

import java.util.Map;
import entities.model.Artifact;
import entities.model.Helper;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

/**
 * This action initializes a helper.
 */
public class initialize extends DefaultInternalAction 
{
	private static final long serialVersionUID = 1L;
	private Map<Integer, Helper> helperMap = DischargeEnv.model.getWorld().getHelperMap();
	private Map<Integer, Artifact> garageMap = DischargeEnv.model.getWorld().getGarageMap();
	private Map<Integer, Artifact> rechargeMap = DischargeEnv.model.getWorld().getRechargeMap();
	
	/**
	 * Arguments (come from parameter args):
	 * @param args[0]: Helper's name
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Helper h = helperMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	
    	ts.getAg().addBel(Literal.parseLiteral("pos(" + h.getPos().x + "," + h.getPos().y +")"));
    	ts.getAg().addBel(Literal.parseLiteral("visible(" + h.isVisible() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("capacity(" + h.getCapacity() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("velocity(" + h.getVelocity() + ")"));
    	ts.getAg().addBel(Literal.parseLiteral("battery(" + h.getBattery() + ")"));    	
    	ts.getAg().addBel(Literal.parseLiteral("energy_cost(" + h.getEnergyCost() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("failure_prob(" + h.getFailureProb() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("safety(" + h.getSafety() +")"));
    	ts.getAg().addBel(Literal.parseLiteral("dexterity(" + h.getDexterity() +")"));
    	
    	for(Artifact g: garageMap.values())
    		ts.getAg().addBel(Literal.parseLiteral("garage(" + g.getName() +")"));
    	
    	for(Artifact r: rechargeMap.values())
    		ts.getAg().addBel(Literal.parseLiteral("recharge(" + r.getName() +")"));
    		
        return true;
    }
}