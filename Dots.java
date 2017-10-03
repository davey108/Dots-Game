import java.io.*;
import java.util.*;
public class Dots implements DotsInterface{
 char[][] board; 
 CoordTracker fallingDot; 
 /**
 * A new dots game of a given width and height.
 *
 * @param width how wide the game board should be
 * @param height how tall the game board should be
 */
 public Dots(int width, int height){
   if(width < 1 || height < 1){
     System.out.println("Board dimension is invalid");
     System.exit(0);
   }
  board = new char[height][width];
  // fill the board with space (as default board look like)
  for(int i = 0; i< board.length; i++){
    for(int j = 0; j<board[i].length; j++){
      board[i][j] = ' ';
    }
  }
 }
 
 /**
 * A new dots game with the game loaded from the given file.
 *
 * @param file the file to load the game from
 * @throws FileNotFoundException if the game file specified can not be
 * found
 * @throws InvaidBoardException if the board is smaller than 1x1 or the
 * board is not a square/rectangle
 */
 public Dots(File file) throws FileNotFoundException, InvalidBoardException{
  Scanner readBoard = new Scanner(file);  
  int height = 0;
  int tempW = 0;
  int previousWidth = 0;
  while(readBoard.hasNextLine()){
   height ++;   
   tempW = readBoard.nextLine().length();
   // only store the value for the first count
   if(height == 1){
      previousWidth = tempW;
   }   
   // check if the num column is different
   // for each row
   if(previousWidth != tempW){
     throw new InvalidBoardException();
   }  
  }
  readBoard.close();
  // check for if either is 0
  if(tempW < 1 || height < 1){
   throw new InvalidBoardException();
  }
  board = new char[height][tempW];
  readBoard = new Scanner(file);
  int heightIndex = -1;  
  while(readBoard.hasNextLine()){
   heightIndex++;
   String temp = readBoard.nextLine();   
   for(int i = 0; i< temp.length(); i++){
     board[heightIndex][i] = temp.charAt(i);
   }   
  } 
 }
 /**
  *  Indicates whether or not a dot is currently in the process
  *  of falling to the bottom of the board.
  *  
  *  @return whether or not a dot is currently in the process of falling to the bottom of the board
  */
 public boolean dotIsDropping(){   
  try{
  // there is no dot falling
  if(fallingDot == null){
       return false;
     }
     else{
       int row = fallingDot.row;
       int col = fallingDot.col;
       // check for dots movability at first column
       if(col == 0){
         // if obstacle is directly below, check the two sides
         if(!(board[row+1][col] == ' ')){
           if(board[row+1][col+1] == ' ' || board[row+1][board[0].length-1] == ' '){
             return true;
           }
           // if no space is movable to, then the dot is 
           // set as dead but the board need to call
           // step() again to know its actually is not falling
           else{
             // set dot to not falling anymore
             fallingDot.setFalling();
             this.isStuckStep();
           }
         }
         else{
           return true;
         }
       }
       // check for dots at last column of the board
       else if(col == board[0].length -1){
         if(!(board[row+1][col] == ' ')){
           if(board[row+1][col-1] == ' ' || board[row+1][0] == ' '){
             return true;
           }
           else{
             fallingDot.setFalling();
             this.isStuckStep();
           }
         }
         else{
           return true;
         }
       }
       // anywhere else on the board
       else{
         if(!(board[row+1][col] == ' ')){
          if(board[row+1][col-1] == ' ' || board[row+1][col+1] == ' '){
            return true;
          }
          else{
            fallingDot.setFalling();
            this.isStuckStep();
          }
         }
         else{
           return true;
         }         
       }
     }
   }
  // if it encounter this (which is out of bound), we know that must be the last row 
  // that is giving this error
  catch (Exception e){
    fallingDot.setFalling();
    this.isStuckStep();
  }
 }
 
