package services.categories.detector;

import java.math.BigDecimal;
import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class PerimeterDetectorTriangle extends CategoryDetector {
	// Perimeter = a + b + c
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 2) {
			return containsOnlyAddition(mathBean);
		}

		return false;
	}

	public void label(MathBean mathBean) {
		mathBean.getLabels().clear();
		mathBean.getLabels().put("base", getShortest(mathBean)+"");
		mathBean.getLabels().put("hypoteneuse", getLongest(mathBean)+"");
		mathBean.getLabels().put("height", getMedium(mathBean)+"");
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		mathBean.setQuestion("Find the perimeter of the triangle");

	}
	
	private BigDecimal getLongest(MathBean mathBean) {
		BigDecimal longestLine = null;
		for (int i = 0; i < mathBean.getIntegers().size(); i++) {
			if (longestLine == null) {
				longestLine = new BigDecimal(mathBean.getIntegers().get(i));
			} else {
				BigDecimal comparator = new BigDecimal(mathBean.getIntegers().get(i));
				if (longestLine.compareTo(comparator) < 0) {
					longestLine = comparator;
				}
			}
		}
		return longestLine;
	}

	private BigDecimal getMedium(MathBean mathBean) {
		BigDecimal longestLine = null;
		BigDecimal shortestLine = null;
		BigDecimal mediumLine = null;
		for (int i = 0; i < mathBean.getIntegers().size(); i++) {
			if (longestLine == null) {
				longestLine = new BigDecimal(mathBean.getIntegers().get(i));
				shortestLine = new BigDecimal(mathBean.getIntegers().get(i));
				mediumLine = new BigDecimal(mathBean.getIntegers().get(i));
			} else {
				BigDecimal comparator = new BigDecimal(mathBean.getIntegers().get(i));
				if (longestLine.compareTo(comparator) < 0) {
					longestLine = comparator;
				}
				if (shortestLine.compareTo(comparator) > 0) {
					shortestLine = comparator;
				}
				if ((mediumLine.compareTo(comparator) > 0) && (mediumLine.compareTo(comparator) < 0)) {
					mediumLine = comparator;
				}
			}
		}
		return mediumLine;
	}

	private BigDecimal getShortest(MathBean mathBean) {
		BigDecimal shortestLine = null;
		for (int i = 0; i < mathBean.getIntegers().size(); i++) {
			if (shortestLine == null) {
				shortestLine = new BigDecimal(mathBean.getIntegers().get(i));
			} else {
				BigDecimal comparator = new BigDecimal(mathBean.getIntegers().get(i));
				if (shortestLine.compareTo(comparator) > 0) {
					shortestLine = comparator;
				}
			}
		}
		return shortestLine;
	}



}
