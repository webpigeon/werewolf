package uk.me.webpigeon.wolf.newcode;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.events.EventI;

public interface SessionManager {

	void bind(String name, WolfController wolfController, BlockingQueue<EventI> eventQueue);

}