 /* Evaluate if this dot is still stuck after being call step() already
  * @return boolean value which say if this dot is stuck after called step()*/
  public boolean isStuckStep(){
	  // isStuckCall is a condition to check if step()
      // has already been called on this dead dot
	  if(fallingDot.isStuckCall()){
		  // reset the fallingDot because this one is done
		  fallingDot = null;
		  return false;
	  }
	  return true;
  }

 
 /**
  *  Set the board at position (x,y) to the given value.
  *  Note: (0,0) is the top left of the board
  *  
  *  @param x the position across the board
  *  @param y the position down the board
  *  @param value the value to set the location to
  */
 public void setPosition(int x, int y, char value){
  // check for out of bound
  if(x > getWidth() -1 || y > getHeight() -1 || x < 0 || y < 0){
   System.out.println("Invalid location");
  }
  else{
    board[y][x] = value;
  }
 }
 
 /**
  *  Get the value at board position (x,y)
  *  Note: (0,0) is the top left of the board
  *  
  *  @param x the position across the board
  *  @param y the position down the board
  *  @return the value at board position (x,y)
  */
 public char getPosition(int x, int y){ 
  // out of bound check
  if(x > getWidth() -1 || y > getHeight() -1 || x < 0 || y < 0){
   System.out.println("Invalid location");
  }
  else{
    return board[y][x];
  }
  return board[y][x];
 }
 
 /**
  *  Get how wide the board is.
  *  
  *  @return the width of the game board
  */
 public int getWidth(){
  return board[0].length;
 }
 
 /**
  *  Get how tall the board is.
  *  
  *  @return the height of the game board
  */
 public int getHeight(){
  return board.length;
 }
 
 /**
  *  If a dot is currently dropping, returns the dot's position on
  *  the x-axis. Otherwise returns -1.
  *  
  *  @return the currently falling dot's x position, -1 if no currently falling dot
  */
 public int getCurrentDropX(){
  // calling dotIsDropping to check if there
  // is any dot that is lookable for x or y (even if dead dot)
  // as long as it hasn't been called by step() after dead
  if(dotIsDropping()){
   return fallingDot.col;
  }
  return -1;
 }
 
 /**
  *  If a dot is currently dropping, returns the dot's position on
  *  the y-axis. Otherwise returns -1.
  *  
  *  @return the currently falling dot's y position, -1 if no currently falling dot
  */
 public int getCurrentDropY(){  
  if(dotIsDropping()){
    return fallingDot.row;
  }
  return -1;
 }
 
 /**
  *  Drops a dot at the top of the board at some x-axis value. If another dot
  *  is currently dropping, no ball is dropped. Additionally, if the position
  *  at the top already contains something (e.g. another dot or a ^), no ball
  *  is dropped.
  *  
  *  @param x the location along the top where the dot should be dropped
  *  @return whether or not the dot was able to be dropped
  */
 public boolean drop(int x){
  // check if any other dot is dropping
  if(dotIsDropping()){
   return false;
  }
  // if the position is filled, then the drop can't be done
  if(board[0][x] == ('^') || board[0][x] == ('O')){
    return false;
   }
  // set that pos to a dot shown
  setPosition(x,0,'O');
  // the dot position is a valid one and will fall
  // so set this to position to a new fallingDot
  // to keep track
  fallingDot = new CoordTracker(0,x);
  return true; 
 }
 
