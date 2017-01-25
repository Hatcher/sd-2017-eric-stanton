package services.categories.detector;

import java.math.BigDecimal;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class PerimeterDetectorSquare extends CategoryDetector {
	// Perimeter = 4 * side
	// Perimeter = x + x + x + x
	// Perimeter = 2 * x + 2 * x

	@Override
	public boolean isCategory(MathBean mathBean) {
		if ((mathBean.getOperators().size() == 1)) {
			// Perimeter = 4 * side
			if (mathBean.getIntegers().get(0).equals(BigDecimal.valueOf(4))
					|| mathBean.getIntegers().get(1).equals(BigDecimal.valueOf(4))) {
				return containsOnlyMultiplicationAndDivision(mathBean);
			}
		}
		if ((mathBean.getOperators().size() == 3)) {
			if (containsOnlyAddition(mathBean)) {
				// Perimeter = x + x + x + x
				BigDecimal x = mathBean.getIntegers().get(0);
				for (BigDecimal otherX : mathBean.getIntegers()) {
					if (!x.equals(otherX)) {
						return false;
					}
				}
				return true;
			}
			if (mathBean.getOperators().get(1).equals(Operators.ADD)) {
				// Perimeter = 2 * x + 2 * x
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
					if ((twoIndexFromExpression1 >= 0) && (twoIndexFromExpression2 >= 0)) {
						for (int i = 0; i < expression1.getIntegers().size(); i++) {
							if (i != twoIndexFromExpression1) {
								for (int j = 0; j < expression2.getIntegers().size(); j++) {
									if (j != twoIndexFromExpression2) {
										return expression1.getIntegers().get(i)
												.equals(expression2.getIntegers().get(j));
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void label(MathBean mathBean) {
		if ((mathBean.getOperators().size() == 1)) {
			mathBean.getLabels().put("side", mathBean.getIntegers().get(0)+"");
		}
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		mathBean.setQuestion("Find the perimeter of the following square");

	}


}
