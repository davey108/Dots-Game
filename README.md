# Dots-Game
This project includes a Dot-Drop game that can be play. 
A game project for CS 211 and GUI Code provided by Professor Katherine Russell

### Files Details
 - *Dots.java* - This class handle most of the game logic by setting up board, dropping the balls, and moving/ update its position.
 - *DotsInterface.java* - This is an interface contains method that *Dots.java* implements
 - *CoordTracker.java* - This class basically contains information about the current fallingdots and its status and positions.
 - *GameGUI.java* - This class is the GUI for this game. It calls to *Dots.java* for the game logic. Provided by Professor Katherine Russell
 - *InvalidBoardException.java* - This class is the custom exception and is only throw when an invalid board size is loaded into the game or a board that is not square/rectangle
 - *NoFallingDotException.java* - This class is the custom exception and is only throw when a dot that is "dead" is called to move again.
 - *MyUnitTests.java* - Personal unit test to make sure the game is running accordingly
 
 # How to Run the Game
 1. Download all the files from the repository.
 2. Compile all the above *.java* files.
 3. Run *GameGUI.java* 
 4. Press Game
 5. Choose to load a custom board in the GUI or play with the default board.
 
 # Game Menu
 - *Step* - If a ball is dropped, clicking step will emulate the 1 turn move of the ball (Stepping the ball down one spot). You can drop a ball by pressing D on the top row (if a ball is currently dropping, you cannot drop new ball!)
 - *Reset* - Clear the board of any balls to the beginning slate.
 - *Play* - Play the game automatically and keep dropping balls until the board is filled
 
 

