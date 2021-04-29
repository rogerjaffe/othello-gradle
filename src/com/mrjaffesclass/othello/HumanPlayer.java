package com.mrjaffesclass.othello;

import java.util.*;

public class HumanPlayer extends Player {
	Scanner sc = new Scanner(System.in);

	public HumanPlayer(int color) {
		super(color);
	}

	@Override
	public Position getNextMove(Board board) {
		int row;
		int col;
		Position pos;
		ArrayList<Position> moves = new ArrayList<Position>();
		
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
		
		do {
			System.out.println("Your move!");
			System.out.print("\nRow: ");
			row = Integer.parseInt(sc.nextLine());
			System.out.print("Column: ");
			col = Integer.parseInt(sc.nextLine());
			pos = new Position(row, col);
		} while (!board.isLegalMove(this, pos));
		return pos;
	}
}
