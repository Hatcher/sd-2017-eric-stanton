package services.categories.detector;

import java.math.BigInteger;

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

}
