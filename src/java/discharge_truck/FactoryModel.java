package discharge_truck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class FactoryModel extends GridWorldModel{
	 // constants for the grid objects
    public static final int DROP1 = 256;
    public static final int DROP2 = 128;
    public static final int TRUCK1  = 64;
    public static final int TRUCK2  = 32;
    public static final int TRUCK3  = 16;
    public static final int GARAGE  = 8;
    
    // the grid size
    public static final int GSize = 10;
    
    //Quantities of agents in the scenario
    public static final int qtdWorkers = 6;
    public static final int qtdHelpers = 3;
    
    //Variables
    public Location ldrop1 = new Location(2,0);
    public Location ldrop2 = new Location(7,0);
    public Location ltruck1  = new Location(0,GSize-1);
    public Location ltruck2  = new Location(4,GSize-1);
    public Location ltruck3  = new Location(GSize-1,GSize-1);
    public Location lgarage  = new Location(6,4);
    public static ArrayList<Location> obstacles = new ArrayList<Location>();
    
    public Queue<Integer> truck1Cargo = new LinkedList<Integer>();
    public Queue<String> truck1CargoDrop = new LinkedList<String>();
    public int qtdTruck = 0;
    
    public Queue<Integer> truck2Cargo = new LinkedList<Integer>();
    public Queue<String> truck2CargoDrop = new LinkedList<String>();
    public int qtdTruck2 = 0;
    
    public Queue<Integer> truck3Cargo = new LinkedList<Integer>();
    public Queue<String> truck3CargoDrop = new LinkedList<String>();
    public int qtdTruck3 = 0;
    
    //Matrices for the LRTA*
    public int [][] tabDrop1 = new int[GSize][GSize];
    public int [][] tabDrop2 = new int[GSize][GSize];
    public int [][] tabTruck = new int[GSize][GSize];
    public int [][] tabTruck2 = new int[GSize][GSize];
    public int [][] tabTruck3 = new int[GSize][GSize];
    public int [][] tabGarage = new int[GSize][GSize];
    
    //Variable used to verify all movements were done
    public int qtdPassos = 0;
        
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
    			truckDrops.add("drop1");
    		}
    		aux += 1;
    	}	
    	return qtd;
    }
    
    
    ////////////////////////////////////////////////////////////
    //Functions for the construction the matrices and the grid//
    
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
    	        //System.out.printf("%s\n", linha);
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
    	    	        			//***********************************************//
    	    	        			//Here you define the initial Heuristic fot LRTA*//
    	    	        			//***********************************************//
    	    	        		
    	    	        			//Calculate the Manhattan distance
    	    	        			matriz[linha][coluna] = Math.abs(loca.x - coluna) + Math.abs(loca.y - linha);
    	    	        			//Use the Null Heuristic
    	    	        			//matriz [linha] [coluna] = 0;
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
    
    //Function to compare if the path found is the ideal path
    public void compareArchive(int type)
    { 
    	//In this, compare if the first optimal path was founded.
    	//For this, compare the value on the arrival place with the valeu in the optimal path
    	if (tabDrop1[GSize -1][0] == 15)
    	{
        	System.out.print("PASSOSSSSSSSSSSSSSSSSSSSSSSSSSSS: " + qtdPassos);
        	
        	//Stop the system
        	//Scanner input = new Scanner(System.in); 
            //String pausa = input.next();
    	}
   }
    
    
    //Put the obstacles on the matrix
    public void buildStruture(){
    	
    	obstacles.add(new Location(1,0));
    	obstacles.add(new Location(1,1));
    	obstacles.add(new Location(1,2));
    	obstacles.add(new Location(1,3));
    	obstacles.add(new Location(2,2));
    	obstacles.add(new Location(3,2));
    	obstacles.add(new Location(3,3));
    	obstacles.add(new Location(3,4));
    	obstacles.add(new Location(3,5));
    	obstacles.add(new Location(3,6));
    	
    	
    	obstacles.add(new Location(8,0));
    	obstacles.add(new Location(8,1));
    	obstacles.add(new Location(8,2));
    	obstacles.add(new Location(8,3));
    	obstacles.add(new Location(8,4));
    	obstacles.add(new Location(8,5));
    	
    	obstacles.add(new Location(7,3));
    	obstacles.add(new Location(6,3));
    	obstacles.add(new Location(5,3));
    	
    	obstacles.add(new Location(7,5));
    	obstacles.add(new Location(6,5));
    	obstacles.add(new Location(5,5));    	
    
    }
   
    ////////////////////////////////////////////////////////////
    
    public FactoryModel() {
        // create a GSize x GSize grid with one mobile agent
        super(GSize, GSize, (qtdHelpers + qtdWorkers) );
        buildStruture();

        //Build the matrices
        buildTable(tabDrop1, ldrop1, 0);
        buildTable(tabDrop2, ldrop2, 1);
        buildTable(tabTruck, ltruck1, 2);
        buildTable(tabTruck2, ltruck2, 3);
        buildTable(tabTruck3, ltruck3, 4);
        buildTable(tabGarage, lgarage, 5);
        
        // Initial agents locations 
        // Every agent have a code (0, 1, 2, 3...)
        Random generator = new Random();
        int cont = 0;
       
        while (cont < qtdWorkers)
        {
        	//Generate the initial position. It is random 
            int x = generator.nextInt(GSize - 1);
            int y = generator.nextInt(GSize - 1);
            if ( !obstacles.contains(new Location(x,y)) )
            {
            	System.out.println("X: " + x + " Y: " + y);
            	System.out.println("POSIÇÕES INICIALIZADAS");
            	setAgPos(cont, x, y); 
            	cont += 1;
            }
        }
       
        cont = 0;
        while (cont < qtdHelpers)
        {
        	//Generate the initial position. It is random 
            setAgPos(qtdWorkers + cont, lgarage.x, lgarage.y); 
            cont += 1;
        }
        
        // initial location of drop1, drop2, truck and garage
        add(DROP1, ldrop1);
        add(DROP2, ldrop2);
        add(TRUCK1, ltruck1);
        add(TRUCK2, ltruck2);
        add(TRUCK3, ltruck3);
        add(GARAGE, lgarage);
    }

    //Do the agent drive on the board 
	public boolean moveTowards(Location dest, int code) {
			Location r1 = getAgPos(code); //Get the current agent location 
			
			//Just for test
			String local = null;
			
        	//Do a copy of the destiny matrix
            int [][] tab = new int[GSize][GSize];
            
            //Select a destiny table
            if(dest.equals(ldrop1)){
            	tab = tabDrop1;
                qtdPassos += 1;
                local = "drop1";
            }
            else if(dest.equals(ldrop2)){
            	tab = tabDrop2;
            }
            else if(dest.equals(ltruck1)){
            	tab = tabTruck;
            	local = "truck";
            }
            else if(dest.equals(ltruck2)){
            	tab = tabTruck2;
            }
            else if(dest.equals(ltruck3)){
            	tab = tabTruck3;
            }
            else if(dest.equals(lgarage)){
            	tab = tabGarage;
            	local = "garagem";
            }
            else{
            	tab = tabTruck;
            	local = "truck";
            }
            
            
            //Check if the agents don't pass in the walls
            if(obstacles.contains(r1)){
	        	System.out.println("AGENTE " + code + " PULOU O MURRO INDO PARA" + local);	
	        	//Stop the systems
	        	@SuppressWarnings("resource")
				Scanner input = new Scanner(System.in); 
		        @SuppressWarnings("unused")
				String pausa = input.next();
	        }
            
            //Define the 4 posibilites to drive (North South East West)
            Location norte;
            Location sul;
            Location leste;
            Location oeste;
   
            //Define the current position. Used for to dont get a invaliable position out of the matrice
            Location out = new Location(-1,-1); ;
            
            //Build the 4 posibilites
            //If one the agent is in the border, the position out of the table is set to "out"
            if ( (r1.y - 1) >= 0){
            	  norte = new Location(r1.x, r1.y - 1); 
            }
            else{
            	norte = out;
            }
            
            if ( (r1.y + 1) < GSize){
            	sul = new Location(r1.x, r1.y + 1);
            }
            else{
            	sul = out;
            }
            
            if ( (r1.x - 1) >= 0){
            	 oeste = new Location(r1.x - 1, r1.y); 
            }
            else{
            	oeste = out;
            }
            
            if ( (r1.x + 1) < GSize){
            	leste = new Location(r1.x + 1, r1.y); 
            }
            else{
            	leste = out;
            }
        
          //Get the value to the matrix
            int qtd = 0;
            //Just initialization of the array with the worst case
            Location[] posibilidades = {out, out, out, out};
            int[] valores = {100000, 10000, 10000, 10000};
            
            //Put the places is not "Out" in the array possibilidades and valores
            if (!(sul == out)){
            	 int vSul = tab[sul.y][sul.x];
            	 valores[qtd] = vSul;
            	 posibilidades[qtd] = sul;
            	 qtd++;
            }
            
            if (!(norte == out)){
            	int vNorte = tab[norte.y][norte.x];
            	valores[qtd] = vNorte;
            	posibilidades[qtd] = norte;
            	qtd++;
            }
            
            if (!(oeste == out)){
            	int vOeste = tab[oeste.y][oeste.x];
            	valores[qtd] = vOeste;
            	posibilidades[qtd] = oeste;
            	qtd++;
            }
            
            if (!(leste == out)){
            	 int vLeste = tab[leste.y][leste.x];
            	 valores[qtd] = vLeste;
            	 posibilidades[qtd] = leste;
            	 qtd++;
            }
            
            //This part, select the minor value on the positions possibles
            int minimo = valores[0];
            Location posMin = posibilidades[0];
                    
            //Create a array with the mininum values
            int aux = 0;
            ArrayList<Integer> minimos = new ArrayList<Integer>();
            while (aux < qtd)
            {
            	//If the value is less than the others, clean the array, and put it in there
            	if(valores[aux] < minimo)
            	{
            		minimos = new ArrayList<Integer>();
            		minimos.add(aux);
            		minimo = valores[aux];
            		posMin = posibilidades[aux];
            	}
            	
            	//If already exist de same value in the array, (two values are the sames), put it in the array too
            	if(valores[aux] == minimo)
            	{
            		minimos.add(aux);
            		minimo = valores[aux];
            		posMin = posibilidades[aux];
            	}
            	aux++;
            }
           
            //Select a random direction
            Random generator = new Random();
            int sele = generator.nextInt(minimos.size());
            int cont = 0;
            int val = 0;
            Iterator<Integer> iter1 = minimos.iterator();
            while(cont <= sele){
                    val = (Integer) iter1.next();
                    cont ++;
            }
            
            //*******************//
            //The chose place is //
            
            minimo = valores[val];
    		posMin = posibilidades[val];
    		//*******************//
    		
            //In this moment, the information about places, the Matrices in LRTA*, are shared between all agents 
            if (tab[r1.y][r1.x] >= 0)
            {
            	//Add one to the place witch have the minimum value
                tab[r1.y][r1.x] = minimo + 1;
            }
            
            //Save the update on the file
            int typeSalv = 999;
            
           //Update the right matrix in the system
            if(dest.equals(ldrop1)){
            	tabDrop1 = tab;
            	typeSalv = 0;
            }
            else if(dest.equals(ldrop2)){
            	tabDrop2 =  tab;
            	typeSalv = 1;
            }
            else if(dest.equals(ltruck1)){
            	tabTruck = tab;
            	typeSalv = 2;
            }
            else if(dest.equals(ltruck2)){
            	tabTruck2 = tab;
            	typeSalv = 3;
            }
            else if(dest.equals(ltruck3)){
            	tabTruck3 = tab;
            	typeSalv = 4;
            }
            else if(dest.equals(lgarage)){
            	tabGarage = tab;
            	typeSalv = 5;
            }
            try {
    			//Save the matrice in the file
            	salvar(typeSalv, tab);
            	//Here compare the archive, if the optimal path was founded
    			//compareArchive(2);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            
            //Update the agent position
            setAgPos(code, posMin);

        if (view != null) {
            view.update(ldrop1.x,ldrop1.y);
            view.update(ldrop2.x,ldrop2.y);
            view.update(ltruck1.x,ltruck1.y);
            view.update(ltruck2.x,ltruck2.y);
            view.update(ltruck3.x,ltruck3.y);
            view.update(lgarage.x,lgarage.y);
        }
        return true;
    }	
}
