package services.categories.detector;

import java.util.ArrayList;
import java.util.List;

import beans.math.MathBean;

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
}
