package actors.passages;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.ActorRef;
import models.common.User;

public class MainActor extends UntypedActor {
	private User user;
	private String passage;
	private int num;

	public MainActor(User user, String passage, int num) {
		this.user = user;
		this.passage = passage;
		this.num = num;
	}

	@Override
	public void preStart() {
		// create the actor to generate the passage questions
		final ActorRef generator = getContext().actorOf(Props.create(GeneratorActor.class, user, passage, num), "generator");
		// tell it to perform the generation
		generator.tell(GeneratorActor.Msg.GENERATE, getSelf());
	}

	@Override
	public void onReceive(Object msg) {
		// shut down this actor when the generator finishes
		if (msg == GeneratorActor.Msg.DONE) {
			System.out.println("---- stopping " + getSelf());
			getContext().stop(getSelf());
		} else
			unhandled(msg);
	}
}