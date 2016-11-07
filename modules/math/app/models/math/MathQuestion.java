package models.math;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;


import javax.persistence.*;

import java.util.*;

//@Entity
public class MathQuestion extends Model {
	
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	private Long questionId;
	
	private long testId;
	
	private String questionText;
	
	private String answerText;
	
	private long categoryId;
	
	private long difficulty;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Test test;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Set<Category> tests = new HashSet<>();

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public long getTestId() {
		return testId;
	}

	public void setTestId(long testId) {
		this.testId = testId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public long getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(long difficulty) {
		this.difficulty = difficulty;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public Set<Category> getTests() {
		return tests;
	}

}
	