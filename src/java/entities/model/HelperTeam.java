package entities.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * This class implements a team of helper, which is able to unload a truck.
 * A team is represented by a Hash table, where each entry is represented as a Helper and its status.
 * The status of a helper is defined either as true (hired to perform a service), or false (dispensed of service).
 */
public class HelperTeam 
{
	private Integer id;
	private Boolean ready;
	private Integer teamSize;
	private Map<Helper, Boolean> team;
	
	public HelperTeam(int id, int teamSize)
	{
		this.id = id;
		this.ready = false;
		this.teamSize = teamSize;
		this.team = new HashMap<Helper, Boolean>();
	}

	/**
	 * Add a helper to team.
	 * @param helper: helper to be added.
	 * @return true, if the helper was added to team, otherwise, false.
	 */
	public boolean addHelper(Helper helper) 
	{
		if (team.size() == teamSize)
			return false;
		else
			team.put(helper, false);	
		return true;
	}
	
	/**
	 * Remove a helper from team.
	 * @param helper: Helper to be removed.
	 * @return true, if the helper was removed from the team, otherwise, false.
	 */
	public boolean removeHelper(Helper helper)
	{
		if(team.containsKey(helper))
			return team.remove(helper) != null;
		else
			throw new IllegalAccessError("Helper not found: " + helper.getName());
	}
	
	/**
	 * Change the status of helper to hired.
	 * @param helper: the helper that will have his status changed.
	 */
	public void hireMember(Helper helper)
	{
		if(team.containsKey(helper))
			team.put(helper, true);
		else
			throw new IllegalAccessError("The helper is not in the team: " + helper.getName());
	}

	/**
	 * Check if the team is full (the team has the maximum size).
	 * @return true, if the team is full, otherwise, false.
	 */
	public boolean teamIsFull()
	{
		return this.team.size() == this.teamSize;
	}
	
	/**
	 * Check if all members of time are hired.
	 * @return true, if all member are hired, otherwise, false.
	 */
	public boolean teamIsReady()
	{
		if(ready)
			return true;
		
		for(Helper helper : team.keySet())
		{
			if(!team.get(helper))
				return false;
		}
		return true;
	}
	
	/**
	 * @return a set of all members of team.
	 */
	public Set<Helper> getMembers()
	{
		return team.keySet();
	}
	
	/**
	 * @return a set of all members that have already accepted the service (hired).
	 */
	public Set<Helper> getReadyMembers()
	{
		Set<Helper> members = new HashSet<Helper>();
		
		for(Helper member: team.keySet())
		{
			if(team.get(member))
				members.add(member);
		}
		return members;
	}
	
	/**
	 * @return a set of all members that haven't accepted the service yet.
	 */
	public Set<Helper> getNotReadyMembers()
	{
		Set<Helper> members = new HashSet<Helper>();
		
		for(Helper member: team.keySet())
		{
			if(!team.get(member))
				members.add(member);
		}
		return members;
	}
	
	public void showTeam()
	{
		System.out.println(this);
	}
	
	public int getId() 
	{
		return id;
	}

	public int getTeamSize() 
	{
		return teamSize;
	}

	public Boolean isReady() 
	{
		return ready;
	}

	public void setReady(Boolean ready) 
	{
		this.ready = ready;
	}

	public void setTeamSize(Integer teamSize) 
	{
		this.teamSize = teamSize;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HelperTeam other = (HelperTeam) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer();
		int i = 0;
		
		for(Helper h : team.keySet())
		{
			if(i++ < team.size() - 1)
			{
				sb.append(h.getName());
				sb.append("(hired: ").append(team.get(h)).append(");");
			}
			else
			{
				sb.append(h.getName());
				sb.append("(hired: ").append(team.get(h)).append(")");
			}
		}
		return "HelperTeam [id=" + id + ", isReady=" + ready + ", teamSize=" + teamSize + ", team={" + sb.toString() + "}]";
	}
}