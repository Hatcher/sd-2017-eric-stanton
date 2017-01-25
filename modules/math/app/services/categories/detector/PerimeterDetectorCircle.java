package services.categories.detector;

import java.math.BigDecimal;
import java.math.BigInteger;

import beans.math.MathBean;

public class PerimeterDetectorCircle extends CategoryDetector {

	@Override
	public boolean isCategory(MathBean mathBean) {
		if (containsOnlyMultiplication(mathBean)) {
			if (mathBean.getOperators().size() == 2) {

				if (mathBean.getIntegers().contains(TWO) && mathBean.getIntegers().contains(THREE)) {
					// 2 * pi * radius
					return true;
				}

			}
			if (mathBean.getOperators().size() == 1) {
				if (mathBean.getIntegers().contains(THREE)) {
					// pi * diameter
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void label(MathBean mathBean) {
		boolean containsTwo = false;
		boolean containsThree = false;
		if (mathBean.getOperators().size() == 2) {
			for (BigDecimal integer : mathBean.getIntegers()) {
				if (integer.equals(TWO)){
					if (containsTwo){
						mathBean.getLabels().clear();
						mathBean.getLabels().put("radius", integer+"");
						return;
					}
					containsTwo = true;
				}
				else if (integer.equals(THREE))
				{
					if (containsThree){
						mathBean.getLabels().clear();
						mathBean.getLabels().put("radius", integer+"");
						return;
					}
					containsThree = true;
				}
				else{
					mathBean.getLabels().clear();
					mathBean.getLabels().put("radius", integer+"");
					return;
				}
			}
		} else {
			for (BigDecimal integer : mathBean.getIntegers()) {
				if (integer.equals(THREE))
				{
					if (containsThree){
						mathBean.getLabels().clear();
						mathBean.getLabels().put("diameter", integer+"");
						return;
					}
					containsThree = true;
				}
				else{
					mathBean.getLabels().clear();
					mathBean.getLabels().put("diameter", integer+"");
					return;
				}
			}
		}
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		mathBean.setQuestion("Find the circumference of the following circle");
		mathBean.getIntegers().remove(THREE);
		mathBean.getIntegers().add(BigDecimal.valueOf(Math.PI));
	}

}
