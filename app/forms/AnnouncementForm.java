package forms;

public class AnnouncementForm {
	public String msg;
	public Long creatorId;

	public AnnouncementForm() {}
	public AnnouncementForm(Long creatorId, String msg) {
		this.creatorId = creatorId;
		this.msg = msg;
	}

	public String validate() {
		if (msg.length() <= 0) {
			return "Please provide a message!";
		}

		return null;
	}

}
