package beans.math.question;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import models.math.MathQuestion;
import models.math.MathQuestionLabel;
import models.math.MathQuestionRule;
import util.math.constants.ExpressionPatterns;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MathQuestionBean {
	private String questionName;
	private String questionText;
	private String equation;
	private String imageUrl;
	private String skillId;
	private List<Label> labels;
	private List<Rule> rules;

	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getEquation() {
		return equation;
	}
	public void setEquation(String equation) {
		this.equation = equation;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSkillId() {
		return skillId;
	}
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	public List<Label> getLabels() {
		return labels;
	}
	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	
	public MathQuestion toEntity() {
		// tidy up request for easier usage during question generation
		removeConstants();
		replaceImpliedMultiplication();
		
		MathQuestion entity = new MathQuestion();

		entity.equation = this.getEquation();
		entity.imageUrl = this.getImageUrl();
		entity.questionName = this.getQuestionName();
		entity.questionText = this.getQuestionText();
		entity.skillId = this.getSkillId();
		entity.treeId = "";
		entity.rules = new ArrayList<MathQuestionRule>();

		for (Rule rule : this.getRules()) {
			if (!"".equals(rule.getRule())) {
				MathQuestionRule ruleEntity = new MathQuestionRule();
				ruleEntity.ruleText = rule.getRule();
				entity.rules.add(ruleEntity);
			}
		}
		entity.labels = new ArrayList<MathQuestionLabel>();
		for (Label label : this.getLabels()) {
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
	private void removeConstants() {
		List<String> variables = new ArrayList<>();
		this.setEquation(this.getEquation().replaceAll("\\s", ""));
		Pattern variablePattern = Pattern.compile(ExpressionPatterns.VARIABLE_PATTERN);
		Matcher variablesMatcher = variablePattern.matcher(this.getEquation());

		while (variablesMatcher.find()) {
			variables.add(variablesMatcher.group().trim());
		}

		Pattern integerPattern = Pattern.compile(ExpressionPatterns.INTEGER_PATTERN);
		Matcher integerMatcher = integerPattern.matcher(this.getEquation());

		char variable = 'a';
		while (integerMatcher.find()) {
			while (variables.contains("${"+variable + "}")) {
				variable++;
			}
			String startingEquation = this.getEquation();
			String endingEquation = this.getEquation().replaceFirst(ExpressionPatterns.INTEGER_PATTERN, Matcher.quoteReplacement("${"+variable+"}"));
			if (startingEquation != endingEquation) {
				this.setEquation(endingEquation);
				// add constant rule
				Rule rule = new Rule();
				rule.setRule("${" + variable + "}==" + integerMatcher.group());
				this.getRules().add(rule);
				variables.add("${"+variable + "}");
			} else {
				// no more constants left in equation
				break;
			}
		}
	}
	private void replaceImpliedMultiplication(){
		this.setEquation(this.getEquation().replaceAll(Matcher.quoteReplacement("}$"), Matcher.quoteReplacement("}*$")));
	}
	
}
