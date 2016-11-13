package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class AreaDetectorRectangle extends CategoryDetector {
//Area = Length * Width
	
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
