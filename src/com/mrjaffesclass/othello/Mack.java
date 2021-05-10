package com.mrjaffesclass.othello;

import java.time.Instant;
import java.util.*;

public class Mack extends Player {
	/**
	 * Constructor
	 * 
	 * @param color Player color: one of Constants.BLACK or Constants.WHITE
	 */
	public Mack(int color) {
		super(color);
	}

	/**
	 * getNextMove
	 * 
	 * @param board
	 * @return The player's next move
	 */
	@Override
	public Position getNextMove(Board board) {
            
            
        //Literally just cheat to move 2nd
        //if(board.countSquares(Constants.EMPTY) == 60) {
        //    return null;
        //}
            
		//Initialize variables
		BoardModel myBoard = new BoardModel(board);
		ArrayList<Position> moves = new ArrayList<>();
		float bestScore = 0;
		int bestIndex = 0;
		
		//Run heuristics
		ArrayList<Float> minimaxHeuristic;
                if(board.countSquares(Constants.EMPTY) > 12) {
                    minimaxHeuristic = getMCTSHeuristic(myBoard, getColor(), 5000);
                } else {
                    minimaxHeuristic = getMinimaxHeuristic(myBoard, getColor(), 13);
                }

		//Get all moves
		for (int i = 0; i < Constants.SIZE; i++) {
			for (int j = 0; j < Constants.SIZE; j++) {
				if (myBoard.isLegalMove(getColor(), new Position(i, j))) {
					moves.add(new Position(i, j));
				}
			}
		}
		
		
		for (int i = 0; i < minimaxHeuristic.size(); i++) {
			if (minimaxHeuristic.get(i) > bestScore) {
				bestScore = minimaxHeuristic.get(i);
				bestIndex = i;
			}
		}


		// TODO implement heuristic-based algorithm

		if (moves.isEmpty()) {
			return null;
		}

		//System.out.println(String.valueOf(minimaxHeuristic));
		return moves.get(bestIndex);
	}

	// TODO write other heuristic functions
	
	/**
	 * This function calculates the optimal move using the Monte Carlo Tree Search algorithm using UCT for deciding which nodes to search.
	 * https://en.wikipedia.org/wiki/Monte_Carlo_tree_search
	 * 
	 * @param board The board to analyze
	 * @param color The color of the player to move
	 * @param time The time allotted to explore nodes
	 * @return An ArrayList of the scores awarded to each possible move, in order from left to right and top to bottom. Higher is better. Scores are between 0 and 1.
	 */
	private ArrayList<Float> getMCTSHeuristic(BoardModel board, int color, int time) {
		long currentTime = System.currentTimeMillis();
		long targetTime = currentTime + time;
		
		int numSims = 0;
		Node current;
		Node gameState = new Node(board, null, color);
		
		//While within allotted time
		while (targetTime > currentTime) {
			//Selection
			current = gameState;
			while(current.wasUsed()) {
				if(current.hasNodes()) {
					current = current.getRandomNode();
				} else {
					break;
				}
			}
			
			//Expansion
			current.buildChain();
			if(current.hasNodes()) {
				current = current.getRandomNode();
			}
			
			//Simulation
			int winner = current.runSimulation();
			
			//Backpropagation
			while(current.hasParent()) {
				current.updateStats(winner);
				current = current.getParent();
			}
			
			//Update current time
			currentTime = System.currentTimeMillis();
			numSims++;
		}
		
		return gameState.getScores();
	}
	
