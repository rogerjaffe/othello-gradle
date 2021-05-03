package com.mrjaffesclass.othello;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jimmy
 */
public class RandomPlayer extends Player {
	Random rand = new Random();
	
	public RandomPlayer(int color) {
		super(color);
	}
	
	@Override
	public Position getNextMove(Board board) {
		
		ArrayList<Position> moves = new ArrayList<>();
		
		for (int i = 0; i < Constants.SIZE; i++) {
			for (int j = 0; j < Constants.SIZE; j++) {
				if (board.isLegalMove(this, new Position(i, j))) {
					moves.add(new Position(i, j));
				}
			}
		}
		
		if(moves.isEmpty()) {
			return null;
		}
		
		return moves.get(rand.nextInt(moves.size()));
	}
}
