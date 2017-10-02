import java.io.FileNotFoundException;
import java.io.File;

/**
 *  Dots game interface for CS211 Summer 2016
 *  
 *  @author Your Name Here
 */
interface DotsInterface {
	/**
	 *  Indicates whether or not a dot is currently in the process
	 *  of falling to the bottom of the board.
	 *  
	 *  @return whether or not a dot is currently in the process of falling to the bottom of the board
	 */
	public boolean dotIsDropping();
	
	/**
	 *  Set the board at position (x,y) to the given value.
	 *  Note: (0,0) is the top left of the board
	 *  
	 *  @param x the position across the board
	 *  @param y the position down the board
	 *  @param value the value to set the location to
	 */
	public void setPosition(int x, int y, char value);
	
	/**
	 *  Get the value at board position (x,y)
	 *  Note: (0,0) is the top left of the board
	 *  
	 *  @param x the position across the board
	 *  @param y the position down the board
	 *  @return the value at board position (x,y)
	 */
	public char getPosition(int x, int y);
	
	/**
	 *  Get how wide the board is.
	 *  
	 *  @return the width of the game board
	 */
	public int getWidth();
	
	/**
	 *  Get how tall the board is.
	 *  
	 *  @return the height of the game board
	 */
	public int getHeight();
	
	/**
	 *  If a dot is currently dropping, returns the dot's position on
	 *  the x-axis. Otherwise returns -1.
	 *  
	 *  @return the currently falling dot's x position, -1 if no currently falling dot
	 */
	public int getCurrentDropX();
	
	/**
	 *  If a dot is currently dropping, returns the dot's position on
	 *  the y-axis. Otherwise returns -1.
	 *  
	 *  @return the currently falling dot's y position, -1 if no currently falling dot
	 */
	public int getCurrentDropY();
	
	/**
	 *  Drops a dot at the top of the board at some x-axis value. If another dot
	 *  is currently dropping, no ball is dropped. Additionally, if the position
	 *  at the top already contains something (e.g. another dot or a ^), no ball
	 *  is dropped.
	 *  
	 *  @param x the location along the top where the dot should be dropped
	 *  @return whether or not the dot was able to be dropped
	 */
	public boolean drop(int x);
	
	/**
	 *  Ball drops down further.
	 *  
	 *  @returns whether or not the ball was able to move
	 *  @throws NoFallingDotException if there is no dot currently falling
	 */
	public boolean step() throws NoFallingDotException;
	
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
	public void printBoard();
	
	/**
	 *  Saves the game board to a file. Note: this does not draw the "outline"
	 *  of the board like printBoard(). This also does not draw any dots currently
	 *  on the board.
	 *  
	 *  @param f a file containing the game file
	 *  @throws FileNotFoundException if the file can not be found
	 */
	public void saveGameBoard(File f) throws FileNotFoundException;
}