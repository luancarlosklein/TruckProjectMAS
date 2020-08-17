// Internal action code for project discharge_truck

package actions;

import java.util.Map;

import entities.model.Artifact;
import entities.model.Worker;
import environments.DischargeEnv;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class initializeWorker extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;
	private Map<Integer, Worker> workerMap = DischargeEnv.model.getWorld().getWorkerMap();
	private Map<Integer, Artifact> depotMap = DischargeEnv.model.getWorld().getDepotsMap();
	/**
	 * Action's arguments (from args parameter):
	 * args[0]: worker's name
	 */
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	Worker w = workerMap.get(Integer.parseInt(args[0].toString().split("_")[1]));
    	
    	ts.getAg().addBel(Literal.parseLiteral("pos(" + w.getPos().x + "," + w.getPos().y +")"));
    	ts.getAg().addBel(Literal.parseLiteral("specialization(" + w.getSpecialization().name().toLowerCase() +")"));
    
    	for(Artifact d: depotMap.values())
    		ts.getAg().addBel(Literal.parseLiteral("depot(" + d.getName() +")"));
		
        return true;
    }
}