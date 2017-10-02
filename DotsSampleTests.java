import java.io.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DotsSampleTests {
 static ByteArrayOutputStream localOut, localErr;
 static PrintStream sysOut, sysErr;
 static String[] empty = {};
 
 // Determine what the newline is on the running system
 String newline = System.getProperty("line.separator");

 //set the print streams we will be using
 @BeforeClass
 public static void setup() throws Exception {
  sysOut = System.out;
  sysErr = System.err;
 }

 //before every test is run, reset the streams to capture stdout/stderr
 @Before
 public void setupStreams() {
  localOut = new ByteArrayOutputStream();
  localErr = new ByteArrayOutputStream();
  System.setOut(new PrintStream(localOut));
  System.setErr(new PrintStream(localErr));
 }

 //after every test, restore stdout/stderr
 @After
 public void cleanUpStreams() {
  System.setOut(null);
  System.setErr(null);
  System.setOut(sysOut);
  System.setErr(sysErr);
 }

 //make sure making a new game sets the width and height correctly
 @Test(timeout=2000)
 public void gameWidthHeight01() {
  Dots game = new Dots(10, 10);
  assertEquals(10, game.getWidth());
  assertEquals(10, game.getHeight());
 }

 //make sure making a new game sets the width and height correctly
 @Test(timeout=2000)
 public void gameWidthHeight02() {
  Dots game = new Dots(100, 5);
  assertEquals(100, game.getWidth());
  assertEquals(5, game.getHeight());
 }

 //make sure making a new game sets the width and height correctly
 @Test(timeout=2000)
 public void gameValidBoardLoaded() throws IOException, InvalidBoardException {
  //make a valid board
  PrintWriter pw = new PrintWriter("./boards/valid.board");
  pw.println("  ^ ");
  pw.println("    ");
  pw.close();
  
  //try loading it
  Dots game = new Dots(new File("./boards/valid.board"));
  
  assertEquals(4, game.getWidth());
  assertEquals(2, game.getHeight());
 }

 //make sure FileNotFoundException is thrown
 @Test(expected=FileNotFoundException.class,timeout=2000)
 public void gameFileNotFoundThrown() throws FileNotFoundException, InvalidBoardException {
  Dots game = new Dots(new File("banana")); //assumes file "banana" doesn't exist
 }

 //make sure InvalidBoardException is thrown
 @Test(expected=InvalidBoardException.class,timeout=2000)
 public void gameInvalidBoardThrown() throws IOException, InvalidBoardException {
  //make an invalid board (some rows different lengths)
  PrintWriter pw = new PrintWriter("./boards/invalid.board");
  pw.println("    ");
  pw.println("   ");
  pw.println("  ");
  pw.println(" ");
  pw.close();
  
  //try loading it
  Dots game = new Dots(new File("./boards/invalid.board"));
  
  //expect InvalidBoardException
 }

 //make sure loading a game loads the the pins and spaces correctly
 @Test(timeout=2000)
 public void gameLoadBoard() throws IOException, InvalidBoardException {
  //make a valid board
  PrintWriter pw = new PrintWriter("./boards/valid.board");
  pw.println("  ^ ");
  pw.println("    ");
  pw.close();
  
  //try loading it
  Dots game = new Dots(new File("./boards/valid.board"));
  
  for(int x = 0; x < 4; x++) {
   for(int y = 0; y < 2; y++) {
    if(x == 2 && y == 0)
     assertEquals('^', game.getPosition(x, y));
    else
     assertEquals(' ', game.getPosition(x, y));
   }
  }
 }

 //make sure when you set a position it is set
 //pin test
 @Test(timeout=2000)
 public void gameSetPosition01() {
  Dots game = new Dots(10, 10);
  game.setPosition(5, 0, '^');
  assertEquals('^', game.getPosition(5, 0));
 }

 //make sure when you set a position it is set
 //dot test
 @Test(timeout=2000)
 public void gameSetPosition02() {
  Dots game = new Dots(10, 10);
  game.setPosition(5, 0, 'O');
  assertEquals('O', game.getPosition(5, 0));
 }

 //make sure when you set a position it is set
 //set to pin and clear test
 @Test(timeout=2000)
 public void gameSetPosition03() {
  Dots game = new Dots(10, 10);
  game.setPosition(5, 0, '^');
  game.setPosition(5, 0, ' ');
  assertEquals(' ', game.getPosition(5, 0));
 }

 //make sure when the game starts no dots are dropping
 @Test(timeout=2000)
 public void gameStartNoDroppingDots() {
  Dots game = new Dots(10, 10);
  assertFalse(game.dotIsDropping());
 }

 //make sure when you drop a dot is starts falling
 @Test(timeout=2000)
 public void gameDropIsDropping() {
  Dots game = new Dots(10, 10);
  game.drop(5);
  assertTrue(game.dotIsDropping());
 }

 //make sure when you drop a dot it returns true if it can drop
 @Test(timeout=2000)
 public void gameDropReturn01() {
  Dots game = new Dots(10, 10);
  assertTrue(game.drop(5));
 }

 //make sure when you drop a dot it returns false if it can't drop
 //dot already falling test
 @Test(timeout=2000)
 public void gameDropReturn02() {
  Dots game = new Dots(10, 10);
  game.drop(5);
  assertFalse(game.drop(5));
 }

 //make sure when you drop a dot it returns false if it can't drop
 //pin in the way test
 @Test(timeout=2000)
 public void gameDropReturn03() {
  Dots game = new Dots(10, 10);
  game.setPosition(5, 0, '^');
  assertFalse(game.drop(5));
 }

 //make sure when you drop a dot it returns false if it can't drop
 //dot in the way test
 @Test(timeout=2000)
 public void gameDropReturn04() {
  Dots game = new Dots(10, 10);
  game.setPosition(5, 0, 'O');
  assertFalse(game.drop(5));
 }

 //make sure when you drop a dot it appears on the board
 @Test(timeout=2000)
 public void gameDropSetPosition() {
  Dots game = new Dots(10, 10);
  game.drop(5);
  game.getPosition(5, 0);
  assertEquals('O', game.getPosition(5, 0));
 }

 //make sure getting the current drop x and y work
 //no drop test
 @Test(timeout=2000)
 public void gameCurrentDropXY01() {
  Dots game = new Dots(10, 10);
  assertEquals(-1, game.getCurrentDropX());
  assertEquals(-1, game.getCurrentDropY());
 }

 //make sure getting the current drop x and y work
 //drop test
 @Test(timeout=2000)
 public void gameCurrentDropXY02() {
  Dots game = new Dots(10, 10);
  game.drop(5);
  assertEquals(5, game.getCurrentDropX());
  assertEquals(0, game.getCurrentDropY());
 }

 //make sure getting the current drop x and y work
 //drop and step test
 @Test(timeout=2000)
 public void gameCurrentDropXY03() throws NoFallingDotException {
  Dots game = new Dots(10, 10);
  game.drop(5);
  game.step();
  assertEquals(5, game.getCurrentDropX());
  assertEquals(1, game.getCurrentDropY());
 }

 //make sure stepping works
 //straight down test
 @Test(timeout=2000)
 public void gameStepReturn() throws NoFallingDotException {
  Dots game = new Dots(3, 2);
  game.drop(1);
  assertTrue(game.step());
  assertFalse(game.step());
 }

 //make sure stepping works
 //too many steps
 @Test(expected=NoFallingDotException.class,timeout=2000)
 public void gameStepTooFar() throws NoFallingDotException {
  Dots game = new Dots(3, 2);
  game.drop(1);
  game.step();
  game.step();
  game.step();
 }

 //make sure stepping works
 //x, y positions
 @Test(timeout=2000)
 public void gameStepXY() throws NoFallingDotException {
  Dots game = new Dots(3, 3);
  game.drop(1); //should be in (1,0)
  
  game.step(); //should be in (1,1)
  assertEquals(1, game.getCurrentDropX());
  assertEquals(1, game.getCurrentDropY());
  
  game.step(); //should be in (1,2)
  assertEquals(1, game.getCurrentDropX());
  assertEquals(2, game.getCurrentDropY());
  
  game.step(); //should be reset to -1
  assertEquals(-1, game.getCurrentDropX());
  assertEquals(-1, game.getCurrentDropY());
 }

 //make sure stepping works
 //no dropping ball at start
 @Test(expected=NoFallingDotException.class,timeout=2000)
 public void gameStepAtStart() throws NoFallingDotException {
  Dots game = new Dots(3, 2);
  game.step();
 }
 
 //make sure stepping works
 //x, y positions with pins
 @Test(timeout=2000)
 public void gameStepXYPins() throws NoFallingDotException {
  Dots game = new Dots(3, 3);
  game.setPosition(1,1,'^');
  game.drop(1);
  
  game.step(); //should be in (0,1) or (2,1)
  assertEquals(1, game.getCurrentDropY());
  assertTrue(game.getCurrentDropX() == 0 || game.getCurrentDropX() == 2);
 }
 
 //make sure stepping works
 //x, y positions with dots
 @Test(timeout=2000)
 public void gameStepXYDots() throws NoFallingDotException {
  Dots game = new Dots(3, 3);
  game.setPosition(1,1,'O');
  game.drop(1);
  
  game.step(); //should be in (0,1) or (2,1)
  assertEquals(1, game.getCurrentDropY());
  assertTrue(game.getCurrentDropX() == 0 || game.getCurrentDropX() == 2);
 }
 
 //make sure stepping works
 //sets board correctly
 @Test(timeout=2000)
 public void gameStepBoardUpdated() throws NoFallingDotException {
  Dots game = new Dots(3, 3);
  
  game.drop(1);
  assertEquals('O', game.getPosition(1, 0));
  
  game.step();
  assertEquals(' ', game.getPosition(1, 0));
  assertEquals('O', game.getPosition(1, 1));
 }
 
 //make sure stepping works
 //goes one way or the other at least once
 @Test(timeout=10000)
 public void gameStepXYPinsRandom() throws NoFallingDotException {
  boolean doneLeft = false, doneRight = false;
  
  while(!doneLeft || !doneRight) {
   Dots game = new Dots(3, 3);
   game.setPosition(1,1,'^');
   game.drop(1);
   game.step();
   
   if(game.getCurrentDropX() == 0)   
     doneLeft = true;  
   
    else if(game.getCurrentDropX() == 2){     
      doneRight = true;
      
    
    }
  }
 }
 //make sure the environment is toroidal
 @Test(timeout=10000)
 public void gameToroidal01() throws NoFallingDotException {
  boolean doneLeft = false, doneRight = false;
  
  Dots game = new Dots(3, 3);
  game.setPosition(1,1,'^');
  game.setPosition(2,1,'^');
  game.drop(2);
  game.step();
   
  assertEquals(0, game.getCurrentDropX());
  assertEquals(1, game.getCurrentDropY());
 }
 
 //make sure the environment is toroidal
 @Test(timeout=10000)
 public void gameToroidal02() throws NoFallingDotException {
  boolean doneLeft = false, doneRight = false;
  
  Dots game = new Dots(3, 3);
  game.setPosition(1,1,'^');
  game.setPosition(0,1,'^');
  game.drop(0);
  game.step();
   
  assertEquals(2, game.getCurrentDropX());
  assertEquals(1, game.getCurrentDropY());
 }
 
 //make sure printing a board works correctly
 @Test(timeout=2000)
 public void gamePrintBoard01() throws NoFallingDotException {
  Dots game = new Dots(3, 3);
  game.printBoard();
  String output = "|   |"+newline+
      "|   |"+newline+
      "|   |"+newline+
      "-----"+newline;
  assertEquals(output, localOut.toString());
 }
 
 //make sure printing a board works correctly
 @Test(timeout=2000)
 public void gamePrintBoard02() throws NoFallingDotException {
  Dots game = new Dots(3, 3);
  game.drop(1);
  game.printBoard();
  String output = "| O |"+newline+
      "|   |"+newline+
      "|   |"+newline+
      "-----"+newline;
  assertEquals(output, localOut.toString());
 }
 
 //make sure printing a board works correctly
 @Test(timeout=2000)
 public void gamePrintBoard03() throws NoFallingDotException {
  Dots game = new Dots(3, 3);
  game.setPosition(1,1,'^');
  game.drop(1);
  game.printBoard();
  String output = "| O |"+newline+
      "| ^ |"+newline+
      "|   |"+newline+
      "-----"+newline;
  assertEquals(output, localOut.toString());
 }
 
 //make sure printing a board works correctly
 @Test(timeout=2000)
 public void gamePrintBoard04() throws NoFallingDotException {
  Dots game = new Dots(5, 4);
  game.setPosition(1,1,'^');
  game.drop(1);
  game.printBoard();
  String output = "| O   |"+newline+
      "| ^   |"+newline+
      "|     |"+newline+
      "|     |"+newline+
      "-------"+newline;
  assertEquals(output, localOut.toString());
 }
 
 //make sure saving a board works correctly
 //empty board
 @Test(timeout=2000)
 public void gameSaveBoard01() throws FileNotFoundException {
  File f = new File("./boards/saveTest.board");
  
  Dots game = new Dots(3, 3);
  game.saveGameBoard(f);
  
  Scanner s = new Scanner(f);
  assertEquals("   ", s.nextLine());
  assertEquals("   ", s.nextLine());
  assertEquals("   ", s.nextLine());
  assertFalse(s.hasNextLine());
 }
 
 //make sure saving a board works correctly
 //saves pins but not dots
 @Test(timeout=2000)
 public void gameSaveBoard02() throws FileNotFoundException {
  File f = new File("./boards/saveTest.board");
  
  Dots game = new Dots(5, 4);
  game.setPosition(1,1,'^');
  game.drop(1);
  game.saveGameBoard(f);
  
  Scanner s = new Scanner(f);
  assertEquals("     ", s.nextLine());
  assertEquals(" ^   ", s.nextLine());
  assertEquals("     ", s.nextLine());
  assertEquals("     ", s.nextLine());
  assertFalse(s.hasNextLine());
 }
 
 //run the test file
 public static void main(String args[]) {
  org.junit.runner.JUnitCore.main("DotsSampleTests");
 }
}