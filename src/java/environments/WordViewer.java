package environments;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import entities.enums.Constants;
import entities.model.Helper;
import entities.model.SimpleElement;
import entities.model.Truck;
import entities.model.Worker;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

/**
 * This class configures the viewer to be shown on the screen
 */

public class WordViewer extends GridWorldView
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
    		super.drawObstacle(g, wall.y, wall.x);
    	
    	List<Location> posAgents = model.getAgentsPositions();
        
    	// Drawing the artifacts
    	if(object == Constants.GARAGE.getValue())
    	{
    		if (posAgents.contains(new Location(x, y))) 
    			drawArtifact(g, x, y, Color.pink);
            else
            	drawArtifact(g, x, y, Color.green);
        
            g.setColor(Color.black);
            drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), "Garage");
    	}
    	else if(object == Constants.DEPOT.getValue())
    	{
    		if (posAgents.contains(new Location(x, y)))
    			drawArtifact(g, x, y, Color.pink);
            else
            	drawArtifact(g, x, y, Color.orange);
        
            g.setColor(Color.black);
            drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), "Drop");
    	}
    	else if(object == Constants.RECHARGE.getValue())
    	{
    		if (posAgents.contains(new Location(x, y)))
    			drawArtifact(g, x, y, Color.pink);
            else
            	drawArtifact(g, x, y, Color.yellow);
        
            g.setColor(Color.black);
            drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.2)), "Recharge");
    	}
    }
    
    /**
	 *  This method draws agents as circles
	 */
    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int code) 
    {   
    	if(code >= 0)
    	{
    		int id = model.codeMapping().get(code);
        	SimpleElement agent = model.getElement(id);
        	
        	drawBorders(g);
        	
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
        		Truck t = (Truck) agent;
        		
        		if(t.isVisible())
        		{
    	    		super.drawAgent(g, x, y, Color.red, -1);
    	            g.setColor(Color.white);
    	            super.drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), ("T" + agent.getId()));
        		}
        		else
        		{
        			super.drawAgent(g, x, y, Color.black, -1);
    	            g.setColor(Color.white);
    	            super.drawString(g, x, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), ("T" + agent.getId()));
        		}
        	}	
    	}
    }
    
    /**
	 *  This method draws artifacts as squares 
	 */
    public void drawArtifact(Graphics g, int x, int y, Color c)
    {   
    	g.setColor(c);
    	g.fillRect(x * super.cellSizeW, y * super.cellSizeH, super.cellSizeW, super.cellSizeH);
    }
    
    /**
     * Check if the number of lines and columns were already drawn.
     */
    public void drawBorders(Graphics g)
    {	
		int w = (super.getWidth() / super.cellSizeW) - 1;
		int h = (super.getHeight() / super.cellSizeH) - 1;
		
		for(int x = 0; x < w; x++) 
		{    			
			drawArtifact(g, x, h, Color.WHITE);
			
			g.setColor(Color.BLACK);
			super.drawString(g, x, h, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), "" + x);
		}
		
		for(int y = 0; y < h; y++) 
		{    			
			drawArtifact(g, w, y, Color.WHITE);
			
			g.setColor(Color.BLACK);
			super.drawString(g, w, y, new Font("Arial", Font.BOLD, (int) (super.cellSizeH * 0.25)), "" + y);
		}
    }
}