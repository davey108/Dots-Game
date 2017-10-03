/* Test File to make some brand new boards and test the game 
 * to assure the game is running correctly and the balls are falling as described */
import java.io.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MyUnitTests {
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
 // create a sample test file (small so it doesn't kill the memory when 
 // filling the game)
 @BeforeClass
 public static void printSampleBoard() throws IOException, InvalidBoardException{
  PrintWriter out = new PrintWriter("SmileWorld.board");
  out.println("        ");
  out.println(" ^    ^ ");  
  out.println("        ");
  out.println("^      ^");
  out.println(" ^    ^ ");  
  out.println("   ^^   ");
  out.close();
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
 // check if the board has any empty space
 // aka means some stuff can still move
 public static boolean searchMovable(Dots game){
   char [][] board = game.board;
   for(int i = 0; i< board.length; i++){
     for(int j = 0; j<board[0].length; j++){
       if(board[i][j] == ' '){
         return true;
       }
     }
   }
   return false;
 }
 @Test(timeout = 40000)
 public void testingTrueBoard()throws FileNotFoundException,IOException, InvalidBoardException, NoFallingDotException{
   Dots game = new Dots(new File("SmileWorld.board")); 
   // no dot is dropping so should be false
   assertEquals(false,game.dotIsDropping());    
   assertEquals(true,game.drop(0));
   for(int i = 0; i < 3; i++){
     game.step();
   }
   // check if it falls to the right correctly
   assertEquals(1,game.getCurrentDropX());
   assertEquals(3,game.getCurrentDropY());
   game.step();
   // check whether the ball fall right or left
   // correctly
   if(game.getPosition(0,4) == ' '){
     assertEquals('O',game.getPosition(2,4));
     game.step();
     assertEquals('O',game.getPosition(2,5));     
     // should be false because technically, the
     // dot is still "falling" until step is called
     assertEquals(false,game.drop(0));
     // should say false now because ball stopped moving
     assertEquals(false,game.step());
   }
   else if(game.getPosition(2,4) == ' '){
     assertEquals('O',game.getPosition(0,4));
     game.step();     
     // deviate from above path, x should be at 0
     assertEquals(0,game.getCurrentDropX());
     // should be false because technically, the
     // dot is still "falling" until step is called
     assertEquals(false,game.drop(0));
     assertEquals(false,game.step());
   }
   // doesn't matter what we set it to
   // game should be able to say this is 
   // not a movable to spot
   game.setPosition(2,4,'*');
   // should allow us to drop a new ball
   assertEquals(true,game.drop(0));
   for(int i = 0; i< 4; i++){
     game.step();
   }  
   // check if the first ball took the left path down
   // and dictate how should 2nd ball fall
   if(game.getPosition(0,5) == ' '){
     game.step();
     assertEquals('O',game.getPosition(0,5));
     game.step();
     // out of bound so -1
     assertEquals(-1,game.getCurrentDropX());
   }
   // if not a space then must be a ball under
   // should fall to the other right
   else if(game.getPosition(0,5) != ' '){
     // this line affect the below printBoard() code
     game.setPosition(1,5,'^');
     game.step();
     assertEquals(7,game.getCurrentDropX());
     assertEquals(5,game.getCurrentDropY());
     game.step();
     // out of bound so -1
     assertEquals(-1,game.getCurrentDropY());
   }
   // let the code play the game and fill up the board
   while(searchMovable(game)){
     int max = 8;
     int min = 0;
     // generate random number to drop at the number column
     Random dropGenerator = new Random();
     int randomDropper = dropGenerator.nextInt(max-min) + min;
     // check if that column is legit place to drop, if it
     // isn't then move to different one
     if(game.getPosition(randomDropper,0) == ' '){
       game.drop(randomDropper);
       while(game.step()){         
       }
     }     
   }
   game.printBoard();
   // because the different path taken
   // and setting position, such case
   // need to be check at the diffent
   // position (at 1,5 for ball or block)
   if(game.getPosition(1,5) == 'O'){
     String output = "|OOOOOOOO|" +newline+
                   "|O^OOOO^O|" +newline+ 
                   "|OOOOOOOO|" +newline+
                   "|^OOOOOO^|" +newline+
                   "|O^*OOO^O|" +newline+
                   "|OOO^^OOO|" +newline+
                   "----------" +newline;
     assertEquals(output,localOut.toString());
   }
   else if(game.getPosition(1,5) != 'O'){
    String output = "|OOOOOOOO|" +newline+
                   "|O^OOOO^O|" +newline+ 
                   "|OOOOOOOO|" +newline+
                   "|^OOOOOO^|" +newline+
                   "|O^*OOO^O|" +newline+
                   "|O^O^^OOO|" +newline+
                   "----------" +newline;
     assertEquals(output,localOut.toString());
   }
   File f = new File("SmileWorldEdit.board");   
   game.saveGameBoard(f);
   Dots game2 = new Dots(new File("SmileWorldEdit.board"));
   // check if the saved game file has same num row and col as
   // the first one (because in dimension, they are same)
   assertEquals(8,game2.getWidth());
   assertEquals(6,game.getHeight());
   // check to see if the difference maker, the item
   // at spot 1,5 is correctly stored also
   if(game.getPosition(1,5) == 'O'){
     assertEquals(' ',game2.getPosition(1,5));
   }   
 }
 public static void main(String args[]) {
  org.junit.runner.JUnitCore.main("MyUnitTests");
 }
}
