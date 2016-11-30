package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class SurfaceAreaDetectorRectangularPrism extends CategoryDetector {
	// SA = 2*length*width + 2*length*height + 2*width*height
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 8) {
			if (mathBean.getOperators().get(2).equals(Operators.ADD)
					&& mathBean.getOperators().get(5).equals(Operators.ADD)) {
				MathBean expression1 = new MathBean();
				expression1.getOperators().add(mathBean.getOperators().get(0));
				expression1.getOperators().add(mathBean.getOperators().get(1));
				expression1.getIntegers().add(mathBean.getIntegers().get(0));
				expression1.getIntegers().add(mathBean.getIntegers().get(1));
				expression1.getIntegers().add(mathBean.getIntegers().get(2));

				MathBean expression2 = new MathBean();
				expression2.getOperators().add(mathBean.getOperators().get(3));
				expression2.getOperators().add(mathBean.getOperators().get(4));
				expression2.getIntegers().add(mathBean.getIntegers().get(3));
				expression2.getIntegers().add(mathBean.getIntegers().get(4));
				expression2.getIntegers().add(mathBean.getIntegers().get(5));

				MathBean expression3 = new MathBean();
				expression3.getOperators().add(mathBean.getOperators().get(6));
				expression3.getOperators().add(mathBean.getOperators().get(7));
				expression3.getIntegers().add(mathBean.getIntegers().get(6));
				expression3.getIntegers().add(mathBean.getIntegers().get(7));
				expression3.getIntegers().add(mathBean.getIntegers().get(8));

				if (containsOnlyMultiplication(expression1) && containsOnlyMultiplication(expression2)
						&& containsOnlyMultiplication(expression3)) {
					int location2Expression1 = find2(expression1);
					int location2Expression2 = find2(expression2);
					int location2Expression3 = find2(expression3);
					if ((location2Expression1 < 0) || (location2Expression2 < 0) || (location2Expression3 < 0)) {
						return false;
					}
					// final condition is each number has to exist in at least 1
					// other expression
					for (BigInteger integer : expression1.getIntegers()) {
						if (!expression2.getIntegers().contains(integer) && !expression3.getIntegers().contains(integer)){
							return false;
						}
					}
					for (BigInteger integer : expression2.getIntegers()) {
						if (!expression1.getIntegers().contains(integer) && !expression3.getIntegers().contains(integer)){
							return false;
						}
					}

					for (BigInteger integer : expression2.getIntegers()) {
						if (!expression1.getIntegers().contains(integer) && !expression2.getIntegers().contains(integer)){
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void label(MathBean mathBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		// TODO Auto-generated method stub
		
	}

}
