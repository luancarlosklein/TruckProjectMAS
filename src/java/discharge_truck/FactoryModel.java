package discharge_truck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class FactoryModel extends GridWorldModel{
	 // constants for the grid objects
    public static final int DROP1 = 256;
    public static final int DROP2 = 128;
    public static final int TRUCK  = 64;
    public static final int TRUCK2  = 32;
    public static final int TRUCK3  = 16;
    public static final int GARAGE  = 8;
    
    // the grid size
    public static final int GSize = 10;
    
    //Variables
    public Location ldrop1 = new Location(0,0);
    public Location ldrop2 = new Location(GSize-1,0);
    public Location ltruck  = new Location(GSize-1,GSize/2);
    public Location ltruck2  = new Location(0, GSize/2);
    public Location ltruck3  = new Location(GSize/2,GSize-1);
    public Location lgarage  = new Location(GSize-1,GSize-1);
    public static ArrayList<Location> obstacles = new ArrayList<Location>();
    
    
    public Queue<Integer> truckCargo = new LinkedList<Integer>();
    public Queue<String> truckCargoDrop = new LinkedList<String>();
    public int qtdTruck = 0;
    
    
    public Queue<Integer> truck2Cargo = new LinkedList<Integer>();
    public Queue<String> truck2CargoDrop = new LinkedList<String>();
    public int qtdTruck2 = 0;
    
    
    public Queue<Integer> truck3Cargo = new LinkedList<Integer>();
    public Queue<String> truck3CargoDrop = new LinkedList<String>();
    public int qtdTruck3 = 0;
    
    //Matrices for the RLTA*
    public int [][] tabDrop1 = new int[GSize][GSize];
    public int [][] tabDrop2 = new int[GSize][GSize];
    public int [][] tabTruck = new int[GSize][GSize];
    public int [][] tabTruck2 = new int[GSize][GSize];
    public int [][] tabTruck3 = new int[GSize][GSize];
    public int [][] tabGarage = new int[GSize][GSize];
    
    
    //Matrices for the RLTA*
    public int [][] tabDrop1S = new int[GSize][GSize];
    public int [][] tabDrop2S = new int[GSize][GSize];
    public int [][] tabTruckS = new int[GSize][GSize];
    public int [][] tabTruck2S = new int[GSize][GSize];
    public int [][] tabTruck3S = new int[GSize][GSize];
    public int [][] tabGarageS = new int[GSize][GSize];
    
    ////////////////////////////////////////////////////////////
    //Function to generate the load of truck//
    public int generateNewTruck(Queue<Integer> truck, Queue<String> truckDrops)
    {
    	Random generatorCharge = new Random();
    	int qtd = 0;
    	while (qtd == 0)
    	{
    		qtd = generatorCharge.nextInt(20);
    	}
    	int aux = 0;
    	
    	while (aux < qtd)
    	{
    		//The weights and the place to drop is randon
    		truck.add(generatorCharge.nextInt(10));
    		if(generatorCharge.nextInt(100) > 50)
    		{
    			truckDrops.add("drop1");
    		}
    		else
    		{
    			truckDrops.add("drop2");
    		}
    		aux += 1;
    	}
    	return qtd;
    	
    }
    
    
    ////////////////////////////////////////////////////////////
    //Functions for the costruction the matrices and the grid//
    
    //Build a matrix
    public void buildTable(int [][] matriz, Location loca, int type)
    {
    	//Try to read an existing file with have the matrix
    	try {
    	      FileReader arq = new FileReader("tab" + type + ".txt");
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
	    FileWriter arq = new FileWriter("tab" + n + ".txt");
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
    	obstacles.add(new Location(3,2));
    	obstacles.add(new Location(3,3));
    	obstacles.add(new Location(3,4));
    	obstacles.add(new Location(3,5));
    	obstacles.add(new Location(3,6));
    	obstacles.add(new Location(4,2));
    	obstacles.add(new Location(5,2));
    	
    	obstacles.add(new Location(4,5));
    
    	obstacles.add(new Location(6,6));
    	obstacles.add(new Location(6,7));
    	
    	
    	
    }
   
    ////////////////////////////////////////////////////////////
    
    public FactoryModel() {
        // create a GSize x GSize grid with one mobile agent
        super(GSize, GSize, 2);
        buildStruture();

        // initial location of robot (column 1, line 1)
        // ag code 0 means the robot
        setAgPos(0, 6, 5);  
        setAgPos(1, 2, 2);  
        
        
        //Build the matrices
        buildTable(tabDrop1, ldrop1, 0);
        buildTable(tabDrop2, ldrop2, 1);
        buildTable(tabTruck, ltruck, 2);
        buildTable(tabTruck2, ltruck2, 3);
        buildTable(tabTruck3, ltruck3, 4);
        buildTable(tabGarage, lgarage, 5);
        
        buildTable(tabDrop1S, ldrop1, 0);
        buildTable(tabDrop2S, ldrop2, 1);
        buildTable(tabTruckS, ltruck, 2);
        buildTable(tabTruck2S, ltruck2, 3);
        buildTable(tabTruck3S, ltruck3, 4);
        buildTable(tabGarageS, lgarage, 5);
        
          	
        // initial location of drop1, drop2, truck and garage
        add(DROP1, ldrop1);
        add(DROP2, ldrop2);
        add(TRUCK, ltruck);
        add(TRUCK2, ltruck2);
        add(TRUCK3, ltruck3);
        add(GARAGE, lgarage);
    }

   

    //Do the agent drive on the board 
	public boolean moveTowards(Location dest, int code) {
    	
        Location r1 = getAgPos(code); //Get the current agent location 
    
        	//Do a copy of the destiny matrix
            int [][] tab = new int[GSize][GSize];
            //Select a destiny table
            
            if (code == 0)
            {
            	
	            if(dest.equals(ldrop1))
	            {
	            	tab = tabDrop1;
	            }
	            else if(dest.equals(ldrop2))
	            {
	            	tab = tabDrop2;
	            }
	            else if(dest.equals(ltruck))
	            {
	            	tab = tabTruck;
	            }
	            
	            else if(dest.equals(ltruck2))
	            {
	            	tab = tabTruck2;
	            }
	            
	            else if(dest.equals(ltruck3))
	            {
	            	tab = tabTruck3;
	            }
	            
	            else if(dest.equals(lgarage))
	            {
	            	tab = tabGarage;
	            }
	            else
	            {
	            	tab = tabTruck;
	            }
            }
            
            else if(code == 1)
            {
            	//Do a copy of the destiny matrix
                
                //Select a destiny table
                if(dest.equals(ldrop1))
                {
                	tab = tabDrop1S;
                }
                else if(dest.equals(ldrop2))
                {
                	tab = tabDrop2S;
                }
                else if(dest.equals(ltruck))
                {
                	tab = tabTruckS;
                }
                
                else if(dest.equals(ltruck2))
                {
                	tab = tabTruck2S;
                }
                
                else if(dest.equals(ltruck3))
                {
                	tab = tabTruck3S;
                }
                
                else if(dest.equals(lgarage))
                {
                	tab = tabGarageS;
                }
                else
                {
                	tab = tabTruckS;
                }
            }
            //Define the 4 posibilites to drive (North South East West)
            Location norte;
            Location sul;
            Location leste;
            Location oeste;
   
            //Define the current position. Used for to dont get a invaliable position out of the matrice
            Location out = new Location(-1,-1); ;
            
            //Build the 4 posibilites
            if ( (r1.y - 1) >= 0)
            {
            	  norte = new Location(r1.x, r1.y - 1); 
            }
            else
            {
            	norte = out;
            	
            }
            
            if ( (r1.y + 1) < GSize)
            {
            	sul = new Location(r1.x, r1.y + 1);
            	
            }
            else
            {
            	sul = out;
            	
            }
            
            if ( (r1.x - 1) >= 0)
            {
            	 oeste = new Location(r1.x - 1, r1.y); 
            }
            else
            {
            	oeste = out;
            	
            }
            
            if ( (r1.x + 1) < GSize)
            {
            	leste = new Location(r1.x + 1, r1.y); 
            }
            else
            {
            	leste = out;
            	
            }
        
          //Get the value to the matrix
            int qtd = 0;
            Location[] posibilidades = {out, out, out, out};
            int[] valores = {100000, 10000, 10000, 10000};
            
            if (!(sul == out))
            {
            	 int vSul = tab[sul.y][sul.x];
            	 valores[qtd] = vSul;
            	 posibilidades[qtd] = sul;
            	 qtd++;
            }
            
            if (!(norte == out))
            {
            	int vNorte = tab[norte.y][norte.x];
            	valores[qtd] = vNorte;
            	posibilidades[qtd] = norte;
            	qtd++;
            }
            
            if (!(oeste == out))
            {
            	int vOeste = tab[oeste.y][oeste.x];
            	valores[qtd] = vOeste;
            	posibilidades[qtd] = oeste;
            	qtd++;
            }
            
            if (!(leste == out))
            {
            	 int vLeste = tab[leste.y][leste.x];
            	 valores[qtd] = vLeste;
            	 posibilidades[qtd] = leste;
            	 qtd++;
            }
            int minimo = valores[0];
            Location posMin = posibilidades[0];
                    
            int aux = 0;
            while (aux < qtd)
            {
            	if(valores[aux] < minimo)
            	{
            		minimo = valores[aux];
            		posMin = posibilidades[aux];
            	}
            	aux++;
            }
            
            if (tab[r1.y][r1.x] > 0)
            {
            	//Add one to the place witch have the minimum value
                tab[r1.y][r1.x] = minimo + 1;
            }
            
            //Save the update on the file
            int typeSalv = 999;
           //Save files for the Worker
            if (code == 0)
            {
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
	            
	            else if(dest.equals(ltruck2))
	            {
	            	tabTruck2 = tab;
	            	typeSalv = 3;
	            }
	            
	            else if(dest.equals(ltruck3))
	            {
	            	tabTruck3 = tab;
	            	typeSalv = 4;
	            }
	            
	            else if(dest.equals(lgarage))
	            {
	            	tabGarage = tab;
	            	typeSalv = 5;
	            }
	            try {
	    			salvar(typeSalv, tab);
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
            }
            //Save files for the Worker2
            else if(code == 1)
            {
            	  //Save the update on the file
                
                if(dest.equals(ldrop1))
                {
                	tabDrop1S = tab;
                	typeSalv = 00;
                }
                else if(dest.equals(ldrop2))
                {
                	tabDrop2S =  tab;
                	typeSalv = 11;
                }
                else if(dest.equals(ltruck))
                {
                	tabTruckS = tab;
                	typeSalv = 22;
                }
                
                else if(dest.equals(ltruck2))
                {
                	tabTruck2S = tab;
                	typeSalv = 33;
                }
                
                else if(dest.equals(ltruck3))
                {
                	tabTruck3S = tab;
                	typeSalv = 44;
                }
                
                else if(dest.equals(lgarage))
                {
                	tabGarageS = tab;
                	typeSalv = 55;
                }
                try {
        			salvar(typeSalv, tab);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
            

            //Update the agent position
            setAgPos(code, posMin);

        if (view != null) {
            view.update(ldrop1.x,ldrop1.y);
            view.update(ldrop2.x,ldrop2.y);
            view.update(ltruck.x,ltruck.y);
            view.update(ltruck2.x,ltruck2.y);
            view.update(ltruck3.x,ltruck3.y);
            view.update(lgarage.x,lgarage.y);
        }
        return true;
    }
   

}
