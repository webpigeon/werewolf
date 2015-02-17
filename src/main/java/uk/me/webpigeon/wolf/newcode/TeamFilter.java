package uk.me.webpigeon.wolf.newcode;

import java.util.function.Predicate;

import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.Team;

public class TeamFilter implements Predicate<RoleI> {
	private final Team team;
	
	public TeamFilter(Team t) {
		this.team = t;
	}

	@Override
	public boolean test(RoleI r) {
		return r.isOnTeam(team);
	}

}
