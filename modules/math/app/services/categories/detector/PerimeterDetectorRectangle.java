package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class PerimeterDetectorRectangle extends CategoryDetector {
	// Perimeter = x + x + y + y
	// Perimeter = 2 * length + 2 * width

	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 3) {
			if (containsOnlyAddition(mathBean)) {
				// Perimeter = x + x + y + y
				return hasDoublesTwice(mathBean);
			}
			if (containsOnlyAdditionAndMultiplication(mathBean)) {
				// Perimeter = 2 * length + 2 * width
				if (mathBean.getOperators().get(1).equals(Operators.ADD)) {
					MathBean expression1 = new MathBean();
					expression1.getOperators().add(mathBean.getOperators().get(0));
					expression1.getIntegers().add(mathBean.getIntegers().get(0));
					expression1.getIntegers().add(mathBean.getIntegers().get(1));

					MathBean expression2 = new MathBean();
					expression2.getOperators().add(mathBean.getOperators().get(2));
					expression2.getIntegers().add(mathBean.getIntegers().get(2));
					expression2.getIntegers().add(mathBean.getIntegers().get(3));

					if (containsOnlyMultiplication(expression1) && containsOnlyMultiplication(expression2)) {
						int twoIndexFromExpression1 = find2(expression1);
						int twoIndexFromExpression2 = find2(expression2);

						return ((twoIndexFromExpression1 >= 0) && (twoIndexFromExpression2 >= 0));

					}

				}
			}
		}
		return false;
	}

	private boolean hasDoublesTwice(MathBean mathBean) {
		BigInteger firstInteger = null;
		BigInteger secondInteger = null;
		for (BigInteger comparator : mathBean.getIntegers()) {
			if (firstInteger == null) {
				firstInteger = comparator;
			} else if (secondInteger == null) {
				if (!firstInteger.equals(comparator)) {
					secondInteger = comparator;
				}
			} else {
				if (!comparator.equals(firstInteger) && !comparator.equals(secondInteger)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void label(MathBean mathBean) {
		if (containsOnlyAddition(mathBean)) {
			BigInteger firstInteger = null;
			BigInteger secondInteger = null;
			for (BigInteger comparator : mathBean.getIntegers()) {
				if (firstInteger == null) {
					firstInteger = comparator;
				} else if (secondInteger == null) {
					if (!firstInteger.equals(comparator)) {
						secondInteger = comparator;
					}
				} 
			}
			mathBean.getLabels().put("width", firstInteger+"");
			mathBean.getLabels().put("length", secondInteger+"");
		}
		else{
			MathBean expression1 = new MathBean();
			expression1.getOperators().add(mathBean.getOperators().get(0));
			expression1.getIntegers().add(mathBean.getIntegers().get(0));
			expression1.getIntegers().add(mathBean.getIntegers().get(1));

			if (expression1.getIntegers().get(0).equals(BigInteger.ONE.add(BigInteger.ONE))){
				mathBean.getLabels().put("width", expression1.getIntegers().get(1)+"");
			}
			else{
				mathBean.getLabels().put("width", expression1.getIntegers().get(0)+"");
			}

			MathBean expression2 = new MathBean();
			expression2.getOperators().add(mathBean.getOperators().get(2));
			expression2.getIntegers().add(mathBean.getIntegers().get(2));
			expression2.getIntegers().add(mathBean.getIntegers().get(3));
			
			if (expression2.getIntegers().get(0).equals(BigInteger.ONE.add(BigInteger.ONE))){
				mathBean.getLabels().put("width", expression2.getIntegers().get(1)+"");
			}
			else{
				mathBean.getLabels().put("width", expression2.getIntegers().get(0)+"");
			}
		}

	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		mathBean.setQuestion("Find the perimeter of the rectangle.");
	}
}
