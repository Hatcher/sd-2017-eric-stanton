package services.question;

import com.avaje.ebean.annotation.Transactional;

import beans.math.question.MathQuestionBean;
import beans.math.question.QuestionRootNode;
import models.maths.MathQuestion;
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
		return entity;
	}
}
