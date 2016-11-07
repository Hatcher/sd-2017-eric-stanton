package actors.passages;

import akka.actor.UntypedActor;
import generators.passages.ComprehensionQuestionGenerator;
import models.common.User;
//import generators.ComprehensionQuestionGenerator;

public class GeneratorActor extends UntypedActor {

	private User user;
	private String passage;
	private int num;

	public GeneratorActor(User user, String passage, int num) {
		this.user = user;
		this.passage = passage;
		this.num = num;
	}

	public static enum Msg {
		GENERATE, DONE;
	}

	@Override
	public void onReceive(Object msg) {
		if (msg == Msg.GENERATE) {
			// run the questions generator
			ComprehensionQuestionGenerator.generateQuestions(user, passage, num);
			// tell the parent actor that we finished
			getSender().tell(Msg.DONE, getSelf());
			// shut down this actor
			System.out.println("---- stopping " + getSelf());
			getContext().stop(getSelf());
		}
		else
			unhandled(msg);
	}

}