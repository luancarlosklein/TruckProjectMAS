package discharge_truck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class FactoryModel extends GridWorldModel{
	 // constants for the grid objects
    public static final int DROP1 = 128;
    public static final int DROP2 = 64;
    public static final int TRUCK  = 32;
    public static final int GARAGE  = 16;
    
    // the grid size
    public static final int GSize = 10;
    
    //Variables
    public Location ldrop1 = new Location(0,0);
    public Location ldrop2 = new Location(GSize-1,0);
    public Location ltruck  = new Location(GSize-1,GSize-1);
    public Location lgarage  = new Location(GSize/2,GSize-1);
    public static ArrayList<Location> obstacles = new ArrayList<Location>();
    
    //Matrices for the RLTA*
    public int [][] tabDrop1 = new int[GSize][GSize];
    public int [][] tabDrop2 = new int[GSize][GSize];
    public int [][] tabTruck = new int[GSize][GSize];
    public int [][] tabGarage = new int[GSize][GSize];
    
    ////////////////////////////////////////////////////////////
    //Functions for the costruction the matrices and the grid//
    
    //Build a matrix
    public void buildTable(int [][] matriz, Location loca, int type)
    {
    	//Try to read an existing file with have the matrix
    	try {
    	      FileReader arq = new FileReader("C:\\\\Users\\\\Luan\\\\Desktop\\\\tab" + type + ".txt");
    	      BufferedReader lerArq = new BufferedReader(arq);
    	      String linha = lerArq.readLine(); 
    	      int i = 0;
    	      //Put the informations that have collected on the file in the matrix
    	      while (linha != null) { 
    	        System.out.printf("%s\n", linha);
    	        String[] linhaSeparada = linha.split(",");
    	        for (int j = 0; j < linhaSeparada.length; j++) {
    	        	matriz[i][j] = Integer.parseInt(linhaSeparada[j]);
    	        }
    	          
    	        linha = lerArq.readLine();
    	        i++;
    	      }
    	      arq.close();
    	      //If the file did not exist, the matrix is build from to zero
    	    } catch (IOException e) {
    	    	 for (int linha = 0; linha< GSize; linha++) {
    	    	        for (int coluna = 0; coluna < GSize; coluna++) {
    	    	        	if (!obstacles.contains(new Location(coluna,linha)) )
    	    	        		{
    	    	        			//Calculate the Manhattan distance
    	    	        			matriz [linha] [coluna] = Math.abs(loca.x - coluna) + Math.abs(loca.y - linha);
    	    	        		}
    	    	        	else
    	    	        	{
    	    	        		//If in the place have an obstacles, the distance is infinite
    	    	        		matriz [linha][coluna] = 9999;
    	    	        	}
    	    	        }
    	    	    }   
    	    }
    }
    
    //Save the matrix in a file
    public void salvar(int type, int[][] tab) throws IOException
    {
    	
    	int n = type;
	    FileWriter arq = new FileWriter("C:\\Users\\Luan\\Desktop\\tab" + n + ".txt");
	    PrintWriter gravarArq = new PrintWriter(arq);   
        for (int linha = 0; linha< GSize; linha++) {
 	        for (int coluna = 0; coluna < GSize; coluna++) {
 	        	 gravarArq.printf(tab[linha][coluna] + ",");
 	        }
 	       gravarArq.printf("%n");
 	       }        
	       arq.close();
	    }
    
    //Put the obstacles on the matrix
    public void buildStruture(){
    	obstacles.add(new Location(5,0));
    	obstacles.add(new Location(5,1));
    	obstacles.add(new Location(5,2));
    	obstacles.add(new Location(5,3));
    	obstacles.add(new Location(5,4));
    	obstacles.add(new Location(4,3));
    	obstacles.add(new Location(4,1));
    	obstacles.add(new Location(2,1));
    	obstacles.add(new Location(3,3));
    	obstacles.add(new Location(4,4));
    	obstacles.add(new Location(5,5));
    	obstacles.add(new Location(6,6));
    	
    }
   
    ////////////////////////////////////////////////////////////
    
    public FactoryModel() {
        // create a GSize x GSize grid with one mobile agent
        super(GSize, GSize, 1);
        buildStruture();

        // initial location of robot (column 1, line 1)
        // ag code 0 means the robot
        setAgPos(0, 1, 1);  
        
        //Build the matrices
        buildTable(tabDrop1, ldrop1, 0);
        buildTable(tabDrop2, ldrop2, 1);
        buildTable(tabTruck, ltruck, 2);
        buildTable(tabGarage, lgarage, 3);
          	
        // initial location of drop1, drop2, truck and garage
        add(DROP1, ldrop1);
        add(DROP2, ldrop2);
        add(TRUCK, ltruck);
        add(GARAGE, lgarage);
    }

    //Do the agent drive on the board 
    public boolean moveTowards(Location dest) {
    	
        Location r1 = getAgPos(0); //Get the current agent location 
        
        //Do a copy of the destiny matrix
        int [][] tab = new int[GSize][GSize];
        //Select a destiny table
        if(dest.equals(ldrop1))
        {
        	tab = tabDrop1;
        }
        else if(dest.equals(ldrop1))
        {
        	tab = tabDrop2;
        }
        else if(dest.equals(ltruck))
        {
        	
        	tab = tabTruck;
        }
        else if(dest.equals(lgarage))
        {
        	tab = tabGarage;
        }
        else
        {
        	tab = tabTruck;
        }
        
        //Define the 4 posibilites to drive (North South East West)
        Location norte;
        Location sul;
        Location leste;
        Location oeste;
        
        //Define the current position. Used for to dont get a invaliable position out of the matrice
        Location minimo = r1 ;
        
        //Build the 4 posibilites
        if ( (r1.y - 1) >= 0)
        {
        	  norte = new Location(r1.x, r1.y - 1); 
        }
        else
        {
        	norte = r1;
        }
        
        if ( (r1.y + 1) < GSize)
        {
        	sul = new Location(r1.x, r1.y + 1);
        	
        }
        else
        {
        	sul = r1;        
        }
        
        if ( (r1.x - 1) >= 0)
        {
        	 oeste = new Location(r1.x - 1, r1.y); 
        }
        else
        {
        	oeste = r1;
        }
        
        if ( (r1.x + 1) < GSize)
        {
        	leste = new Location(r1.x + 1, r1.y); 
        }
        else
        {
        	leste = r1;
        }
    
        //Get the value to the matrix
        int vSul = tab[sul.y][sul.x];
        int vNorte = tab[norte.y][norte.x];
        int vOeste = tab[oeste.y][oeste.x];
        int vLeste = tab[leste.y][leste.x];
        
       //Selects the lowest value from the 4 found
        if (vSul <= vNorte)
        {
        	if (vSul <= vOeste)
        	{
        		if (vSul <= vLeste)
        		{
        			 minimo = sul;
        		}
        		
        	}
        }			
        if (minimo.equals(r1))
        {
        	 if (vNorte <= vSul)
        	 {
             	if (vNorte <= vOeste)
             	{
             		if (vNorte <= vLeste)
             		{
             			minimo = norte;
             		}
             	}
        	 }
        }    
        if (minimo.equals(r1))
        {
             if (vOeste <= vNorte)
             {
             	if (vOeste <= vSul)
             	{
             		if (vOeste <= vLeste)
             		{
             			minimo = oeste;
             		}
             	}
             }
        }
        if (minimo.equals(r1))
        {
             if (vLeste <= vNorte)
             {
             	if (vLeste <= vOeste)
             	{
             		if (vLeste <= vSul)
             		{
             			minimo = leste;
             		}
             	}
             }
        }
             			
        //Add one to the place witch have the minimum value
        tab[r1.y][r1.x] = tab[minimo.y][minimo.x] + 1;
        
        //Save the update on the file
        int typeSalv = 999;
        if(dest.equals(ldrop1))
        {
        	tabDrop1 = tab;
        	typeSalv = 0;
        }
        else if(dest.equals(ldrop2))
        {
        	tabDrop2 =  tab;
        	typeSalv = 1;
        }
        else if(dest.equals(ltruck))
        {
        	tabTruck = tab;
        	typeSalv = 2;
        }
        else if(dest.equals(lgarage))
        {
        	tabGarage = tab;
        	typeSalv = 3;
        }
        try {
			salvar(typeSalv, tab);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
        //Update the agent position
        setAgPos(0, minimo);
        
        if (view != null) {
            view.update(ldrop1.x,ldrop1.y);
            view.update(ldrop2.x,ldrop2.y);
            view.update(ltruck.x,ltruck.y);
            view.update(lgarage.x,lgarage.y);
        }
        return true;
    }
   
}
