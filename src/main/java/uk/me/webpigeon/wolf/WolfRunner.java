package uk.me.webpigeon.wolf;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class WolfRunner {

	public static void main(String[] args) {
		
		List<Role> roles = Arrays.asList(Role.WOLF, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.SEER);
		WolfGame game = new WolfGame(roles);
		
		game.add(new BasicIntelligencePlayer("Fred"));
		game.add(new BasicIntelligencePlayer("John"));
		game.add(new BasicIntelligencePlayer("Bob"));
		game.add(new BasicIntelligencePlayer("Wolfgang"));
		game.add(new BasicIntelligencePlayer("Pebbles"));
		game.add(new BasicIntelligencePlayer("Jackie"));
		game.add(new BasicIntelligencePlayer("Sarah"));
		
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
