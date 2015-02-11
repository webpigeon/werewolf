package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.SeerRole;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.events.PlayerRole;

public class SeeAction implements ActionI {
	
	private String seen;
	
	public SeeAction(String seen) {
		this.seen = seen;
	}

	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		RoleI seenRole = model.getRole(seen);
		controller.unicast(name, new PlayerRole(seen, seenRole));
	}
	
	public String toString() {
		return "see "+seen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((seen == null) ? 0 : seen.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeeAction other = (SeeAction) obj;
		if (seen == null) {
			if (other.seen != null)
				return false;
		} else if (!seen.equals(other.seen))
			return false;
		return true;
	}

	@Override
	public boolean isEqual(ActionI action) {
		SeeAction seeAction = (SeeAction)action;
		return seen.equals(seeAction.seen);
	}
	
}
