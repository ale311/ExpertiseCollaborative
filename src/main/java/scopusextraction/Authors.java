package scopusextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import neo4j.StartGraphDB;


public class Authors {
	
//	metodo che dato lo scopusid di un documento, torna un set di autori relativi a quel documento
	public static HashSet<String> getAuthorsFromDocument(String scopus_id, String apikey) throws IOException{
		
		HashSet<String> result = new HashSet<>();
		URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+apikey+"&httpAccept=application/json&view=full&field=authors");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		try {
			
			JsonArray jsonArray = 
					jsonElement
					.getAsJsonObject().get("abstracts-retrieval-response")
					.getAsJsonObject().get("authors")
					.getAsJsonObject().get("author")
					.getAsJsonArray();

			for(JsonElement jse : jsonArray){
				String authorId = jse.getAsJsonObject().get("@auid").toString();
				authorId = authorId.substring(1, authorId.length()-1);
//				System.out.println(authorId);
				result.add(authorId);
			}
			
		}catch(Exception e){
			
		}
		return result;
	}
// 	metodo che inizializza l'estrazione e la costruzione della mappa di mappe principale.
//	data la mappa di documenti e relativi valori associati, 
//	costruisce la mappa principale, come chiavi imposta gli scopusid (le chiavi della mappa passata in input)
//	come valore ha una mappa che contiene come chiavi gli autori del documento corrente e come valore il valore dell'autorità
	public static HashMap<String, HashMap<String, BigDecimal>> buildMap(HashMap<String, BigDecimal> hitsDocuments, String apikey) {
		// TODO Auto-generated method stub
		int i = 0;
		int j = 0;
		HashMap<String, HashMap<String, BigDecimal>> result = new HashMap<>();
		try {
			for(String s : hitsDocuments.keySet()){
				System.out.println(hitsDocuments.size());
				System.out.println(i);
				HashSet<String> currentAuthor = getAuthorsFromDocument(s, apikey);
//				lancio metodo che costruisce la mappa interna: autore e punteggio del paper, e lo restituisce a questo metodo
				HashMap<String, BigDecimal> aux = buildMapAux(currentAuthor, hitsDocuments.get(s));
//				infine in questo punto inserisco nella mappa principale tutto quello che ho estratto
				result.put(s, aux);
//				if(i>=5) break;
				System.out.println(i);
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String t : result.keySet()){
			
			System.out.println(result.get(t));
		}
		
		return result;
	}
	private static HashMap<String, BigDecimal> buildMapAux(HashSet<String> currentAuthor, BigDecimal bigDecimal) {
		// TODO Auto-generated method stub
		HashMap<String, BigDecimal> result = new HashMap<>();
		for(String autore : currentAuthor){
			result.put(autore, bigDecimal);
		}
		return result;
	}
}
