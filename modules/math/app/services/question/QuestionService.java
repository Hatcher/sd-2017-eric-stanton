package services.question;

import java.util.ArrayList;

import com.avaje.ebean.annotation.Transactional;

import beans.math.question.MathQuestionBean;
import beans.math.question.QuestionRootNode;
import beans.math.question.Rule;
import models.maths.MathQuestion;
import models.maths.RuleEntity;
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
		entity.rules = new ArrayList<RuleEntity>();
		for (Rule rule : bean.getRules()){
			RuleEntity ruleEntity = new RuleEntity();
			ruleEntity.ruleText = rule.getRule();
			entity.rules.add(ruleEntity);
		}
		return entity;
	}
}
