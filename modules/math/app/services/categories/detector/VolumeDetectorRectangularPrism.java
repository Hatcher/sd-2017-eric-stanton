package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class VolumeDetectorRectangularPrism extends CategoryDetector {
// Volume = Length * Width * Height
	
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 2){
			return containsOnlyMultiplicationAndDivision(mathBean);
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
