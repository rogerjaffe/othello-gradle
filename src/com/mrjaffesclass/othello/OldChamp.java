package com.mrjaffesclass.othello;

import java.util.*;

public class OldChamp extends Player{

	private final int lookAhead = 5;
	private final double BIAS = 5;
	private Player opponent;

	public OldChamp(int color){
		super(color);
		this.opponent = new Player(color == Constants.BLACK ? Constants.WHITE : Constants.BLACK);
	}

	@Override
	public Position getNextMove(Board board) {

		ArrayList<Position> moves = this.findValidMoves(board, this);

		if (moves.size() > 0) {

			Double nextCoords = exploreTree(board, lookAhead, lookAhead, true);

			if (nextCoords != null) {

				String[] coords = String.valueOf(nextCoords.doubleValue()).split("\\.");
				Position nextMove = new Position(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));
				return nextMove;

			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private Double exploreTree(Board board, int currentLevel, int topLevel, boolean myTurn) {

		Player currentPlayer = myTurn ? this : opponent;

		ArrayList<Position> positions = this.findValidMoves(board, currentPlayer);
		ArrayList<Integer> scores = new ArrayList<Integer>();

		if (positions.size() <= 0) {
			return null;
		}
		
		for (int i = 0; i < positions.size(); i++) {

			Board clonedBoard = this.clone(board);
			clonedBoard.makeMove(currentPlayer, positions.get(i));

			if (currentLevel > 0) {

				Double deeper = exploreTree(clonedBoard, currentLevel-1, topLevel,! myTurn);

				if (deeper != null) {
					scores.add((int)Math.floor(deeper.doubleValue()));
				}
			} else {
				scores.add(weightScore(getScore(clonedBoard), positions.get(i), clonedBoard, currentPlayer, myTurn));
			}
		}

		if(currentLevel == topLevel) {
			if (scores.size() <= 0) {
				return exploreTree(board, topLevel - 1, topLevel - 1, true);
			}
			
			Position bestMove = positions.get(scores.indexOf(myTurn ? Collections.max(scores) : Collections.min(scores)));
			return Double.valueOf(bestMove.getRow() + (bestMove.getCol() / 10.0));
		}

		if (scores.size() > 0) {
			if (myTurn) {
				return Double.valueOf(Collections.max(scores));
			} else {
				return Double.valueOf(Collections.min(scores));
			}
		}
		return null;
	}

	private Board clone (Board board) {

		Board newBoard = new Board();

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				Position pos = new Position(i, j);
				Square tempSquare = board.getSquare(pos);

				if (tempSquare.getStatus()==0) {
					continue;
				}

				newBoard.setSquare(new Player(tempSquare.getStatus()),pos);
			}
		}
		return newBoard;
	}

	private ArrayList<Position> findValidMoves(Board board, Player player){

		ArrayList<Position> validPositions = new ArrayList<Position>();

		for(int i = 0; i < 8; i++){

			for(int j = 0; j < 8 ; j++){

				Position testPos = new Position(i, j);

				if(board.isLegalMove(player, testPos)){
					validPositions.add(testPos);
				}
			}
		}
		return validPositions;
	}
	
	private int getScore(Board board) {
		return board.countSquares(this.getColor()) - board.countSquares(this.getColor() == Constants.BLACK ? Constants.WHITE : Constants.BLACK);
	}
	
	private int weightScore(int score, Position pos, Board board, Player player, boolean myTurn) {
		double multiplier = 1;
		if (pos.getRow() == 0 || pos.getRow() == 7) {
			score += myTurn ? BIAS : -BIAS;
			multiplier *= 1.5;
		}
		if (pos.getCol() == 0 || pos.getCol() == 7) {
			score += myTurn ? BIAS : -BIAS;
			multiplier *= 1.5;			
		}
		if (pathToCorner(player, board, pos, true)) {
			multiplier *= 2;
		}
		return (int)Math.round(score * multiplier);
	}

	private boolean pathToCorner(Player player, Board board, Position pos, boolean isColumn) {
		if (isColumn) {
			boolean passed = true;
			for (int i = pos.getRow() - 1; i >= 0; i--) {
				if (board.getSquare(player, i, pos.getCol()).getStatus() != player.getColor()) {
					passed = false;
					break;
				}
			}
			if (passed) {
				return true;
			} else {
				passed = true;
				for (int i = pos.getRow() + 1; i <= 7; i++ ) {
					if (board.getSquare(player, i, pos.getCol()).getStatus() != player.getColor()) {
						passed = false;
						break;
					}
				}
				return passed;
			}
		} else {
			boolean passed = true;
			for (int i = pos.getCol() - 1; i >= 0; i--) {
				if (board.getSquare(player, pos.getRow(), i).getStatus() != player.getColor()) {
					passed = false;
					break;
				}
			}
			if (passed) {
				return true;
			} else {
				passed = true;
				for (int i = pos.getCol() + 1; i <= 7; i++ ) {
					if (board.getSquare(player, pos.getRow(), i).getStatus() != player.getColor()) {
						passed = false;
						break;
					}
				}
				return passed;
			}
		}
	}
}