package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class VolumeDetectorRightPrism extends CategoryDetector {
// Volume = height * base * depth / 2
	
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 3){
			if (containsOnlyMultiplicationAndDivision(mathBean)){
				if (findDivideBy2Operator(mathBean) >= 0){
					return true;
				}
			}
		}
		return false;
	}
	
	
}
