package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import entities.model.Constants;
import entities.model.Helper;
import entities.model.SimpleElement;
import entities.model.Truck;
import entities.model.Worker;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

/**
 * This class configures the viewer to be shown on the screen
 */

public class WordViewer  extends GridWorldView
{
	private static final long serialVersionUID = 1L;
	private WorldModel model;
	
	public WordViewer(WorldModel model, String title, int windowSize) 
	{		
        super(model, title, windowSize);
        this.model = model;
        
        setVisible(true);
        repaint();
	}
	
    @Override
    public void draw(Graphics g, int x, int y, int object) 
    {
    	// Drawing the obstacles (walls)
    	for(Location wall : model.getObstacles())
    		super.drawObstacle(g, wall.x, wall.y);
    	
    	List<Location> posAgents = model.getAgentsPositions();
        
    	// Drawing the artifacts
    	if(object == Constants.GARAGE.getValue())
    	{
    		if (posAgents.contains(new Location(x, y))) 
    			drawnArtifact(g, x, y, Color.pink);
            else
            	drawnArtifact(g, x, y, Color.green);
        
            g.setColor(Color.black);
            drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), "Garage");
    	}
    	else if(object == Constants.DEPOT.getValue())
    	{
    		if (posAgents.contains(new Location(x, y)))
    			drawnArtifact(g, x, y, Color.pink);
            else
            	drawnArtifact(g, x, y, Color.orange);
        
            g.setColor(Color.black);
            drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), "Drop");
    	}
    	else if(object == Constants.RECHARGE.getValue())
    	{
    		if (posAgents.contains(new Location(x, y)))
    			drawnArtifact(g, x, y, Color.pink);
            else
            	drawnArtifact(g, x, y, Color.yellow);
        
            g.setColor(Color.black);
            drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.2)), "Recharge");
    	}
    }

    /**
	 *  This method draws artifacts as squares 
	 */
    public void drawnArtifact(Graphics g, int x, int y, Color c)
    {    	
    	g.setColor(c);
    	g.fillRect(x * super.cellSizeW, y * super.cellSizeH, super.cellSizeW, super.cellSizeH);
    }
    
    /**
	 *  This method draws agents as circles
	 */
    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int i) 
    {
    	int id = model.getIdMapping().get(i);
    	SimpleElement agent = model.getElement(id);
    	
    	if(agent.getClass().equals(Worker.class))
    	{
    		super.drawAgent(g, x, y, Color.yellow, -1);
            g.setColor(Color.black);
            super.drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), ("W" + agent.getId()));
		}
    	else if(agent.getClass().equals(Helper.class))
    	{
    		super.drawAgent(g, x, y, Color.blue, -1);
            g.setColor(Color.white);
            super.drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), ("H" + agent.getId()));
    	}
    	else if(agent.getClass().equals(Truck.class))
    	{
    		super.drawAgent(g, x, y, Color.red, -1);
            g.setColor(Color.white);
            super.drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), ("T" + agent.getId()));
    	}
    }
}