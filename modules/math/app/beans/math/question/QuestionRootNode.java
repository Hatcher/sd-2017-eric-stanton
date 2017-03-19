package beans.math.question;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionRootNode {
	private MathQuestionBean question;

	public MathQuestionBean getQuestion() {
		return question;
	}

	public void setQuestion(MathQuestionBean nodeStructure) {
		this.question = nodeStructure;
	}
	
}
