package beans.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import services.equation.Operators;

public class MathBean {
	private String id="0";
	private String type = "";
	private String question = "";
	private String imageUrl = "";
	private List<LabelBean> labels = new ArrayList<LabelBean>();


	private List<BigDecimal> integers = new ArrayList<BigDecimal>();
	private List<String> operators = new ArrayList<String>();


	public MathBean() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<BigDecimal> getIntegers() {
		return integers;
	}

	public List<String> getOperators() {
		return operators;
	}

	public String getAnswer() {
		Evaluator eval = new Evaluator();
		try {
			return eval.evaluate(this.toString());
		} catch (EvaluationException e) {
			return "";
		}
	}

	public Set<String> getAnswerChoices() {

		// decimal format
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		// ^decimal format

		Set<String> answers = new HashSet<String>();

		BigDecimal answer;
		try {
			answer = (BigDecimal) decimalFormat.parse(getAnswer());
			answer = answer.setScale(2, RoundingMode.HALF_UP);
			
			answers.add(answer.stripTrailingZeros().toEngineeringString());
			Evaluator eval = new Evaluator();
			Random random = new Random();

			while (answers.size() < 4) {
				MathBean wrongBean = new MathBean();

				wrongBean.getIntegers().addAll(cloneIntegers());
				wrongBean.getOperators().addAll(cloneOperators());

				if (random.nextInt(2) > 0) {
					wrongBean.getIntegers().set(random.nextInt(wrongBean.getIntegers().size()),
							BigDecimal.valueOf(random.nextInt(9) + 1));
				} else {
					wrongBean.getOperators().set(random.nextInt(wrongBean.getOperators().size()),
							Operators.ALL_OPERATORS.get(random.nextInt(Operators.ALL_OPERATORS.size())));
				}

				BigDecimal wrongAnswer = (BigDecimal) decimalFormat.parse(wrongBean.getAnswer());
				wrongAnswer = wrongAnswer.setScale(2, RoundingMode.HALF_UP);
				answers.add(wrongAnswer.stripTrailingZeros().toEngineeringString());

			}
		} catch (ParseException e) {
			return new HashSet<String>();
		}
		return answers;

	}

	private List<BigDecimal> cloneIntegers() {
		List<BigDecimal> wrongIntegers = new ArrayList<BigDecimal>();
		for (BigDecimal tmp : getIntegers()) {
			wrongIntegers.add(BigDecimal.valueOf(tmp.intValue()));
		}
		return wrongIntegers;
	}

	private List<String> cloneOperators() {
		List<String> wrongOperators = new ArrayList<String>();
		for (String tmp : getOperators()) {
			wrongOperators.add(tmp);
		}
		return wrongOperators;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String toString() {
		String rValue = "";
		for (int i = 0; i < getOperators().size(); i++) {
			if (rValue.equals("")) {
				rValue += getIntegers().get(i) + " " + getOperators().get(i) + " " + getIntegers().get(i + 1);
			} else {
				rValue += getOperators().get(i) + " " + getIntegers().get(i + 1);
			}
		}
		return rValue;
	}

	public List<LabelBean> getLabels() {
		return labels;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
