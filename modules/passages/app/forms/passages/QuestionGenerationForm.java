package forms.passages;

public class QuestionGenerationForm {
	public String passage;
	public String num;
	public String exerciseId;

	public QuestionGenerationForm() {}
	public QuestionGenerationForm(String exerciseId, String passage, String num) {
		this.exerciseId = exerciseId;
		this.passage = passage;
		this.num = num;
	}

	public String validate() {
		if (passage.length() <= 0) {
			return "Please provide a message!";
		}

		if (num.length() <= 0) {
			return "Please choose a maximum number of question to generate!";
		}

		return null;
	}

}
