package discharge_truck;
import java.io.BufferedReader;
import java.io.DataOutputStream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Planner {
	public List<String> sendingPostRequest() throws Exception {
		 
		//Faz o request no planner online
		  String url = "http://solver.planning.domains/solve";
		  URL obj = new URL(url);
		  HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		 
		        // Setting basic post request
		  con.setRequestMethod("POST");
		  //con.setRequestProperty("User-Agent", USER_AGENT);
		  con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		  con.setRequestProperty("Content-Type","application/json");
		 
		  String postJsonData = Planner.readArchive();
		  
		  //System.out.println(postJsonData);
		  // Send post request
		  con.setDoOutput(true);
		  DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		  wr.writeBytes(postJsonData);
		  wr.flush();
		  wr.close();
		 
		  int responseCode = con.getResponseCode();
		  //System.out.println("nSending 'POST' request to URL : " + url);
		  //System.out.println("Post Data : " + postJsonData);
		  //System.out.println("Response Code : " + responseCode);
		 
		  BufferedReader in = new BufferedReader(
		          new InputStreamReader(con.getInputStream()));
		  
		  String output;
		  StringBuffer response = new StringBuffer();
		  String result = "";
		  while ((output = in.readLine()) != null) {
		   response.append(output.trim());
		   //System.out.println(output.trim());
		   result += output.trim();
		  }
		  in.close();
		  //Transforma o resultado em JSON, e salva no arquivo saida
		  List<String> plano = new ArrayList<String>();
		  
		  try {
				 FileWriter arq = new FileWriter("saida.txt");
				 PrintWriter gravarArq = new PrintWriter(arq);   
			     JSONObject jsonObject1 = new JSONObject(result);
			     System.out.println("RESULTADOOOOOOOOOOOOOOOOOOOOOOOOOOO CAYYYYY");
			     //System.out.println(jsonObject1);
				 JSONObject resultadoJson = new JSONObject(jsonObject1.get("result").toString());
				 JSONArray jArray = (JSONArray) resultadoJson.get("plan");
				 for(int i = 0; i < jArray.length(); i++){
		                JSONObject o = jArray.getJSONObject(i);
		                gravarArq.printf(o.get("name").toString());
		                plano.add(o.get("name").toString());
		              
		               
		            }
				 arq.close();
			}catch (JSONException err){
				System.out.println("IHHHHHH RAPAIZ, DEU RUIM");
			}
		  return plano;
		 }
	

	
	public static String readArchive () throws IOException
	{
	      FileReader arq = new FileReader("domainX.txt");
	      BufferedReader lerArq = new BufferedReader(arq);
	      String linha = lerArq.readLine(); 
	      String domain = "";
	      //Put the informations that have collected on the file in the matrix
	      while (linha != null) { 
	        //System.out.printf("%s\n", linha);
	        domain = domain.concat(linha);
	        //domain = domain.concat("\n"); 
	        linha = lerArq.readLine();
	      }
	      arq.close();
	      
	      arq = new FileReader("problemX.txt");
	      lerArq = new BufferedReader(arq);
	      linha = lerArq.readLine(); 
	      
	      String problem = "";
	      //Put the informations that have collected on the file in the matrix
	      while (linha != null) { 
	        //System.out.printf("%s\n", linha);
	        problem = problem.concat(linha);
	        //problem = problem.concat("\n"); 
	        linha = lerArq.readLine();
	      }
	      arq.close();
	    
	      //JSONObject dataInfo = new JSONObject();
	      String data = "{\n" + "\"domain\": \" " +  domain + "\",\n" +
			        "    \"problem\": \""+ problem + "\" \n}";
	        //preenche o objeto com os campos: titulo, ano e genero
	      //dataInfo.put("domain", domain);
	      //dataInfo.put("problem", problem);
	        
		return data;
		
	}
	
}