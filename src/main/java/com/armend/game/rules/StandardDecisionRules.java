package com.armend.game.rules;

import com.armend.game.components.Item;

/**
 * Defines the standard set of rules for deciding who's the winner.
 * <ol>
 * <li>Paper beats Rock</li>
 * <li>Rock beats Scissors</li>
 * <li>Scissors beats Paper</li>
 * </ol>
 *
 */
public class StandardDecisionRules implements DecisionRules {

	private final Item[][] decisionTable;

	public StandardDecisionRules() {
		decisionTable = new Item[][]
		{ 			/*SECOND:rock	 			paper			scissors*/
		/*FIRST*/
		/*rock*/		{ null, 			Item.PAPER, 		Item.ROCK 		},
		/*paper*/		{ Item.PAPER, 		null, 				Item.SCISSORS 	},
		/*scissors*/	{ Item.ROCK, 		Item.SCISSORS, 		null			}
		};
	}

	@Override
	public Item whoIsTheWinner(Item firstItem, Item secondItem) {
		validate(firstItem, secondItem);
		return decisionTable[firstItem.getIndex()][secondItem.getIndex()];
	}

	private void validate(Item firstItem, Item secondItem) {
		if (firstItem == null) {
			throw new IllegalArgumentException("firstItem should not be null");
		}
		if (secondItem == null) {
			throw new IllegalArgumentException("secondItem should not be null");
		}
	}
}