	/**
	 * This function calculates the optimal move using the minimax algorithm with alpha-beta cutoffs.
	 * 
	 * @param board The board to analyze
	 * @param color The player color to analyze
	 * @param depth The number of moves to look ahead
	 * @return The player's next move
	 */
	private byte minimaxRecursive(BoardModel board, int color, int depth, byte myBest, byte theirBest) {
		// locals
		ArrayList<BoardModel> boards = new ArrayList<>();
		BoardModel myBoard = new BoardModel(board);
		int otherColor = 0 - color; // Color is 1 or -1. Abuse of magic numbers, but efficient.

		if (depth == 0) {
			return (byte) myBoard.countSquaresFast(getColor());
		}

		// Generate all legal moves.
		for (int i = 0; i < Constants.SIZE; i++) {
			for (int j = 0; j < Constants.SIZE; j++) {
				if (board.isLegalMove(color, new Position(i, j))) {
					BoardModel temp = new BoardModel(board);
					temp.makeMove(color, new Position(i, j));
					boards.add(temp);
				}
			}
		}

		if (boards.isEmpty()) {
			return minimaxRecursive(myBoard, otherColor, depth - 1, myBest, theirBest);
		}

		// The bulk of the algorithm.
		for (int i = 0; i < boards.size(); i++) {
			byte tempScore = minimaxRecursive(boards.get(i), otherColor, depth - 1, myBest, theirBest);

			// Alpha-beta cutoffs
			if (color == getColor() && tempScore > myBest) {
				if (tempScore > theirBest) {
					break;
				}
				myBest = tempScore;
			}
			if (color != getColor() && tempScore < theirBest) {
				if (tempScore < myBest) {
					break;
				}
				myBest = tempScore;
			}
		}

		return myBest;
	}
	
	/**
	 * This function implements the Minimax algorithm with alpha-beta cutoffs in order to assign each possible move a score.
	 * 
	 * @param board The board to analyze
	 * @param color	The player's color
	 * @param depth How deep the minimax algorithm should recurse
	 * @return An ArrayList of the scores awarded to each possible move, in order from left to right and top to bottom. Higher is better. Scores are between 0 and 1.
	 */
	private ArrayList<Float> getMinimaxHeuristic(BoardModel board, int color, int depth) {
		ArrayList<BoardModel> boards = new ArrayList<>();
		ArrayList<Float> toReturn = new ArrayList<>();
		float bestScore = 0;
		int otherColor = 0 - color; // Color is 1 or -1. Abuse of magic numbers, but efficient.

		// Generate all legal moves.
		for (int i = 0; i < Constants.SIZE; i++) {
			for (int j = 0; j < Constants.SIZE; j++) {
				if (board.isLegalMove(color, new Position(i, j))) {
					BoardModel temp = new BoardModel(board);
					temp.makeMove(color, new Position(i, j));
					boards.add(temp);
				}
			}
		}

		for (int i = 0; i < boards.size(); i++) {
			toReturn.add((float) minimaxRecursive(boards.get(i), otherColor, depth - 1, (byte) -1, (byte) 65));
			if (toReturn.get(i) > bestScore) {
				bestScore = toReturn.get(i);
			}
		}

		final float best = bestScore;

		toReturn.replaceAll(e -> e / best);

		return toReturn;
	}

	/**
	 * The Board class, but with extra functionality to support more efficient calculations. Incompatible with Board, might fix later.
	 * 
	 * @author jimmy
	 */
	private class BoardModel{
		/**
		 * Board squares set up in a 2x2 array
		 */
		private byte[][] squares;

		// THESE SHOULD REMAIN ACCURATE THROUGH THE USE OF ANY METHOD.
		private int blackCount;
		private int whiteCount;
		private int emptyCount;

		/**
		 * Constructor for objects of class BoardModel
		 */
		public BoardModel() {
			squares = new byte[Constants.SIZE][Constants.SIZE];
			squares = this.initBoard(squares);
			blackCount = 2;
			whiteCount = 2;
			emptyCount = 60;
		}

		public BoardModel(Board board) {
			blackCount = 0;
			whiteCount = 0;
			emptyCount = 0;
			squares = new byte[Constants.SIZE][Constants.SIZE];
			for (int i = 0; i < Constants.SIZE; i++) {
				for (int j = 0; j < Constants.SIZE; j++) {
					int color = board.getSquare(new Position(i, j)).getStatus();
					squares[i][j] = (byte)color;
					if(color == Constants.BLACK) {
						blackCount++;
					}
					if(color == Constants.WHITE) {
						whiteCount++;
					}
					if(color == Constants.EMPTY) {
						emptyCount++;
					}
				}
			}
		}

