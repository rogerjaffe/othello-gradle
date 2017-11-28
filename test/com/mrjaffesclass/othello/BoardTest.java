/*
 * Copyright 2017 Roger Jaffe
 * All rights reserved
 */
package com.mrjaffesclass.othello;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Roger Jaffe
 */
public class BoardTest {
  
  public BoardTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }
  
  /**
   * Test of countSquares method, of class Board.
   */
  @Test
  public void testCountSquares() {
    System.out.println("countSquares");
    int toMatch = 0;
    Board instance = new Board();
    int expResult = 0;
    int result = instance.countSquares(toMatch);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSquare method, of class Board.
   */
  @Test
  public void testGetSquare_3args() {
    System.out.println("getSquare");
    Player player = null;
    int row = 0;
    int col = 0;
    Board instance = new Board();
    Square expResult = null;
    Square result = instance.getSquare(player, row, col);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSquare method, of class Board.
   */
  @Test
  public void testGetSquare_Player_Position() {
    System.out.println("getSquare");
    Player player = null;
    Position position = null;
    Board instance = new Board();
    Square expResult = null;
    Square result = instance.getSquare(player, position);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of isLegalMove method, of class Board.
   */
  @Test
  public void testIsLegalMove() {
    System.out.println("isLegalMove");
    Player player = null;
    Position positionToCheck = null;
    Board instance = new Board();
    boolean expResult = false;
    boolean result = instance.isLegalMove(player, positionToCheck);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of makeMove method, of class Board.
   */
  @Test
  public void testMakeMove() {
    System.out.println("makeMove");
    Player playerToMove = null;
    Position positionToMove = null;
    Board instance = new Board();
    instance.makeMove(playerToMove, positionToMove);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of toString method, of class Board.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    Board instance = new Board();
    String expResult = "";
    String result = instance.toString();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
