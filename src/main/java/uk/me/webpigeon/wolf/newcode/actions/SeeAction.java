package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class SeeAction implements ActionI {
	
	private String seer;
	private String seen;
	
	public SeeAction(String seer, String seen) {
		this.seer = seer;
		this.seen = seen;
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		RoleI seenRole = model.getRole(seen);
		controller.sendRole(seer, seen, seenRole);
	}
	
	public String toString() {
		return "see "+seen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((seen == null) ? 0 : seen.hashCode());
		result = prime * result + ((seer == null) ? 0 : seer.hashCode());
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
		if (seer == null) {
			if (other.seer != null)
				return false;
		} else if (!seer.equals(other.seer))
			return false;
		return true;
	}

	@Override
	public boolean isEqual(ActionI action) {
		SeeAction seeAction = (SeeAction)action;
		return seen.equals(seeAction.seen);
	}
	
}
