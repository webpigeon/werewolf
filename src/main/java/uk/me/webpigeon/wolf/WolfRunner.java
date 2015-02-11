package uk.me.webpigeon.wolf;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import uk.me.webpigeon.wolf.newcode.actions.WolfUtils;
import uk.me.webpigeon.wolf.newcode.legacy.players.BasicIntelligencePlayer;

public class WolfRunner {

	public static void main(String[] args) {
		
		List<RoleI> roles = WolfUtils.buildRoleList();
		WolfGame game = new WolfGame(roles);
		
		game.add("Fred", new BasicIntelligencePlayer());
		game.add("John", new BasicIntelligencePlayer());
		game.add("Bob", new BasicIntelligencePlayer());
		game.add("Wolfgang", new BasicIntelligencePlayer());
		game.add("Pebbles", new BasicIntelligencePlayer());
		game.add("Jackie", new BasicIntelligencePlayer());
		game.add("Sarah", new BasicIntelligencePlayer());
		
		Thread gameThread = new Thread(game);
		gameThread.start();
		
		Scanner commandLine = new Scanner(System.in);
		while (commandLine.hasNextLine()) {
			String command = commandLine.nextLine();
			
			switch(command) {
				case "timeout":
					game.timeout();
					break;
				default:
					System.out.println("invalid command");
			}
		}
		
		commandLine.close();
	}
	
}
