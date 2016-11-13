package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class AreaDetectorParallelogram extends CategoryDetector {
// Area = Base * Height
		
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 1){
			if (containsOnlyMultiplicationAndDivision(mathBean)){
				return true;
			}
		}
		return false;
	}
}
