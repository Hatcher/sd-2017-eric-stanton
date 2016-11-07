package models.math;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;

import javax.persistence.*;
import java.util.*;

//@Entity
public class Test extends Model {
	
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	private Long testId;
	
	private long userId;
	
	private long grade;
	
	private Date completedDate;
	
// TODO question
	@OneToMany(cascade = CascadeType.ALL)
	private Set<MathQuestion> questions = new HashSet<>();

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getGrade() {
		return grade;
	}

	public void setGrade(long grade) {
		this.grade = grade;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public Set<MathQuestion> getQuestions() {
		return questions;
	}
	













}
	