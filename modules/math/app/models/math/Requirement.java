package models.math;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;

import javax.persistence.*;
import java.util.*;

//@Entity
public class Requirement extends Model {
	
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	public Long requirementId;
	
	// id of category
	public long categoryId;
	
	// id of requirement of category
	public long requirementsCategoryId;
	

	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Requirement> find = new Finder<>(Requirement.class);











}
	