		public BoardModel(BoardModel board) {
			squares = new byte[Constants.SIZE][Constants.SIZE];
			for (int i = 0; i < Constants.SIZE; i++) {
				for (int j = 0; j < Constants.SIZE; j++) {
					squares[i][j] = board.getSquare(new Position(i, j));
				}
			}
			this.blackCount = board.blackCount;
			this.whiteCount = board.whiteCount;
			this.emptyCount = board.emptyCount;
		}

		/**
		 * Initialize a new player board with empty spaces except the four middle
		 * spaces. These are initialized W B B W
		 * 
		 * @return New board
		 */
		private byte[][] initBoard(byte[][] squares) {
			for (int row = 0; row < Constants.SIZE; row++) {
				for (int col = 0; col < Constants.SIZE; col++) {
					squares[row][col] = Constants.EMPTY;
				}
			}
			squares[3][3] = Constants.WHITE;
			squares[4][4] = Constants.WHITE;
			squares[3][4] = Constants.BLACK;
			squares[4][3] = Constants.BLACK;
			return squares;
		}

		public void setSquare(int color, Position position) {
			int color2 = this.squares[position.getRow()][position.getCol()];
			if(color == Constants.BLACK) {
				blackCount++;
			}
			if(color == Constants.WHITE) {
				whiteCount++;
			}
			if(color == Constants.EMPTY) {
				emptyCount++;
			}
			if(color2 == Constants.BLACK) {
				blackCount--;
			}
			if(color2 == Constants.WHITE) {
				whiteCount--;
			}
			if(color2 == Constants.EMPTY) {
				emptyCount--;
			}
			this.squares[position.getRow()][position.getCol()] = (byte)color;
		}

		public byte getSquare(Position position) {
			return this.squares[position.getRow()][position.getCol()];
		}

		public int countSquares(int toMatch) {
			int count = 0;
			for (byte[] row : this.squares) {
				for (byte square : row) {
					if (square == toMatch) {
						count++;
					}
				}
			}

			return count;
		}

		public int countSquaresFast(int toMatch) {
			if (toMatch == Constants.BLACK) {
				return this.blackCount;
			}
			if (toMatch == Constants.WHITE) {
				return this.whiteCount;
			}
			if (toMatch == Constants.EMPTY) {
				return this.emptyCount;
			}
			return (countSquares(toMatch));
		}

		public boolean isLegalMove(int color, Position positionToCheck) {
			// If the space isn't empty, it's not a legal move
			if (getSquare(positionToCheck) != Constants.EMPTY)
				return false;
			// Check all directions to see if the move is legal
			for (String direction : Directions.getDirections()) {
				Position directionVector = Directions.getVector(direction);
				if (step(color, positionToCheck, directionVector, 0)) {
					return true;
				}
			}
			return false;
		}

		protected boolean step(int color, Position position, Position direction, int count) {
			Position newPosition = position.translate(direction);
			if (newPosition.isOffBoard()) {
				// If off the board then move is not legal
				return false;
			} else if ((this.getSquare(newPosition) == Constants.EMPTY) && (count == 0)) {
				// If empty space AND adjacent to position then not legal
				return false;
			} else if (color != this.getSquare(newPosition)
				&& this.getSquare(newPosition) != Constants.EMPTY) {
				// If space has opposing player then move to next space in same direction
				return step(color, newPosition, direction, count + 1);
			} else if (color == this.getSquare(newPosition)) {
				// If space has this player and we've moved more than one space then it's legal,
				// otherwise it's not legal
				return count > 0;
			} else {
				// Didn't pass any other test, not legal move
				return false;
			}
		}

