package discharge_truck;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

public class FactoryView extends GridWorldView{
	private static final long serialVersionUID = 1L;
	FactoryModel hmodel;
    
	//Screen Settings 
    public FactoryView(FactoryModel model) {
        super(model, "Discharge Truck", 700);
        hmodel = model;
        defaultFont = new Font("Arial", Font.BOLD, 16); // change default font
        setVisible(true);
        repaint();
        
    }

    /** draw application objects */
    @Override
    public void draw(Graphics g, int x, int y, int object) {
    	
    	int qtdAgents = FactoryModel.qtdWorkers + FactoryModel.qtdHelpers;
    	ArrayList<Location> posAgents = new ArrayList<Location>();
    	int cont = 0;
    	while (cont < qtdAgents)
    	{
    		posAgents.add(hmodel.getAgPos(cont));
    		cont ++;
    	}
              
        Iterator<Location> itr = FactoryModel.obstacles.iterator();
        while(itr.hasNext()) {
        	Location element = (Location) itr.next();
        	super.drawObstacle(g, element.x, element.y);
        }
        
        switch (object) {
            case FactoryModel.TRUCK1: 
            	super.drawAgent(g, x, y, Color.red, -1);
                if (posAgents.contains(hmodel.ltruck1)){
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Truck");  
                break;
                
            case FactoryModel.TRUCK2: 
            	super.drawAgent(g, x, y, Color.red, -1);
                if (posAgents.contains(hmodel.ltruck2)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Truck2");  
                break;
                
            case FactoryModel.TRUCK3: 
            	super.drawAgent(g, x, y, Color.red, -1);
                if (posAgents.contains(hmodel.ltruck3)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Truck3");  
                break;
                
            case FactoryModel.DROP1:
            	super.drawAgent(g, x, y, Color.orange, -1);
                if (posAgents.contains(hmodel.ldrop1)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Drop1");  
                break;
            case FactoryModel.GARAGE:
            	super.drawAgent(g, x, y, Color.green, -1);
                if (posAgents.contains(hmodel.lgarage)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Garage");  
                break;    
            case FactoryModel.DROP2:
            	super.drawAgent(g, x, y, Color.orange, -1);
                if (posAgents.contains(hmodel.ldrop2)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Drop2");  
                break;  
        }      
    }

   
    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
    	
    		//Worker color
    		if (id < 3)
    		{
    			c = Color.yellow;
                super.drawAgent(g, x, y, c, id);
                g.setColor(Color.black);
                super.drawString(g, x, y, defaultFont, "W.......");		
    		}
            
            //Helper color
    		else if (id >= 3)
            {
            	c = Color.blue;
                super.drawAgent(g, x, y, c, id);
                g.setColor(Color.white);
                super.drawString(g, x, y, defaultFont, "H.......");
            }
        
    }

}
