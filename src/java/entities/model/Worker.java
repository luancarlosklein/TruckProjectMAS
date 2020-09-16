package entities.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import entities.enums.RiskProfile;
import entities.enums.WorkerSpecialization;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;

/**
 * This class implements a worker.
 * A worker provider/offer a service for a trucker (unload service).
 * However, workers depend on helper to deliver the promised service to trucker.
 */

public class Worker extends SimpleElement
{	
	private RiskProfile riskProfile;
	private WorkerSpecialization specialization;
	private Map<Integer, HelperTeam> teams;
	
	public Worker(Integer posX, Integer posY) 
	{
		// Basic initialization
		super(posX, posY);
		this.setName("worker_" + super.id);
		this.teams = new HashMap<Integer, HelperTeam>();
		
		// Setting specialization and risk profiles
		defineSpecialization();
		defineRiskProfile();
	}
	
	/**
	 * Check if a team has already been registered.
	 * @param teamId: identifier of team.
	 * @return true, if the team exists, otherwise returns false.
	 */
	public boolean containsTeam(int teamId)
	{
		return teams.containsKey(teamId);
	}
	
	/**
	 * Add a team to the map of teams.
	 * @param team: a helper team which will be added into hash table.
	 * 		  The hash table stores the available teams at the moment.
	 */
	public void addTeam(HelperTeam team) throws Exception
	{
		if(teams.containsKey(team.getId()))
			throw new Exception("There is already a team registered with this id: " + team.getId());
		else
			teams.put(team.getId(), team);
	}
	
