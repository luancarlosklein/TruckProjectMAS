package views;

import java.awt.Font;

import jason.environment.grid.GridWorldView;

public class WordViewer  extends GridWorldView
{
	private static final long serialVersionUID = 1L;
	private WorldModel model;
	
	public WordViewer(WorldModel model, String title, int windowSize) 
	{		
        super(model, title, windowSize);
        this.model = model;
        defaultFont = new Font("Arial", Font.BOLD, 16);
        setVisible(true);
        repaint();
	}


}
