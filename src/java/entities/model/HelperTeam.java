package entities.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelperTeam 
{
	private int id;
	private int teamSize;
	private Map<Helper, Boolean> team;		// Helper and his status (false, not hired; true, hired)
	
	public HelperTeam(int id, int teamSize)
	{
		this.id = id;
		this.teamSize = teamSize;
		this.team = new HashMap<Helper, Boolean>();
	}
	
	/**
	 * Check if team is complete (reached maximum size).
	 * @return true, if the team is complete.
	 */
	public boolean teamIsComplete()
	{
		return this.team.size() == this.teamSize;
	}

	/**
	 * Add a helper to team.
	 * 
	 * @param helper: Helper to be added.
	 * @return true, if the helper was added to team, otherwise returns false.
	 */
	public boolean addHelper(Helper helper) 
	{
		if (teamIsComplete())
			return false;
		else
			team.put(helper, false);
		
		return true;
	}
	
	/**
	 * Remove a helper from team.
	 * @param helper: Helper to be removed.
	 * @return true, if the helper was removed from team, otherwise returns false.
	 */
	public boolean removeHelper(Helper helper)
	{
		if(team.containsKey(helper))
			return team.remove(helper);
		else
			throw new IllegalAccessError("Helper not found: " + helper.getName());
	}
	
	/**
	 * Change the status of helper for hired.
	 * @param helper: the helper that will be his status changed.
	 */
	public void contractMember(Helper helper)
	{
		if(team.containsKey(helper))
			team.put(helper, true);
		else
			throw new IllegalAccessError("The helper wasn't in the team: " + helper.getName());
	}
	
	/**
	 * Create a list compound of names of members of a team.
	 * @return true, if the team is complete.
	 */
	public List<String> getMemberNames()
	{
		List<String> memberNames = new ArrayList<String>();
		
		for(Helper member : team.keySet())
			memberNames.add(member.getName());
		
		return memberNames;
	}
	
	/**
	 * Check if all members of time are hired.
	 * @return true, if all member are hired, otherwise, false.
	 */
	public boolean teamIsReady()
	{
		for(Helper helper : team.keySet())
		{
			if(!team.get(helper))
				return false;
		}
		return true;
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
				sb.append(h.getName()).append(";");
			else
				sb.append(h.getName());
		}
		
		return "HelperTeam [id=" + id + ", teamSize=" + teamSize + ", team={" + sb.toString() + "}]";
	}
}