package com.mrjaffesclass.othello;

import java.util.*;

/**
 * Write a description of class Yeung here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Yeung extends Player
{

    private Player opponent;
    private Position bestCoordinate = null;
    int SEARCH_DEPTH = 8;

    public Yeung(int color) {
        super(color);
        this.opponent = new Player(color == Constants.BLACK ? Constants.WHITE : Constants.BLACK);
    }

    @Override
    public Position getNextMove(Board board) {
        ArrayList<Position> moves = this.validMoves(board, this);
		bestCoordinate = null;
        if (moves.size() > 0) {
            minimax(board, SEARCH_DEPTH, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
            return bestCoordinate;
        } else {
            return null;
        }
    }

    private int minimax(Board board, int depth, boolean maximizingPlayer, int alpha, int beta) {
		
        if (depth == 0) {
            return board.countSquares(this.getColor()) - board.countSquares(this.getColor() == Constants.BLACK ? Constants.WHITE : Constants.BLACK);
        }

        if (maximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            ArrayList<Position> moves = this.validMoves(board, this);
			
            for (Position move : moves) {
                Board newBoard = clone(board);
                newBoard.makeMove(this, move);
                int v = minimax(newBoard, depth - 1, !maximizingPlayer, alpha, beta);
                if (v > bestValue) {
                    bestValue = v;
                    if (depth == SEARCH_DEPTH) {
                        bestCoordinate = move;
                    }
                    if (bestValue > alpha) {
                        alpha = bestValue;
                    }
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestValue;
        }
        else {
            int bestValue = Integer.MAX_VALUE;
            ArrayList<Position> moves = this.validMoves(board, opponent);
			
            for (Position move : moves) {
                Board newBoard = clone(board);
                newBoard.makeMove(opponent, move);
                int v = minimax(newBoard, depth - 1, !maximizingPlayer, alpha, beta);
                if (v < bestValue) {
                    bestValue = v;
                    if (bestValue < beta) {
                        beta = bestValue;
                    }
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestValue;
        }
    }

    private Board clone(Board board) {
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

    private ArrayList<Position> validMoves(Board board, Player player) {
        ArrayList<Position> validMoves = new ArrayList<Position>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position test = new Position(row, col);
                if (board.isLegalMove(player, test)) {
                    validMoves.add(test);
                }
            }
        }
        return validMoves;
    }
}
