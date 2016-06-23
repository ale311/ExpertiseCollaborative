package collaborative;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import javax.naming.spi.DirStateFactory.Result;

import org.neo4j.cypher.internal.compiler.v2_2.ast.rewriters.reattachAliasedExpressions;

import scopusextraction.LeggiCSV;

public class VisualizzaListaHits {
	public static void main(String [] x) throws IOException{
		String path = "util/author-hits.csv";
		HashMap<String, BigDecimal> authorityChart = new HashMap<>();
		HashMap<String, BigDecimal> authorityMax = new HashMap<>();
		HashMap<String, BigDecimal> authorityAvg = new HashMap<>();
		HashMap<String, BigDecimal> authoritySum = new HashMap<>();
		
//		authoritySum = LeggiCSV.getSumScore(path);
//		authorityMax = LeggiCSV.getMaxScore(path);
		authorityAvg = LeggiCSV.getAvgScore(path);
		
		
	}
}
