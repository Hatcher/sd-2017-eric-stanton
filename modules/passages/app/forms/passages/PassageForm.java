package forms.passages;

public class PassageForm {

	public Long uploaderId;
    public String title;
	public String source;
	public String text;
	public String tags;
	public Long level;
	public String type;
	public Boolean isGlobal;

	public PassageForm() {}
	public PassageForm(Long uploaderId, String title, String source, String text, String tags, Long level, String type, Boolean isGlobal) {
        this.uploaderId = uploaderId;
		this.title = title;
		this.source = source;
		this.text = text;
		this.tags = tags;
		this.level = level;
		this.type = type;
		this.isGlobal = isGlobal;
	}

	public String validate() {
		if (title.length() <= 0) {
			return "Please provide a title for this passage.";
		}
		
		if (text.length() <= 0) {
			return "Please provide some text for this passage.";
		}
		
		if (level < 1 || level > 12) {
			return "The grade level of this passage must be 1-12 inclusive.";
		}
		
		return null;
	}

}
