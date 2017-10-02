import java.io.FileNotFoundException;
import java.io.File;

/**
 *  Dots game for CS211 Summer 2016
 *  
 *  @author Katherine (Raven) Russell
 */
class DotsSampleRun {
	
	/**
	 *  A main method that drops a single ball down a game board and
	 *  uses printBoard() to display each step. If a single file is
	 *  given as a command line argument, it tries to load that file
	 *  as a board file, otherwise it does a 10x10 board with one ^.
	 */
	public static void main(String[] args) {
		Dots game = null;
		
		if(args.length == 1) {
			try{
				game = new Dots(new File(args[0]));
			}
			catch(FileNotFoundException e) {
				System.out.println("Could not find file.");
				System.exit(0);
			}
			catch(InvalidBoardException e) {
				System.out.println("Board in file is not valid, check that all lines have the same number of characters.");
				System.exit(0);
			}
		}
		else {
			game = new Dots(10, 10);
			game.setPosition(game.getWidth()/2, 1, '^');
		}
		
		game.drop(game.getWidth()/2);
		game.printBoard();
		try {
			while(game.step()) {
				game.printBoard();
			}
		}
		catch(NoFallingDotException e) {
			
		}
	}
}