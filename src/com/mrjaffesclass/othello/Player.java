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
  private final String name;
  
  /**
   * Player constructor
   * @param name    Name of the player
   * @param color   One of Constants.WHITE or Constants.BLACK
   */
  public Player(String name, int color) {
    this.color = color;
    this.name = name;
  }
  
  /**
   * Gets the player color
   * @return        Player color
   */
  public int getColor() {
    return this.color;
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
        return this.name+" (BLACK)";
      case Constants.WHITE:
        return this.name+" (WHITE)";
      default:
        return this.name+" (?????)";
    }
  }
  
}
