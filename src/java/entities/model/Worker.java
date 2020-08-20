package entities.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;

public class Worker extends SimpleElement
{	
	private WorkerType specialization;
	private Map<Integer, HelperTeam> teams;
	
	public Worker(Integer posX, Integer posY) 
	{
		super(posX, posY);
		this.setName("worker_" + id);
		this.teams = new HashMap<Integer, HelperTeam>();
		
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
	
	/**
	 * Check if a team has already been registered.
	 * @param teamId: id of team.
	 * @return true, if the team exists, otherwise returns false.
	 */
	public boolean containsTeam(int teamId)
	{
		return teams.containsKey(teamId);
	}
	
	/**
	 * Add a team to the map of teams.
	 * @param team: helper team to be added.
	 */
	public void addTeam(HelperTeam team) throws Exception
	{
		if(teams.containsKey(team.getId()))
			throw new Exception("A team has already registered with this: " + team.getId());
		else
			teams.put(team.getId(), team);
	}
	
	/**
	 * Remove a team of map of teams.
	 * @param teamId: id of team that will be removed.
	 */
	public void removeTeam(int teamId) throws Exception
	{
		if(teams.containsKey(teamId))
			teams.remove(teamId);
		else
			throw new IllegalAccessError("It was not found a team with this id: " + teamId);
	}
	
	/**
	 * Add a helper to a team.
	 * @param teamId: id of team which the helper will be added.
	 * @param helper: the helper to be added.
	 */
	public void addHelperToTeam(int teamId, Helper helper) throws Exception
	{
		if(teams.containsKey(teamId))
		{
			HelperTeam team = teams.get(teamId);
			
			if(!team.addHelper(helper))
				throw new Exception("It is not possible to add the helper, the team is complete.");
		}
		else
			throw new IllegalAccessError("It was not found a team with this id: " + teamId);
	}

	/**
	 * Remove a helper from a team.
	 * @param teamId: id of team which the helper will be removed.
	 * @param helper: the helper to be removed.
	 */
	public void removeHelperFromTeam(int teamId, Helper helper) throws Exception
	{
		if(teams.containsKey(teamId))
		{
			HelperTeam team = teams.get(teamId);
			
			if(!team.removeHelper(helper))
				throw new Error("It was not possible to remove the helper.");
		}
		else
			throw new IllegalAccessError("It was not found a team with this id: " + teamId);
	}
	
	/**
	 * Check if a given team is complete.
	 * @param teamId: id of team that will be checked.
	 * @return true, if the team is complete.
	 */
	public boolean teamIsComplete(int teamId) throws Exception
	{
		if(teams.containsKey(teamId))
			return teams.get(teamId).teamIsComplete();
		else
			throw new IllegalAccessError("It was not found a team with this id: " + teamId);
	}
	
	/**
	 * Check if the team is ready to work.
	 * @return true, if all member are hired, otherwise, false.
	 */
	public boolean teamIsReady(int teamId)
	{
		if(teams.containsKey(teamId))
			return teams.get(teamId).teamIsReady();
		else
			throw new IllegalAccessError("It was not found a team with this id: " + teamId);
	}
	
	/**
	 * Generate a list of names of members of a team.
	 * @param teamId: id of team that will be checked.
	 * @return the names of members of team.
	 */
	public List<String> getTeamMemberNames(int teamId) throws Exception
	{
		if(teams.containsKey(teamId))
			return teams.get(teamId).getMemberNames();
		else
			throw new IllegalAccessError("It was not found a team with this id: " + teamId);
	}

	/**
	 * Return the names of team's members as a list of terms.
	 * @param teamId: id of team that will be checked.
	 * @return a list of members.
	 */
	public ListTerm getMembersAsTermList(int teamId) throws Exception
	{
		ListTerm terms = new ListTermImpl();
		
		if(teams.containsKey(teamId))
		{	
			for(String name : teams.get(teamId).getMemberNames())
				terms.add(new Atom(name));
			
			return terms;
		}
		else
			throw new IllegalAccessError("It was not found a team with this id: " + teamId);
	}
	
	public void showTeam(int teamId)
	{
		if(teams.containsKey(teamId))
			teams.get(teamId).showTeam();
		else
			throw new IllegalAccessError("It was not found a team with this id: " + teamId);
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