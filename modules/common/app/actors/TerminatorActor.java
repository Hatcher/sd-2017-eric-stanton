package actors;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;

public class TerminatorActor extends UntypedActor {

	private final ActorRef ref;

	public TerminatorActor(ActorRef ref) {
		this.ref = ref;
		getContext().watch(ref);
	}

	@Override
	public void onReceive(Object msg) {
		if (msg instanceof Terminated) {
			System.out.println("---- stopping " + getSelf());

			// shut down this actor
			getContext().stop(getSelf());
			System.out.println("shutting down " + getContext().system());
			getContext().system().shutdown();
		} else {
			unhandled(msg);
		}
	}
}