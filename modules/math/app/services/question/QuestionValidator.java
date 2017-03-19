package services.question;

import java.util.ArrayList;
import java.util.List;

import beans.math.question.MathQuestionBean;
import beans.math.question.QuestionRootNode;
import beans.math.question.Rule;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import play.mvc.Controller;

public class QuestionValidator extends Controller {
	public List<String> validate(QuestionRootNode questionNode) {
		List<String> errors = new ArrayList<String>();
		errors = validateRequiredFields(questionNode, errors);
		
		if (!errors.isEmpty()){
			return errors;
		}
		else{
			MathQuestionBean question = questionNode.getQuestion();
			Evaluator eval = new Evaluator();
			try {
				
				
				eval.evaluate(question.getEquation());
			} catch (EvaluationException e) {
				errors.add("equation is not solveable");
			}
			
			for (Rule rule : question.getRules()){
				try {
					eval.evaluate(rule.getRule());
				} catch (EvaluationException e) {
					errors.add("rule is not solveable");
				}
			}
		}
		
		
		return errors;
	}
	private List<String> validateRequiredFields(QuestionRootNode questionNode, List<String> errors) {
		if (questionNode == null){
			errors.add("question cannot be empty");
		}
		else{
			MathQuestionBean question = questionNode.getQuestion();
			if (question == null){
				errors.add("question cannot be empty");
			}
			else{
				if ((question.getEquation() == null)||("".equals(question.getEquation()))){
					errors.add("equation cannot be empty");
				}
				if ((question.getQuestionName() == null)||("".equals(question.getQuestionName()))){
					errors.add("question name cannot be empty");
				}
				if ((question.getQuestionText() == null)||("".equals(question.getQuestionText()))){
					errors.add("question text cannot be empty");
				}
				if ((question.getSkillId() == null)||("".equals(question.getSkillId()))){
					errors.add("skill id cannot be empty");
				}
			}
		}
		return errors;
	}
}
