package models.math;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.*;

/////@Entity
public class Category extends Model {
	
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	private Long categoryId;
	
	private String name;
	
	private String description;
	
	// TODO mapped by situation
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Requirement> requirements = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Requirement> requiredBy = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<MathQuestion> questions = new HashSet<>();

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Requirement> getRequirements() {
		return requirements;
	}
	public Set<Requirement> getRequiredBy() {
		return requiredBy;
	}
	public Set<MathQuestion> getQuestions() {
		return questions;
	}
}
	