		/**
		 * Traverses the board in the provided direction. Checks the status of each
		 * space: a. If it's the opposing player then we'll move to the next space to
		 * see if there's a blank space b. If it's the same player then this direction
		 * doesn't represent a legal move c. If it's a blank AND if it's not the
		 * adjacent square then this direction is a legal move. Otherwise, it's not. If
		 * the move is legal, then this changes the pieces based on the the move
		 * 
		 * @param color     Player color making the request
		 * @param position  Position being checked
		 * @param direction Direction to move
		 * @param count     Number of steps we've made so far
		 * @return True if we find a legal move
		 */
		private boolean makeMoveStep(int color, Position position, Position direction, int count) {
			Position newPosition = position.translate(direction);
			if (newPosition.isOffBoard()) {
				return false;
			} else if (this.getSquare(newPosition) == -color) {
				boolean valid = makeMoveStep(color, newPosition, direction, count + 1);
				if (valid) {
					this.setSquare(color, newPosition);
				}
				return valid;
			} else if (this.getSquare(newPosition) == color) {
				return count > 0;
			} else {
				return false;
			}
		}

		/**
		 * Make the move. Scan all directions and switch the piece colors of the ones as
		 * appropriate.
		 * 
		 * @param color          Color of move
		 * @param positionToMove Position of the new move
		 */
		public void makeMove(int color, Position positionToMove) {
			for (String direction : Directions.getDirections()) {
				Position directionVector = Directions.getVector(direction);
				if (makeMoveStep(color, positionToMove, directionVector, 0)) {
					this.setSquare(color, positionToMove);
//		      } else {
//		        System.out.println("**** THIS SPACE IS NOT A VALID MOVE. YOU LOSE!");
				}
			}
		}
	}
	
	/**
	 * An extension of the board used to create a tree used in MCTS.
	 * "inspiration" from: https://github.com/spopov812/MonteCarlo-Othello/blob/master/src/main/java/my/project/othello/Node.java
	 * 
	 * @author jimmy
	 */
	public class Node extends BoardModel {
		private Node parentNode = null;
		private ArrayList<Node> nodeChain = new ArrayList<>();
		private boolean wasUsed = false;
		private int numWins;
		private int numSimulations;
		private int nextMoveColor;
		private final int[][] weights;
		/**
		 * Constructor
		 * 
		 * @param board A board to start with
		 * @param parent The parent node of the node being created
		 * @param color The color that will move next
		 */
		public Node(BoardModel board, Node parent, int color) {
			super(board);
			parentNode = parent;
			nextMoveColor = color;
			numWins = 0;
			numSimulations = 0;
			nodeChain.clear();
			weights = new int[][] {
				{9, 1, 7, 7, 7, 7, 1, 9},
				{1, 1, 3, 3, 3, 3, 1, 1},
				{7, 3, 5, 5, 5, 5, 3, 7},
				{7, 3, 5, 5, 5, 5, 3, 7},
				{7, 3, 5, 5, 5, 5, 3, 7},
				{7, 3, 5, 5, 5, 5, 3, 7},
				{1, 1, 3, 3, 3, 3, 1, 1},
				{9, 1, 7, 7, 7, 7, 1, 9}
			};
		}
		
		/**
		 * Add all legal moves to nodeChain.
		 */
		public void buildChain() {
			wasUsed = true;
			for (int i = 0; i < Constants.SIZE; i++) {
				for (int j = 0; j < Constants.SIZE; j++) {
					if (this.isLegalMove(nextMoveColor, new Position(i, j))) {
						Node temp = new Node(this, this, 0-nextMoveColor);
						temp.makeMove(nextMoveColor, new Position(i, j));
						nodeChain.add(temp);
					}
				}
			}
		}
		
		/**
		 * Switch colors, then retry building nodeChain. Only used when generating child nodes fails.
		 */
		public void regenerateChain() {
			nextMoveColor = 0 - nextMoveColor;
			buildChain();
		}
		
