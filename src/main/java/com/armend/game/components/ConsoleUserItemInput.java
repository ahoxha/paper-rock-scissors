package com.armend.game.components;

import java.io.*;

public class ConsoleUserItemInput implements ItemInput {
	private final BufferedReader reader;

	public ConsoleUserItemInput(Reader reader) {
		this.reader = new BufferedReader(reader);
	}

	@Override
	public Item get() {
		return readInputFromConsole();
	}

	private Item readInputFromConsole() {
		Item item;
		var tries = 3;
		do {
			String input = askUserForInput();
			item = Item.of(input);
			if (isWrongInput(item)) {
				System.err.println("Wrong input: " + input);
			}
			tries--;
		} while (isWrongInput(item) && tries > 0);
		if (isWrongInput(item)) {
			System.out.printf("Failed to get a valid input after %s consequetive tries.", tries);
		}
		return item;
	}

	private boolean isWrongInput(Item item) {
		return item == null;
	}

	private String askUserForInput() {
		System.out.println("Type S for Scissors, R for Rock, P for Paper:");
		String input;
		try {
			input = reader.readLine();
		} catch (IOException e) {
			input = "";
		}
		return input;
	}
}
