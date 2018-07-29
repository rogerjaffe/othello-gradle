package com.mrjaffesclass.othello;

/**
 * Player object. Students will extend this class
 * and override the getNextMove method
 * 
 * @author Mr. Jaffe
 * @version 1.0
 */
public class Player
{
  private final int color;
  
  /**
   * Player constructor
   * @param color   One of Constants.WHITE or Constants.BLACK
   */
  public Player(int color) {
    this.color = color;
  }
  
  /**
   * Gets the player color
   * @return        Player color
   */
  public int getColor() {
    return this.color;
  }
  
  /**
   * Gets the player name
   * @return        Player name
   */
  public String getName() {
    return this.getClass().getSimpleName();
  }

  /**
   * The player must override getNextMove
   * @param board Game board
   * @return A position coordinate pair of his/her next move. Returns null
   *          if no move is available
   */
  Position getNextMove(Board board) {
    return null;
  };

  @Override
  public String toString() {
    switch (this.color) {
      case Constants.BLACK:
        return this.getName()+" (BLACK)";
      case Constants.WHITE:
        return this.getName()+" (WHITE)";
      default:
        return this.getName()+" (?????)";
    }
  }
  
}
