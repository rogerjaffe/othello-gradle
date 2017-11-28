/*
 * Othello class project competition
 * Copyright 2017 Roger Jaffe
 * All rights reserved
 */
package com.mrjaffesclass.othello;

import java.awt.Color;

/**
 * Othello class project competition
 * @author Roger Jaffe
 * @version 1.0
 */
public class Othello {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Controller c = new Controller( 
      new <PLAYER1 CLASS>("Roger 1", Constants.BLACK),
      new <PLAYER2 CLASS>("Roger 2", Constants.WHITE) 
    );
    c.run();
  }
  
}