 /**
  *  Ball drops down further.
  *  
  *  @returns whether or not the ball was able to move
  *  @throws NoFallingDotException if there is no dot currently falling
  */
 public boolean step() throws NoFallingDotException{
  // if no dot can be fall then throw this exception
  if(!dotIsDropping()){   
   throw new NoFallingDotException();    
  }
  
  // if a stuck dot has been called, then
  // check if it has been called before,
  // if is not, then return false and put the check that it
  // cannot be call anymore
  if(!fallingDot.isStuckCall() && !fallingDot.isFalling()){
    fallingDot.setStuckCall();
    return false;
  }  
  else{
    int row = fallingDot.row;
    int col = fallingDot.col;
    Random rand = new Random();
    // true is right and false is left
    boolean direction = rand.nextBoolean();
    // if dot is in first column
    if(col == 0){
       if(!(board[row+1][col] == ' ')){
         // force move left because right is filled
         if(!(board[row+1][col+1] == ' ')){
           board[row+1][board[0].length-1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = board[0].length-1;
           return true;
         }
         // force move right because left is filled
         if(!(board[row+1][board[0].length-1] == ' ')){
           board[row+1][col+1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = col+1;
           return true;
         }
         // move right
         if(direction == true){
           board[row+1][col+1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = col+1;
           return true;
         }
         // move left
         if(direction == false){
           board[row+1][board[0].length-1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = board[0].length-1;
           return true;
         }
       }
       // fall straight down
       else{
         board[row+1][col] = 'O';
         board[row][col] = ' ';
         fallingDot.row = row+1;
         return true;
       }
     }
     else if(col == board[0].length -1){
       if(!(board[row+1][col] == ' ')){
         // force move left because right is filled
         if(board[row+1][0] == '^' || board[row+1][0] == 'O'){
           board[row+1][col-1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = col-1;
           return true;
         }
         // force move right because left is filled
         if(!(board[row+1][col-1] == ' ')){
           board[row+1][0] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = 0;
           return true;
         }
         // move right
         if(direction == true){
           board[row+1][0] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = 0;
           return true;
         }
         // move left
         if(direction == false){
           board[row+1][col-1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = col-1;
           return true;
         }
       }
       // fall straight down
       else{
         board[row+1][col] = 'O';
         board[row][col] = ' ';
         fallingDot.row = row+1;
         return true;
       }
     }
     else{
       // if below has blocker
        if(!(board[row+1][col] == ' ')){
         // force move left because right is filled
         if(!(board[row+1][col+1] == ' ')){
           board[row+1][col-1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = col-1;
           return true;
         }
         // force move right because left is filled
         if(!(board[row+1][col-1] == ' ')){
           board[row+1][col+1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = col+1;
           return true;
         }
         // move right
         if(direction == true){
           board[row+1][col+1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = col+1;
           return true;
         }
         // move left
         if(direction == false){
           board[row+1][col-1] = 'O';
           board[row][col] = ' ';
           fallingDot.row = row+1;
           fallingDot.col = col-1;
           return true;
         }
       }
       // fall straight down
       else{
         board[row+1][col] = 'O';
         board[row][col] = ' ';
         fallingDot.row = row+1;
         return true;
       }
     }
  }
  return false;
 }
 
 /**
  *  Prints an ascii art version of the current game board. Left and right
  *  borders are drawn with pipes ('|') and bottom border is drawn with
  *  dashes ('-'). For example, an empty 3x3 board would be drawn as:
  *  
  *  |   |
  *  |   |
  *  |   |
  *  -----
  */
 public void printBoard(){  
  for(int i = 0; i< board.length; i++){
   String output = "";
   output += "|";
   // print whatever is in between the two "|"
   for(int j = 0; j<board[i].length;j++){
    output += Character.toString(board[i][j]);
   }
   output += "|";
   System.out.println(output);
  }
  // print out the bottom dash and add 2
  // because the two "|"
  int bottomDash = getWidth() + 2;
  for(int a = 0; a<bottomDash; a++){
    if(a<bottomDash-1){
      System.out.print("-");
    }
    // last "-" should be with a "\n" at end
    else{
      System.out.println("-");
    }
  }
 }
 
 /**
  *  Saves the game board to a file. Note: this does not draw the "outline"
  *  of the board like printBoard(). This also does not draw any dots currently
  *  on the board.
  *  
  *  @param f a file containing the game file
  *  @throws FileNotFoundException if the file can not be found
  */
 public void saveGameBoard(File f) throws FileNotFoundException{
  PrintWriter out = new PrintWriter(f);
  for(int i = 0; i<board.length; i++){
   for(int j = 0; j<board[i].length; j++){
      if(j<board[i].length-1){
        // avoid saving in the dot and replace with space
        if(board[i][j] == ('O')){
         out.print(' ');
         out.flush();
         
      }
        else{
          out.print(board[i][j]);
          out.flush();
        }
      }
      // last column so add a new line
      else{
        if(board[i][j] == ('O')){
          out.println(' ');
          out.flush();
        }
        else{
          out.println(board[i][j]);
          out.flush();
        }
      }
   }
  }
  out.close();
 } 
}