	/**
	 * Remove a team from the hash table. 
	 * @param teamId: identifier of team which will be removed from hash table.
	 */
	public void removeTeam(int teamId) throws Exception
	{
		if(teams.containsKey(teamId))
			teams.remove(teamId);
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Change the status of helper to hired.
	 * A hired helper is a helper who received a confirmation from a worker to perform a service.
	 * @param teamId: identifier of team where the helper is inserted.
	 * @param helper: the helper.
	 */
	public void hireHelper(int teamId, Helper helper)
	{
		if(teams.containsKey(teamId))
			teams.get(teamId).hireMember(helper);
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Add a helper to a team.
	 * @param teamId: identifier of team which the helper will be added.
	 * @param helper: the helper to be added.
	 */
	public void addHelperToTeam(int teamId, Helper helper) throws Exception
	{
		if(teams.containsKey(teamId))
		{
			HelperTeam team = teams.get(teamId);
			
			if(!team.addHelper(helper))
				throw new Exception("It is not possible to add the helper to team, the team is full.");
		}
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}

	/**
	 * Remove a helper from a team.
	 * @param teamId: identifier of team which the helper will be removed.
	 * @param helper: the helper to be removed.
	 */
	public void removeHelperFromTeam(int teamId, Helper helper) throws Exception
	{
		if(teams.containsKey(teamId))
		{
			HelperTeam team = teams.get(teamId);
			
			if(!team.removeHelper(helper))
				throw new Error("It was not possible to remove the helper: " + helper.getName());
		}
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Check if a given team is full (if there is more space to add new members).
	 * @param teamId: identifier of team that will be checked.
	 * @return true, if the team is full, otherwise, false.
	 */
	public boolean teamIsFull(int teamId) throws Exception
	{
		if(teams.containsKey(teamId))
			return teams.get(teamId).teamIsFull();
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Check if the team is ready to perform the service.
	 * A team is ready if only if all members of team were hire.
	 * @return true, if all members are hired, otherwise, false.
	 */
	public boolean teamIsReady(int teamId)
	{
		if(teams.containsKey(teamId))
			return teams.get(teamId).teamIsReady();
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Get the members of a team.
	 * @param teamId: identifier of team.
	 * @return a set of all member of the team.
	 */
	public Set<Helper> getTeamMembers(int teamId) throws Exception
	{
		if(teams.containsKey(teamId))
			return teams.get(teamId).getMembers();
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Get the names of members of a team.
	 * @param teamId: identifier of team.
	 * @return a set of all names of members of team.
	 */
	public Set<String> getTeamMemberNames(int teamId) throws Exception
	{
		if(teams.containsKey(teamId))
		{
			Set<String> memberNames = new HashSet<String>();
			
			for(Helper member : teams.get(teamId).getMembers())
				memberNames.add(member.getName());
			
			return memberNames;
		}
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}

	/**
	 * Get the names of members of team as a list of terms.
	 * @param teamId: identifier of team.
	 * @return a list of terms, where each term is a name of a helper.
	 */
	public ListTerm getTeamMembersAsTermList(int teamId) throws Exception
	{
		ListTerm terms = new ListTermImpl();
		
		if(teams.containsKey(teamId))
		{	
			for(Helper helper : teams.get(teamId).getMembers())
				terms.add(new Atom(helper.getName()));
			
			return terms;
		}
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Get the names of ready helpers (as a list of terms)
	 * @param teamId: identifier of team.
	 * @return a list of terms, where each term is a name of a ready helper.
	 */
	public ListTerm getReadyMembersAsTermList(int teamId) throws Exception
	{
		ListTerm terms = new ListTermImpl();
		
		if(teams.containsKey(teamId))
		{	
			for(Helper helper : teams.get(teamId).getReadyMembers())
				terms.add(new Atom(helper.getName()));
			
			return terms;
		}
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}

	/**
	 * Get the names of no ready helpers (as a list of terms)
	 * @param teamId: identifier of team.
	 * @return a list of terms, where each term is a name of a no ready helper.
	 */
	public ListTerm getNotReadyMembersAsTermList(int teamId) throws Exception
	{
		ListTerm terms = new ListTermImpl();
		
		if(teams.containsKey(teamId))
		{	
			for(Helper helper : teams.get(teamId).getNotReadyMembers())
				terms.add(new Atom(helper.getName()));
			
			return terms;
		}
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Define when a team is ready to perform the service.
	 * @param teamId: identifier of team.
	 */
	public void setTeamAsReady(int teamId)
	{
		if(teams.containsKey(teamId))
		{	
			teams.get(teamId).setReady(true);
		}
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Show the team members on the screen.
	 * @param teamId: identifier of team.
	 */
	public void showTeam(int teamId)
	{
		if(teams.containsKey(teamId))
			teams.get(teamId).showTeam();
		else
			throw new IllegalAccessError("There is not a team with this id: " + teamId);
	}
	
	/**
	 * Set the specialization of worker.
	 * A specialization defines the kind of service that a worker can offer. 
	 */
	private void defineSpecialization()
	{
		Random rand = new Random();
		
		switch (rand.nextInt(3)) 
		{
			case 0:
				this.specialization = WorkerSpecialization.FRAGILE_SPECIALIZATION;
				break;
				
			case 1:
				this.specialization = WorkerSpecialization.COMMON_SPECIALIZATION;
				break;
				
			default:
				this.specialization = WorkerSpecialization.DUAL_SPECIALIZATION;
		}
	}
	
	/**
	 * Set the risk profile of worker.
	 * The risk profile affects the decision taken by worker in uncertain situations. 
	 */
	private void defineRiskProfile()
	{
		Random rand = new Random();
		
		switch (rand.nextInt(4)) 
		{
			case 0:
				this.riskProfile = RiskProfile.CONSERVATIVE;
				break;
				
			case 1:
				this.riskProfile = RiskProfile.BALANCED;
				break;
				
			case 2:
				this.riskProfile = RiskProfile.BALANCED;
				break;
				
			default:
				this.riskProfile = RiskProfile.AGGRESSIVE;
		}
	}
	
	public void setRiskProfile(RiskProfile riskProfile)
	{
		this.riskProfile = riskProfile;
	}
	
	public String getRiskProfile() 
	{
		return riskProfile.getProfile();
	}
	
	public Double getRiskLevel() 
	{
		return riskProfile.getRiskLevel();
	}
	
	public WorkerSpecialization getSpecialization() 
	{
		return specialization;
	}

	public void setSpecialization(WorkerSpecialization specialization) 
	{
		this.specialization = specialization;
	}

	@Override
	public String toString() 
	{
		return "Worker [" + super.toString() + " riskProfile=" + riskProfile 
				+ ", specialization=" + specialization + "]";
	}
}