		/**
		 * Gets a random child node from the current node.
		 * 
		 * @return the child node selected completely randomly.
		 */
		public Node getRandomNode() {
//                    float score;
//                    float bestScore = 0;
//                    int bestIndex = 0;
//                    
//                    for(int i = 0; i < nodeChain.size(); i++) {
//                        Node current = nodeChain.get(i);
//                        float winRate;
//                        if(current.getSimulations() == 0) {
//                            winRate = 0;
//                        } else {
//                            winRate = (float)current.getWins() / current.getSimulations();
//                        }
//                        float explore;
//                        if(current.getSimulations() > 0) {
//                            explore = (float)Math.log(this.getSimulations());
//                            explore *= 2;
//                            explore /= current.getSimulations();
//                            explore = (float)Math.sqrt(explore);
//                        } else {
//                            explore = 2;
//                        }
//                        score = winRate + explore;
//                        if(score >= bestScore) {
//                            bestScore = score;
//                            bestIndex = i;
//                        }
//                    }
//                    
//                    return nodeChain.get(bestIndex);
                    Random rand = new Random();
                    return(nodeChain.get(rand.nextInt(nodeChain.size())));
		}
		
		public int runSimulation() {
			BoardModel simBoard = new BoardModel(this);
			int currentColor = nextMoveColor;
			Position position;
			
			Random rand = new Random();
			
			while(true) {
				ArrayList<Position> moves = new ArrayList<>();
				
				for(int i = 0; i < Constants.SIZE; i++) {
					for (int j = 0; j < Constants.SIZE; j++) {
						position = new Position(i, j);
						if(simBoard.isLegalMove(currentColor, position)) {
							for(int k = 0; k < weights[i][j]; k++) {
								moves.add(position);
							}
						}
					}
				}
				
				if(moves.size() == 0) {
					break;
				}
				
				simBoard.makeMove(currentColor, moves.get(rand.nextInt(moves.size())));
				currentColor = 0 - currentColor;
			}
			
			int black = simBoard.countSquaresFast(Constants.BLACK);
			int white = simBoard.countSquaresFast(Constants.WHITE);
			if(black > white) {
				return Constants.BLACK;
			}
			if(white > black) {
				return Constants.WHITE;
			}
			return 0;
		}
		
		/**
		 * Checks if the node has children.
		 * 
		 * @return true if the node has children, false otherwise.
		 */
		public boolean hasNodes() {
			return (nodeChain.size() > 0);
		}
		
		/**
		 * Gets the node's parent.
		 * 
		 * @return the parent node
		 */
		public Node getParent() {
			return parentNode;
		}
		
		/**
		 * Checks whether the node has a parent.
		 * 
		 * @return true if the node has a parent, false otherwise.
		 */
		public boolean hasParent() {
			return (parentNode != null);
		}
		
		/**
		 * Sets the current node as the parent of all nodes in the tree.
		 */
		public void setParent() {
			parentNode = null;
		}
		
		/**
		 * Updates the number of simulation runs and wins through this node. Used in backpropagation.
		 * 
		 * @param win Who won the game.
		 */
		public void updateStats(int winner) {
			if(winner == nextMoveColor) {
				numWins++;
			}
			numSimulations++;
		}
		
		/**
		 * Check whether or not children have been generated.
		 * 
		 * @return true if the node has any children, false otherwise.
		 */
		public boolean wasUsed() {
			return wasUsed;
		}
		
		/**
		 * @return the number of games won through this node.
		 */
		public int getWins() {
			return numWins;
		}
		
		/**
		 * @return the number of simulations that ran through this node.
		 */
		public int getSimulations() {
			return numSimulations;
		}
		
		public ArrayList<Float> getScores() {
			ArrayList<Float> scores = new ArrayList<>();
			for(int i = 0; i < nodeChain.size(); i++) {
				Node child = nodeChain.get(i);
				scores.add(1 - ((float)child.getWins() / child.getSimulations()));
			}
			return scores;
		}
	}
}
