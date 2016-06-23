package scopusextraction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jfree.data.Values;

import au.com.bytecode.opencsv.CSVReader;

public class LeggiCSV {
	static HashSet<String> getAbstractCSV(String position) throws IOException{
		HashSet<String> result = new HashSet<String>();
		CSVReader reader = new CSVReader(new FileReader(position));
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String s = nextLine[0];
			if (!s.equals("n")) {
				// nextLine[] is an array of values from the line
				s = s.substring(13, s.length()-2);

				//			System.out.println(s);
				result.add(s);
			}
		}
		return result;
	}
	static HashSet<String> getScopusIDCSV(String position) throws IOException{
		HashSet<String> result = new HashSet<String>();
		CSVReader reader = new CSVReader(new FileReader(position));
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String s = nextLine[0];
			if (!s.equals("n")) {

				//				 nextLine[] is an array of values from the line
				s = s.substring(3, s.length());

				System.out.println(s);
				result.add(s);
			}
		}
		return result;
	}

	static HashMap<String, String> getRelations(String position) throws IOException{
		HashMap<String, String> result = new HashMap<String, String>();
		CSVReader reader = new CSVReader(new FileReader(position));
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String s = nextLine[0];
			s = s.substring(2);
			String t = nextLine [1];
			t = t.substring(2);
			System.out.println("source "+s);
			System.out.println(("target "+t));

			result.put(s, t);
		}
		return result;
	}

	static HashMap getScore(String position) throws IOException{
		HashMap<String, BigDecimal> result = new HashMap<>();
		Reader in = new FileReader("util/sortedAuthorityWithError.csv");
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {
			String columnOne = record.get(0);
			String autore = columnOne.substring(3);
			String columnTwo = record.get(1);
			String auth = columnTwo.replace(",", ".");
			Double authority = Double.parseDouble(auth);
			BigDecimal a = new BigDecimal(auth);
			result.put(autore, a);
		}
		return result;
	}
	
	public static HashMap getScorePresentNEW(String position) throws IOException{
		HashMap<String, BigDecimal> result = new HashMap<>();
		String path = "util/sortedAuthorityWithError.csv";
		Reader in = new FileReader(position);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {
			String columnOne = record.get(0);
			String autore = columnOne.substring(3);
			String columnTwo = record.get(1);
			String auth = columnTwo.replace(",", ".");
			Double authority = Double.parseDouble(auth);
			BigDecimal a = new BigDecimal(auth);
			if(result.containsKey(autore)){
				if(result.get(autore).floatValue()>authority.floatValue()){
					//non fare nulla, ho il valore più alto già memorizzato nella mia mappa
				}
				else{
					result.put(autore, a);
				}
			}
			else{
				//questo ramo indica che non è presente nella mia mappa l'autore e quindi va memorizzato
				result.put(autore, a);
			}
		}
		return result;
	}
	public static HashMap getScorePresent(String position) throws IOException{
		HashMap<String, BigDecimal> result = new HashMap<>();
		String path = "util/sortedAuthorityWithError.csv";
		Reader in = new FileReader(position);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {
			
			String columnOne = record.get(0);
			String autore = columnOne.substring(3);
			String columnTwo = record.get(1);
			String auth = columnTwo.replace(",", ".");
			Double authority = Double.parseDouble(auth);
			BigDecimal a = new BigDecimal(auth);
			if(result.containsKey(autore)){
				if(result.get(autore).floatValue()>authority.floatValue()){
					//non fare nulla, ho il valore più alto già memorizzato nella mia mappa
				}
				else{
					result.put(autore, a);
				}
			}
			else{
				//questo ramo indica che non è presente nella mia mappa l'autore e quindi va memorizzato
				result.put(autore, a);
			}
		}
		return result;
	}
	public static HashMap<String, BigDecimal> getSumScore(String position) throws IOException {
		HashMap<String, BigDecimal> result = new HashMap<>();
		Reader in = new FileReader(position);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {
			String columnOne = record.get(0);
			String autore = columnOne.substring(3);
			String columnTwo = record.get(1);
			String auth = columnTwo.replace(",", ".");
			BigDecimal currentAuthority = new BigDecimal(auth);
			if(result.containsKey(autore)){
				//		    	ramo in cui trovo già un valore all'interno della mia mappa risultato
				BigDecimal inMapAuthority = result.get(autore);
				
				BigDecimal newValue = currentAuthority.add(inMapAuthority);
				System.out.println("nuovo valore: "+newValue);
				System.out.println("somma di: "+inMapAuthority+" e "+currentAuthority);
				result.put(autore, newValue);
//				System.out.println("valore di "+autore+" è stato sostituito con "+newValue);
			}
			else{
				//questo ramo indica che non è presente nella mia mappa l'autore e quindi va memorizzato
				result.put(autore, currentAuthority);
			}
		}
		return result;
	}
	
	
	public static HashMap<String, BigDecimal> getMaxScore(String position) throws IOException {
		HashMap<String, BigDecimal> result = new HashMap<>();
		Reader in = new FileReader(position);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {
			String columnOne = record.get(0);
			String autore = columnOne.substring(3);
			String columnTwo = record.get(1);
			String auth = columnTwo.replace(",", ".");
			BigDecimal currentAuthority = new BigDecimal(auth);
//			Double currentAuthority = Double.parseDouble(auth);
			if(result.containsKey(autore)){
				BigDecimal inMapAuthority = result.get(autore);
				if(currentAuthority.compareTo(inMapAuthority)>0){
					System.out.println("sostituisco "+inMapAuthority);
					System.out.println("con "+currentAuthority);
//				if(result.get(autore).floatValue()<currentAuthority.floatValue()){
//					in questo caso, il nuovo valore trovato deve sostituire il vecchio valore, che gli è inferiore
					result.put(autore, currentAuthority);
				}
				else{
					System.out.println("NON sostituisco "+inMapAuthority);
					System.out.println("con "+currentAuthority);
					result.put(autore, inMapAuthority);
					//			    		in questo ramo, l'autore è presente nella mia mappa con un valore più alto
					//			    		System.out.println("non sostituisco "+result.get(autore)+" perché maggiore di "+new BigDecimal(authority));
				}
			}
			else{
				//questo ramo indica che non è presente nella mia mappa l'autore e quindi va memorizzato
				result.put(autore, currentAuthority);
			}
		}
		return result;
	}
	public static HashMap<String, BigDecimal> getAvgScore(String position) throws IOException {
		HashMap<String, BigDecimal> result = new HashMap<>();
		HashMap<String, Integer> tempOccurrence = new HashMap<>();
		Reader in = new FileReader(position);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {
			String columnOne = record.get(0);
			String autore = columnOne.substring(3);
			String columnTwo = record.get(1);
			String auth = columnTwo.replace(",", ".");
			Double authority = Double.parseDouble(auth);
			BigDecimal a = new BigDecimal(auth);
			
			if(result.containsKey(autore)){
				if(tempOccurrence.containsKey(autore)){
					int vecchiaOccorrenza = tempOccurrence.get(autore);
					int nuovaOccorrenza = vecchiaOccorrenza+1;
					tempOccurrence.put(autore, nuovaOccorrenza);
				}
				else{
					tempOccurrence.put(autore,1);
				}
				//		    	ramo in cui trovo già un valore all'interno della mia mappa risultato
				BigDecimal currValue = result.get(autore);
				BigDecimal newValue = a.add(currValue);
				result.put(autore, newValue);
			}
			else{
				//questo ramo indica che non è presente nella mia mappa l'autore e quindi va memorizzato
				result.put(autore, a);
				tempOccurrence.put(autore, 1);
			}
		}
//		System.out.println("somma di 13005355500: "+result.get("13005355500"));
//		a questo punto eseguo la media
//		scansiono la mappa delle occorrenze e per ogni voce modifico la mappa delle somme
		for(String currAuthor : tempOccurrence.keySet()){
			
			int occorrenze = tempOccurrence.get(currAuthor);
			BigDecimal occ = new BigDecimal(occorrenze);
			BigDecimal somma = result.get(currAuthor);
			float f_somma = somma.floatValue();
			float f_media = f_somma/occorrenze;
			BigDecimal media = new BigDecimal(f_media);
//			ora memorizzo il nuovo valore che andrà a sostituire la somma con la media
			result.put(currAuthor, media);
		}
//		System.out.println();
//		System.out.println("media di 13005355500: "+ result.get("13005355500"));
		return result;
	}
}

