package uk.me.webpigeon.wolf.newcode.players;

public class PlayerUtils {
	
	public static String toTripple(String subject, String predicate, String object) {
		return String.format("(%s,%s,%s)", subject, predicate, object);
	}

}
