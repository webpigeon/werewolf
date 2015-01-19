package uk.me.webpigeon.wolf;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class WolfRunner {

	public static void main(String[] args) {
		
		List<Role> roles = Arrays.asList(Role.WOLF, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER);
		WolfGame game = new WolfGame(roles);
		
		game.add(new RandomPlayer("Fred"));
		game.add(new RandomPlayer("John"));
		game.add(new RandomPlayer("Bob"));
		game.add(new RandomPlayer("Wolfgang"));
		game.add(new RandomPlayer("Dave"));
		game.add(new RandomPlayer("Jackie"));
		
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
