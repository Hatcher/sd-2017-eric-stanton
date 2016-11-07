package forms;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ExerciseForm {

    public String name;
    public String description;
    public String start;
    public String type;
	public Long creatorId;

	public ExerciseForm() {}
	public ExerciseForm(Long creatorId, String name, String description, String start, String type) {
        this.creatorId = creatorId;
		this.name = name;
        this.description = description;
        this.start = start;
        this.type = type;
	}

	public String validate() {
        SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy");
        
        try {
            dateParser.parse(start);
        } catch (ParseException e) {
            return "Invalid start date. Please choose a valid start date from the calendar.";
        }

		return null;
	}

}
