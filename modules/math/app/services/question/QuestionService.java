package services.question;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.avaje.ebean.annotation.Transactional;

import beans.math.question.Label;
import beans.math.question.MathQuestionBean;
import beans.math.question.QuestionRootNode;
import beans.math.question.Rule;
import models.math.MathQuestion;
import models.math.MathQuestionLabel;
import models.math.MathQuestionRule;
import play.mvc.Controller;
import util.math.constants.ExpressionPatterns;

public class QuestionService extends Controller {
	@Transactional
	public boolean create(QuestionRootNode questionNode) {
		MathQuestionBean questionBean = questionNode.getQuestion();
		removeConstants(questionBean);
		MathQuestion question = beanToEntity(questionBean);
		MathQuestion.create(question);
		return true;
	}

	private MathQuestion beanToEntity(MathQuestionBean bean) {
		MathQuestion entity = new MathQuestion();

		entity.equation = bean.getEquation();
		entity.imageUrl = bean.getImageUrl();
		entity.questionName = bean.getQuestionName();
		entity.questionText = bean.getQuestionText();
		entity.skillId = bean.getSkillId();
		entity.treeId = "";
		entity.rules = new ArrayList<MathQuestionRule>();

		for (Rule rule : bean.getRules()) {
			if (!"".equals(rule.getRule())) {
				MathQuestionRule ruleEntity = new MathQuestionRule();
				ruleEntity.ruleText = rule.getRule();
				entity.rules.add(ruleEntity);
			}
		}
		entity.labels = new ArrayList<MathQuestionLabel>();
		for (Label label : bean.getLabels()) {
			MathQuestionLabel labelEntity = new MathQuestionLabel();
			labelEntity.variableName = label.getValue();
			labelEntity.x = label.getX();
			labelEntity.y = label.getY();
			entity.labels.add(labelEntity);
		}

		return entity;
	}

	// replaces constants with variables and creates a rule to enforce the
	// constant as a rule
	private void removeConstants(MathQuestionBean bean) {
		List<String> variables = new ArrayList<>();
		bean.setEquation(bean.getEquation().replaceAll("\\s", ""));
		Pattern variablePattern = Pattern.compile(ExpressionPatterns.VARIABLE_PATTERN);
		Matcher variablesMatcher = variablePattern.matcher(bean.getEquation());

		while (variablesMatcher.find()) {
			variables.add(variablesMatcher.group());
		}

		Pattern integerPattern = Pattern.compile(ExpressionPatterns.INTEGER_PATTERN);
		Matcher integerMatcher = integerPattern.matcher(bean.getEquation());

		char variable = 'a';
		while (integerMatcher.find()) {
			while (variables.contains(variable + "")) {
				variable++;
			}
			String startingEquation = bean.getEquation();
			String endingEquation = bean.getEquation().replaceFirst(ExpressionPatterns.INTEGER_PATTERN, Matcher.quoteReplacement("${"+variable+"}"));
			if (startingEquation != endingEquation) {
				bean.setEquation(endingEquation);
				// add constant rule
				Rule rule = new Rule();
				rule.setRule("${" + variable + "}==" + integerMatcher.group());
				bean.getRules().add(rule);

				variables.add(variable + "");
			} else {
				// no more constants left in equation
				break;
			}
		}
	}
}
