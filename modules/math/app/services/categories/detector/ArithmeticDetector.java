package services.categories.detector;

import java.util.ArrayList;
import java.util.List;

import beans.math.MathBean;
import services.equation.Operators;

public class ArithmeticDetector extends CategoryDetector {

	private List<String> enabledOperators = new ArrayList<String>();

	public ArithmeticDetector(List<String> enabledOperators) {
		for (String oper : enabledOperators) {
			this.enabledOperators.add(oper);
		}
	}

	@Override
	public boolean isCategory(MathBean mathBean) {
		// contains only the enabled operators
		for (String operator : mathBean.getOperators()) {
			if (!enabledOperators.contains(operator)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void label(MathBean mathBean) {
		// no labels for arithmetic
		
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		String question = "<strong>Solve the following:</strong>";
		if ((mathBean.getIntegers().size() == 2) && (!mathBean.getOperators().contains(Operators.DIVIDE))){
			question = "<div style=\"width:20px; text-align:right; border-bottom-style:solid; border-bottom-color:#000000; border-bottom-width:1px\">";
			question+=""+mathBean.getIntegers().get(0);
			question+="<br/>"+mathBean.getOperators().get(0)+""+mathBean.getIntegers().get(1);
			question+="</div>";
		}
		else{
			question = mathBean.toString();
		}
		mathBean.setQuestion(question);
		
	}
}
