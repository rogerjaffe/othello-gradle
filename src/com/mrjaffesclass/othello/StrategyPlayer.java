package com.mrjaffesclass.othello;

import java.util.ArrayList;

/**
 *
 * @author jimmy
 */
public class StrategyPlayer extends Player {
	ArrayList<Position> moveList;
	
	public StrategyPlayer(int color) {
		super(color);
		
		moveList = new ArrayList<>();
		
		//Corners
		moveList.add(new Position(0,0));
		moveList.add(new Position(0,7));
		moveList.add(new Position(7,0));
		moveList.add(new Position(7,7));
		
		//Sides
		moveList.add(new Position(2,0));
		moveList.add(new Position(3,0));
		moveList.add(new Position(4,0));
		moveList.add(new Position(5,0));
		
		moveList.add(new Position(2,7));
		moveList.add(new Position(3,7));
		moveList.add(new Position(4,7));
		moveList.add(new Position(5,7));
		
		moveList.add(new Position(0,2));
		moveList.add(new Position(0,3));
		moveList.add(new Position(0,4));
		moveList.add(new Position(0,5));
		
		moveList.add(new Position(7,2));
		moveList.add(new Position(7,3));
		moveList.add(new Position(7,4));
		moveList.add(new Position(7,5));
		
		//Insides
		moveList.add(new Position(2,2));
		moveList.add(new Position(2,3));
		moveList.add(new Position(2,4));
		moveList.add(new Position(2,5));
		moveList.add(new Position(3,2));
		moveList.add(new Position(3,5));
		moveList.add(new Position(4,2));
		moveList.add(new Position(4,5));
		moveList.add(new Position(5,2));
		moveList.add(new Position(5,3));
		moveList.add(new Position(5,4));
		moveList.add(new Position(5,5));
		
		//Corner adjacents
		moveList.add(new Position(0,1));
		moveList.add(new Position(1,0));
		moveList.add(new Position(7,1));
		moveList.add(new Position(6,0));
		moveList.add(new Position(0,6));
		moveList.add(new Position(1,7));
		moveList.add(new Position(7,6));
		moveList.add(new Position(6,7));
		
		//Side adjacents
		moveList.add(new Position(1,2));
		moveList.add(new Position(1,3));
		moveList.add(new Position(1,4));
		moveList.add(new Position(1,5));
		
		moveList.add(new Position(6,2));
		moveList.add(new Position(6,3));
		moveList.add(new Position(6,4));
		moveList.add(new Position(6,5));
		
		moveList.add(new Position(2,1));
		moveList.add(new Position(3,1));
		moveList.add(new Position(4,1));
		moveList.add(new Position(5,1));
		
		moveList.add(new Position(2,6));
		moveList.add(new Position(3,6));
		moveList.add(new Position(4,6));
		moveList.add(new Position(5,6));
		
		//Corner diagonals
		moveList.add(new Position(1,1));
		moveList.add(new Position(1,6));
		moveList.add(new Position(6,1));
		moveList.add(new Position(6,6));
	}
	
	@Override
	public Position getNextMove(Board board) {
		for(int i = 0; i < moveList.size(); i++) {
			if(board.isLegalMove(this, moveList.get(i))) {
				return moveList.get(i);
			}
		}
		return null;
	}
}
