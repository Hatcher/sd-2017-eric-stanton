package services.categories.detector;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class AreaDetectorTriangle extends CategoryDetector {
	// Area = Base * Height / 2

	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 2) {
			boolean divideBy2 = false;

			for (int i = 0; i < mathBean.getOperators().size(); i++) {
				if (!mathBean.getOperators().get(i).equals(Operators.MULTIPLY)
						&& !mathBean.getOperators().get(i).equals(Operators.DIVIDE)) {
					// Only multiplication and division supported in area of a
					// triangle
					return false;
				}
				if (mathBean.getOperators().get(i).equals(Operators.DIVIDE)) {
					if (mathBean.getIntegers().get(i + 1).equals(BigInteger.valueOf(2))) {
						divideBy2 = true;
					}
				}
			}
			// must have "divide by 2" scenario.
			return divideBy2;
		}
		return false;
	}

	public void label(MathBean mathBean){
	mathBean.getLabels().clear();
	mathBean.getLabels().put("base", getShortest(mathBean)+"");
	mathBean.getLabels().put("hypoteneuse", "");
	mathBean.getLabels().put("height", getLongest(mathBean)+"");
	//TODO create condition to remove the half condition (1/2 * base * height is the equation)
	
	}
	@Override
	public void populateQuestion(MathBean mathBean) {
		mathBean.setQuestion("Calculate the area of the triangle");
	}
	private BigDecimal getLongest(MathBean mathBean){
		BigDecimal longestLine = null;
		boolean foundDivBy2 = false;
		for (int i = 0; i < mathBean.getIntegers().size(); i++)
		{
			if (longestLine == null){
				longestLine = new BigDecimal(mathBean.getIntegers().get(i));
			}
			else{
				
				BigDecimal comparator;
				if (mathBean.getOperators().get(i-1).equals(Operators.DIVIDE)){
					
					if (!foundDivBy2){
						if (mathBean.getIntegers().get(i).equals(BigInteger.ONE.add(BigInteger.ONE))){
							foundDivBy2 = true; // for area, the divide by 2 is not a label
							continue;
						}
					}
					comparator = BigDecimal.ONE.divide(new BigDecimal(mathBean.getIntegers().get(i)), 2, RoundingMode.HALF_UP);
				}
				else{
					comparator = new BigDecimal(mathBean.getIntegers().get(i));
				}
				if (longestLine.compareTo(comparator) < 0){
					longestLine = comparator;
				}
				
			}
		}
		return longestLine;
	}
	private BigDecimal getMedium(MathBean mathBean){
		BigDecimal longestLine = null;
		BigDecimal shortestLine = null;
		BigDecimal mediumLine = null;
		boolean foundDivBy2 = false;
		for (int i = 0; i < mathBean.getIntegers().size(); i++)
		{
			if (longestLine == null){
				longestLine = new BigDecimal(mathBean.getIntegers().get(i));
				shortestLine = new BigDecimal(mathBean.getIntegers().get(i));
				mediumLine = new BigDecimal(mathBean.getIntegers().get(i));
			}
			else{
				BigDecimal comparator;
				if (mathBean.getOperators().get(i-1).equals(Operators.DIVIDE)){
					if (!foundDivBy2){
						if (mathBean.getIntegers().get(i).equals(BigInteger.ONE.add(BigInteger.ONE))){
							foundDivBy2 = true; // for area, the divide by 2 is not a label
							continue;
						}
					}
					comparator = BigDecimal.ONE.divide(new BigDecimal(mathBean.getIntegers().get(i)), 2, RoundingMode.HALF_UP);
				}
				else{
					comparator = new BigDecimal(mathBean.getIntegers().get(i));
				}
				if (longestLine.compareTo(comparator) < 0){
					longestLine = comparator;
				}
				if (mathBean.getOperators().get(i-1).equals(Operators.DIVIDE)){
					comparator = BigDecimal.ONE.divide(new BigDecimal(mathBean.getIntegers().get(i)), 2, RoundingMode.HALF_UP);
				}
				else{
					comparator = new BigDecimal(mathBean.getIntegers().get(i));
				}
				if (shortestLine.compareTo(comparator) > 0){
					shortestLine = comparator;
				}
				
				if (mathBean.getOperators().get(i-1).equals(Operators.DIVIDE)){
					comparator = BigDecimal.ONE.divide(new BigDecimal(mathBean.getIntegers().get(i)), 2, RoundingMode.HALF_UP);
				}
				else{
					comparator = new BigDecimal(mathBean.getIntegers().get(i));
				}
				if (mediumLine.compareTo(comparator) > 0){
					mediumLine = comparator;
				}
				
			}
		}
		return mediumLine;
	}
	
	private BigDecimal getShortest(MathBean mathBean){
		BigDecimal shortestLine = null;
		boolean foundDivBy2 = false;
		for (int i = 0; i < mathBean.getIntegers().size(); i++)
		{
			if (shortestLine == null){
				shortestLine = new BigDecimal(mathBean.getIntegers().get(i));
			}
			else{
				BigDecimal comparator;
				if (mathBean.getOperators().get(i-1).equals(Operators.DIVIDE)){
					if (!foundDivBy2){
						
						if (mathBean.getIntegers().get(i).equals(BigInteger.ONE.add(BigInteger.ONE))){
							foundDivBy2 = true; // for area, the divide by 2 is not a label
							continue;
						}
					}
					comparator = BigDecimal.ONE.divide(new BigDecimal(mathBean.getIntegers().get(i)), 2, RoundingMode.HALF_UP);
				}
				else{
					comparator = new BigDecimal(mathBean.getIntegers().get(i));
				}
				if (shortestLine.compareTo(comparator) > 0){
					shortestLine = comparator;
				}
			}
		}
		return shortestLine;
	}


	
	
	
	
}
