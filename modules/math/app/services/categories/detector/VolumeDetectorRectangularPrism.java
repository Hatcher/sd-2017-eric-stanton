package services.categories.detector;

import java.math.BigDecimal;
import java.math.RoundingMode;

import beans.math.MathBean;
import services.equation.Operators;

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
		mathBean.getLabels().put("depth", mathBean.getIntegers().get(0)+"");
		if (Operators.MULTIPLY.equals(mathBean.getOperators().get(0))){
			mathBean.getLabels().put("height", mathBean.getIntegers().get(1)+"");
		}
		else{
			mathBean.getLabels().put("height", BigDecimal.ONE.divide(mathBean.getIntegers().get(1),2, RoundingMode.HALF_UP)+"");
		}
		
		if (Operators.MULTIPLY.equals(mathBean.getOperators().get(1))){
			mathBean.getLabels().put("width", mathBean.getIntegers().get(2)+"");
		}
		else{
			mathBean.getLabels().put("width", BigDecimal.ONE.divide(mathBean.getIntegers().get(2),2, RoundingMode.HALF_UP)+"");
		}
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		mathBean.setQuestion("Calculate the volume of the following rectangular prism");
	}
	
	
}
