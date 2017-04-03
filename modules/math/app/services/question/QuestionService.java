package services.question;

import java.util.ArrayList;

import com.avaje.ebean.annotation.Transactional;

import beans.math.question.Label;
import beans.math.question.MathQuestionBean;
import beans.math.question.QuestionRootNode;
import beans.math.question.Rule;
import models.maths.MathQuestion;
import models.maths.MathQuestionLabel;
import models.maths.MathQuestionRule;
import play.mvc.Controller;

public class QuestionService extends Controller {
	@Transactional
	public boolean create(QuestionRootNode questionNode) {
		MathQuestion question = beanToEntity(questionNode);
		MathQuestion.create(question);
		return true;
	}
	
	private MathQuestion beanToEntity(QuestionRootNode questionRootNode){
		MathQuestion entity = new MathQuestion();
		MathQuestionBean bean = questionRootNode.getQuestion();
		
		entity.equation = bean.getEquation();
		entity.imageUrl = bean.getImageUrl();
		//bean.getLabels();
		entity.questionName = bean.getQuestionName();
		entity.questionText = bean.getQuestionText();
		//bean.getRules();
		entity.skillId = bean.getSkillId();
		entity.treeId = "";
		entity.rules = new ArrayList<MathQuestionRule>();
		
		for (Rule rule : bean.getRules()){
			MathQuestionRule ruleEntity = new MathQuestionRule();
			ruleEntity.ruleText = rule.getRule();
			entity.rules.add(ruleEntity);
		}
		entity.labels = new ArrayList<MathQuestionLabel>();
		for (Label label : bean.getLabels()){
			MathQuestionLabel labelEntity = new MathQuestionLabel();
			labelEntity.variableName = label.getValue();
			labelEntity.x = label.getX();
			labelEntity.y = label.getY();
			labels.add(labelEntity);
		}
		
		return entity;
	}
}
