package collaborative;

import java.math.BigDecimal;

public class Parameters {
	private static double lambda = 0.5;
	
	public static BigDecimal getLambda(){
		return new BigDecimal(lambda);
	}
}