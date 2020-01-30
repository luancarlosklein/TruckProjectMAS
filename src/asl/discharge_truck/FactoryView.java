package discharge_truck;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
    	
        Location lRobot = hmodel.getAgPos(0);
        Location lRobot2 = hmodel.getAgPos(1);
        
        //super.drawAgent(g, x, y, Color.lightGray, -1);
              
        Iterator<Location> itr = FactoryModel.obstacles.iterator();
        while(itr.hasNext()) {
        	Location element = (Location) itr.next();
        	super.drawObstacle(g, element.x, element.y);
        }
        
        switch (object) {
            case FactoryModel.TRUCK: 
            	super.drawAgent(g, x, y, Color.red, -1);
                if (lRobot.equals(hmodel.ltruck) || lRobot2.equals(hmodel.ltruck)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Truck");  
                break;
                
            case FactoryModel.TRUCK2: 
            	super.drawAgent(g, x, y, Color.red, -1);
                if (lRobot.equals(hmodel.ltruck2) || lRobot2.equals(hmodel.ltruck2)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Truck2");  
                break;
                
            case FactoryModel.TRUCK3: 
            	super.drawAgent(g, x, y, Color.red, -1);
                if (lRobot.equals(hmodel.ltruck3) || lRobot2.equals(hmodel.ltruck3)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Truck3");  
                break;
                
            case FactoryModel.DROP1:
            	super.drawAgent(g, x, y, Color.orange, -1);
                if (lRobot.equals(hmodel.ldrop1) || lRobot2.equals(hmodel.ldrop1)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Drop1");  
                break;
            case FactoryModel.GARAGE:
            	super.drawAgent(g, x, y, Color.green, -1);
                if (lRobot.equals(hmodel.lgarage) || lRobot2.equals(hmodel.lgarage)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Garage");  
                break;    
            case FactoryModel.DROP2:
            	super.drawAgent(g, x, y, Color.orange, -1);
                if (lRobot.equals(hmodel.ldrop2) || lRobot2.equals(hmodel.ldrop2)) {
                    super.drawAgent(g, x, y, Color.pink, -1);
                }
                g.setColor(Color.black);
                drawString(g, x, y, defaultFont, "Drop2");  
                break;  
        }      
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        Location lRobot = hmodel.getAgPos(0);
        if (!(lRobot.equals(hmodel.ltruck) ||  lRobot.equals(hmodel.ltruck2) ||  lRobot.equals(hmodel.ltruck3)) && !lRobot.equals(hmodel.ldrop1) && !lRobot.equals(hmodel.ldrop2)) {
            c = Color.yellow;
            //if (hmodel.carryingBeer) c = Color.orange;
            super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.black);
            super.drawString(g, x, y, defaultFont, "Worker");
        }       
    }

}
