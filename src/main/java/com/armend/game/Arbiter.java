package com.armend.game;

import java.io.PrintStream;
import java.util.Objects;

import com.armend.game.components.Item;
import com.armend.game.components.Player;
import com.armend.game.strategies.GameStrategy;

public class Arbiter {
	private Player player1;
	private Player player2;
	private GameStrategy strategy;
	private ScoreBoard scoreBoard;

	public Arbiter(GameStrategy strategy, Player player1, Player player2) {
		Objects.requireNonNull(strategy, "NULL value for 'strategy' is not allowed");
		Objects.requireNonNull(player1, "NULL value for 'player1' is not allowed");
		Objects.requireNonNull(player2, "NULL value for 'player2' is not allwed");
		this.strategy = strategy;
		this.player1 = player1;
		this.player2 = player2;
		scoreBoard = new ScoreBoard(player1.getName(), player2.getName());
	}

	/**
	 * Set the new strategy if 'strategy' is not null. If null argument provided,
	 * then it's ignored.
	 * 
	 * @param strategy The new game strategy to be used by the arbiter.
	 */
	public void setStrategy(GameStrategy strategy) {
		if (strategy != null) {
			this.strategy = strategy;
		}
	}

	public void printScores(PrintStream stream) {
		scoreBoard.printTo(stream);
	}

	/**
	 * As the players to make the move and decide who is the winner.
	 * 
	 * @return The winning player, or null if there is a tie between them.
	 */
	public Player execute() {
		Item player1Item = player1.play();
		Item player2Item = player2.play();
		Item result = strategy.whoIsTheWinner(player1Item, player2Item);
		if (result == null) {
			scoreBoard.addRecords(player1Item.name(), player2Item.name(), "It's a tie");
			scoreBoard.incementTies();
			return null;
		}
		if (result == player1Item) {
			scoreBoard.addRecords(player1Item.name(), player2Item.name(), player1.getName());
			scoreBoard.incrementFirstPlayesScore();
			return player1;
		}
		scoreBoard.addRecords(player1Item.name(), player2Item.name(), player2.getName());
		scoreBoard.incrementSecondPlayersScore();
		return player2;
	}
}
