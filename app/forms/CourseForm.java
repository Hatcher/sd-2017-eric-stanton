package forms;

import models.common.Institution;
import models.common.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class CourseForm {

	public Long instructorId;
	public String name;
	public String description;
	public String start;
	public String end;
	public boolean isShared;
	public String coInstructors;
	public Long institutionId;


	public CourseForm() {}
	public CourseForm(Long instructorId, String name, String description, String start, String end, Long institutionId, boolean isShared, String coInstructors) {
		this.instructorId = instructorId;
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
		this.isShared = isShared;
		this.coInstructors = coInstructors;
		this.institutionId = institutionId;
	}

	public String validate() {

		if(start.equals("")) { start = "NULL"; }
		if(end.equals("")) { end = "NULL"; }
		if(coInstructors.equals("")) { coInstructors = "-1"; }

		if (name.length() <= 0) {
			return "No course name specified! Please provide one, then try again.";
		}

		if (name.length() >= 50) {
			return "Course name is too long! Please provide a smaller one.";
		}

		SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy");

		try {
			dateParser.parse(start);
		} catch (ParseException e) {
			return "Invalid start date. Please choose a valid start and end date from the calendars.";
		}

		try {
			dateParser.parse(end);
		} catch (ParseException e) {
			return "Invalid end date. Please choose a valid end date from the calendar.";
		}

		return null;
	}

}
