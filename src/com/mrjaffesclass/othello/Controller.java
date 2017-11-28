package com.mrjaffesclass.othello;

import java.util.concurrent.TimeUnit;


/**
 * Game controller
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Controller
{
  private final Board board;
  private final Player player1, player2;
  
  /**
   * Constructor for objects of class Controller
   * @param player1 Player 1
   * @param player2 Player 2
   */
  public Controller(Player player1, Player player2)
  {
    this.board = new Board();
    this.player1 = player1;
    this.player2 = player2;
  }
  
  /**
   * Run the game with the two players.  Player 1 BLACK always goes first
   */
  public void run() {
    try {
      // Print heading
      System.out.println("START OF GAME");
      System.out.println(board.toString());

      // Black - Player 1 goes first
      boolean blackPlayersTurn = true;

      // While there are still spaces left
      while (board.countSquares(Constants.EMPTY) > 0) {

        // Get the player whose turn it is
        Player playerToMove = (blackPlayersTurn) ? this.player1 : this.player2;

        // Get the player's next move from the student's Player class and 
        // display the move
        Position nextMove = playerToMove.getNextMove(this.board);
        if (nextMove != null) {
          System.out.println(playerToMove.toString()+ " MOVE to "+nextMove.toString());
        }

        if (nextMove != null && board.getSquare(playerToMove, nextMove).getStatus() != Constants.EMPTY) {
          System.out.println("YOU CANNOT MOVE IN SPACE ["+nextMove.getRow()+","+nextMove.getCol()+"]");
        } else if (nextMove != null) {
          // Check that the move is legal
          if (this.board.isLegalMove(playerToMove, nextMove)) {

            // Make the move and print the move and the new board
            board.makeMove(playerToMove, nextMove);
            System.out.println(board.toString());
          }
        } else {
          // No move was found. Skip the player's turn
          System.out.println(playerToMove.toString()+" HAS NO MOVE THIS TURN\n");
        }
        // Delay so we can see the progression in the console
        TimeUnit.SECONDS.sleep(Constants.DELAY);
        // Switch turn to other player
        blackPlayersTurn = !blackPlayersTurn;
      }

      // Print Game Over message
      System.out.println("GAME OVER!");

      // Get the score
      int black = board.countSquares(Constants.BLACK);
      int white = board.countSquares(Constants.WHITE);
      System.out.println("BLACK: "+black+" SQUARES");
      System.out.println("WHITE: "+white+" SQUARES");

      // And who wins
      if (black > white) 
        System.out.println("BLACK WINS!");
      else if (white > black) 
        System.out.println("WHITE WINS!");
      else 
        System.out.println("TIE GAME");
    } catch (InterruptedException e) {
      System.out.println("OH NO!!! There was a time exception!\n");
      System.out.println(e.getMessage());
    }
  }
    
}
