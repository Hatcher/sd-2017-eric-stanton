package services.question;

import com.avaje.ebean.annotation.Transactional;

import beans.math.question.MathQuestionBean;
import beans.math.question.QuestionRootNode;
import models.math.MathQuestion;


public class QuestionService{
	@Transactional
	public boolean create(QuestionRootNode questionNode) {
		MathQuestionBean questionBean = questionNode.getQuestion();
		MathQuestion question = questionBean.toEntity();
		MathQuestion.create(question);
		return true;
	}
}
