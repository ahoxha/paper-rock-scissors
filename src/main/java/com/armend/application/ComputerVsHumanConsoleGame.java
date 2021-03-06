package com.armend.application;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.armend.game.Arbiter;
import com.armend.game.ImpatientArbiter;
import com.armend.game.components.ComputerPlayer;
import com.armend.game.components.ConsoleUserItemInput;
import com.armend.game.components.HumanPlayer;
import com.armend.game.components.ItemInput;
import com.armend.game.components.Player;
import com.armend.game.rules.DecisionRules;
import com.armend.game.rules.StandardDecisionRules;

public final class ComputerVsHumanConsoleGame {
	private final Player player1;
	private final Player player2;
	private final Arbiter arbiter;
	private final int rounds;
	private static final int MIN_ROUNDS = 3;
	private static final int MAX_ROUNDS = 100;
	private static Scanner scanner;

	private ComputerVsHumanConsoleGame(Player player1, Player player2, int rounds, Arbiter arbiter) {
		this.player1 = player1;
		this.player2 = player2;
		this.rounds = rounds;
		this.arbiter = arbiter;
	}

	public static ComputerVsHumanConsoleGame initialize() {
		scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
		System.out.println("======= Rock Paper Scissors ======");
		System.out.println("How many rounds do you want to play? (Min: " + MIN_ROUNDS + ", Max: " + MAX_ROUNDS + "): ");
		int n = readRoundsFromUser();
		System.out.println("Type your name:");
		String name = scanner.nextLine();
		ItemInput humanInput = new ConsoleUserItemInput(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		Player computerPlayer = new ComputerPlayer("Computer");
		Player humanPlayer = new HumanPlayer(name, humanInput);
		DecisionRules decisionRules = new StandardDecisionRules();
		Arbiter arbiter = isTimed() ? new ImpatientArbiter(decisionRules, computerPlayer, humanPlayer, 3)
				: new Arbiter(decisionRules, computerPlayer, humanPlayer);
		return new ComputerVsHumanConsoleGame(computerPlayer, humanPlayer, n, arbiter);
	}

	private static boolean isTimed() {
		System.out.println(
				"Type T for a timed game (You have a limited time to make the move, if you don't play on time you loose the round.");
		System.out.println("Type U for an untimed game (the program will wait indefitely for the user's input.)");
		System.out.println("If you don't give the right input (either T or U), the program will start a timed game.");
		try {
			return !"U".equalsIgnoreCase(scanner.nextLine());
		} catch (NoSuchElementException | IllegalStateException e) {
			return true;
		}
	}

	private static int readRoundsFromUser() {
		int tries = 3;
		do {
			String input = scanner.nextLine();
			try {
				var num = Integer.parseInt(input);
				validateRounds(num);
				return num;
			} catch (NumberFormatException e) {
				System.err.print("Invalid input: " + input + ". Please write a number.");
			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
			}
			tries--;
		} while (tries > 0);
		throw new RuntimeException("Failed to enter a valid input for the number of rounds.");
	}

	private static void validateRounds(int n) {
		if (n < MIN_ROUNDS || n > MAX_ROUNDS) {
			throw new IllegalArgumentException(
					"The 'rounds' argument must be between " + MIN_ROUNDS + " and " + MAX_ROUNDS);
		}
	}

	public void start() {
		System.out.println("Let's start playing between: " + player1.getName() + " and " + player2.getName()
				+ ", number of rounds: " + rounds);

		for (int i = 0; i < rounds; i++) {
			Player winner = arbiter.executeRound();
			if (winner != null) {
				System.out.print("Winner: " + winner.getName());
			} else {
				System.out.print("It's a tie.");
			}
			System.out.println(arbiter.getLastResult());
		}
		finalizeGame();
	}

	private void finalizeGame() {
		arbiter.printScores(System.out);
		scanner.close();
	}
}
