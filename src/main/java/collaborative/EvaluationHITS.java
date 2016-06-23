package collaborative;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.plaf.synth.SynthSpinnerUI;

import scopusextraction.LeggiCSV;
import scopusextraction.ScriviCSV;
import scopusextraction.Util;
import authorOperations.*;
import scala.annotation.meta.param;


public class EvaluationHITS {
	static String readPath = "util/author-hits.csv";


	public static void main(String [] x) throws IOException{
//		buildCSV(readPath);
		String authorId = resolveScopusName(Util.getSampleLastName(), Util.getSampleFirstName(), Util.getSampleAffiliation());
		BigDecimal maxAuthority = findMaxAuthority(authorId);
		BigDecimal sumAuthority = findSumAuthority(authorId);
//		System.out.println("----------");
//		System.out.println(maxAuthority);
//		System.out.println(sumAuthority);
		System.out.println(getWeightedAuthority(authorId));		
	}
	
	private static BigDecimal getWeightedAuthority(String authorId){
		BigDecimal result=new BigDecimal(0);
		
		BigDecimal maxAuthority = findMaxAuthority(authorId);
		BigDecimal sumAuthority = findSumAuthority(authorId);
		result = maxAuthority.multiply(Parameters.getLambda())
			.add(sumAuthority.multiply(Parameters.getLambda()));
		
		return result;
		
		
	}
	private static BigDecimal findSumAuthority(String authorId) {
		// TODO Auto-generated method stub
		HashMap<String, BigDecimal> authoritySum = new HashMap<>();
		try {
			authoritySum = LeggiCSV.getScorePresentNEW("util/author_hits_SUM.csv");
			if(authoritySum.containsKey(authorId)){
				return authoritySum.get(authorId);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static BigDecimal findMaxAuthority(String authorId) {
		// TODO Auto-generated method stub
		HashMap<String, BigDecimal> authorityMax = new HashMap<>();
		try {
			authorityMax = LeggiCSV.getScorePresentNEW("util/author_hits_MAX.csv");
			if(authorityMax.containsKey(authorId)){
				return authorityMax.get(authorId);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void buildCSV(String readPath){
		try {
			String writePathSUM = "util/author_hits_SUM.csv";
			String writePathMAX = "util/author_hits_MAX.csv";
			String writePathAVG = "util/author_hits_AVG.csv";
			HashMap<String, BigDecimal> authorityChart = new HashMap<>();
			HashMap<String, BigDecimal> authorityMax = new HashMap<>();
			HashMap<String, BigDecimal> authorityAvg = new HashMap<>();
			HashMap<String, BigDecimal> authoritySum = new HashMap<>();

			authoritySum = LeggiCSV.getSumScore(readPath);
			authorityMax = LeggiCSV.getMaxScore(readPath);
			authorityAvg = LeggiCSV.getAvgScore(readPath);

			ScriviCSV.writeMapCSV(authoritySum, writePathSUM, "authoritySUM");
			ScriviCSV.writeMapCSV(authorityAvg, writePathAVG, "authorityAVG");
			ScriviCSV.writeMapCSV(authorityMax, writePathMAX, "authorityMAX");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

	}

	public static String resolveScopusName (String lastName, String firstName, String affiliationName){
		String authorId = null;
		String APIKEY = scopusextraction.Util.getKey();
		try {
			HashSet<Author> resultHashSet = SearchAuthorIdByName.searchIDbyName(lastName, firstName, affiliationName, APIKEY);
			authorId = resultHashSet.iterator().next().getAuthorID();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(authorId);
		return authorId;
	}
}

