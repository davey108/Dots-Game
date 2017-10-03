import java.util.*;
import java.io.*;

//graphics packages
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  GUI for the Dots game for CS211
 *  
 *  @author Katherine (Raven) Russell
 */
class GameGUI {
 /**
  *  Frame for the GUI
  */
 private JFrame f;
 
 /**
  *  Current game being played
  */
 private Dots game = null;
 
 /**
  *  The last loaded game board (used for resetting games).
  *  This is null if the "default" board was loaded.
  */
 private File gameBoard = null;
 
 /**
  *  The panel containing the game diplay.
  */
 private JPanel gamePanel = null;
 
 /**
  *  The panel containing the step, reset, and play buttons.
  */
 private JPanel buttonPanel = null;
 
 /**
  *  Whether or not a game is currently playing with
  *  the play button (i.e. automatically playing).
  */
 private boolean playing = false;
 
 /**
  *  How wide to make the buttons representing the game board.
  */
 private final int BUTTON_WIDTH = 40;
 
 /**
  *  How tall to make the buttons representing the game board.
  */
 private final int BUTTON_HEIGHT = 40;
 
 /**
  *  How wide the default board should be.
  */
 private final int DEFAULT_BOARD_WIDTH = 10;
 
 /**
  *  How tall the default board should be.
  */
 private final int DEFAULT_BOARD_HEIGHT = 10;
 
 /**
  *  Load up the GUI!
  */
 public GameGUI() {
  f = new JFrame("Dots Game");
  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500, 540);
        f.getContentPane().setLayout(new FlowLayout());
  
  makeMenu();
  
        f.setVisible(true);
 }
 
 /**
  *  Makes the menu for the game.
  */
 public void makeMenu() {
  JMenuBar menuBar = new JMenuBar();
  JMenu menu = new JMenu("Game");

  //new default game option
  JMenuItem newGame = new JMenuItem("New Default Board");
  newGame.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent event) {
    resetBoard();
   }
  });
  menu.add(newGame);

  //new board loaded from file option
  JMenuItem loadGame = new JMenuItem("Load Board...");
  loadGame.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent event) {
    JFileChooser fc = new JFileChooser(".");
    if (fc.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
     resetBoard(fc.getSelectedFile());
    }
   }
  });
  menu.add(loadGame);

  //save current board configuration option
  JMenuItem saveBoard = new JMenuItem("Save Board...");
  saveBoard.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent event) {
    JFileChooser fc = new JFileChooser(".");
    if (fc.showSaveDialog(f) == JFileChooser.APPROVE_OPTION) {
     File saveFile = fc.getSelectedFile();
     try {
      game.saveGameBoard(saveFile);
      JOptionPane.showMessageDialog(f, "Game board saved.");
     }
     catch(FileNotFoundException e) {
      JOptionPane.showMessageDialog(f, "Could not find file.");
     }
    }
   }
  });
  menu.add(saveBoard);
  
  //exit option
  JMenuItem exit = new JMenuItem("Exit");
  exit.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent event) {
    System.exit(0);
   }
  });
  menu.add(exit);
  
  menuBar.add(menu);
  f.setJMenuBar(menuBar);
 }
 
 /**
  *  Makes the "drop" buttons and the buttons for the game grid.
  */
 public void makeGamePanel() {
  if(game == null) return;
  if(gamePanel != null) f.remove(gamePanel);
  
  gamePanel = new JPanel();
  gamePanel.setLayout(new GridLayout(game.getHeight()+1, game.getWidth()));
  
  //add drop buttons
  for (int x = 0; x < game.getWidth(); x++)
  {
   DropButton b = new DropButton(x);
   b.addDefaultActionListener();
   b.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_WIDTH));
   b.setMargin(new Insets(0,0,0,0));
   //b.setBorderPainted(false);
   b.setFocusPainted(false);
   b.setContentAreaFilled(false);
   gamePanel.add(b);
  }
  
  //add game grid
        for (int y = 0; y < game.getHeight(); y++)
        {
            for (int x = 0; x < game.getWidth(); x++)
            {
                BoardButton b = new BoardButton(x, y);
    b.addDefaultActionListener();
                b.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_WIDTH));
    b.setMargin(new Insets(0,0,0,0));
    //b.setBorderPainted(false);
    b.setFocusPainted(false);
    b.setContentAreaFilled(false);
                gamePanel.add(b);
            }
        }
  
  f.add(gamePanel, 0);
  f.revalidate();
 }
 
 /**
  *  Makes the panel containing the step, reset, and play buttons.
  */
 public void makeBottomButtons() {
  if(game == null) return;
  if(buttonPanel != null) f.remove(buttonPanel);
  
  buttonPanel = new JPanel();
  buttonPanel.setLayout(new GridLayout(1, 2));
  
  //step button
  JButton step = new JButton("Step");
  step.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent event) {
    try {
     step();
    }
    catch(NoFallingDotException e) {
     JOptionPane.showMessageDialog(f, "You must drop a dot before it can fall.");
    }
   }
  });
  buttonPanel.add(step);
  
  //reset button
  JButton reset = new JButton("Reset");
  reset.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent event) {
    resetBoard(gameBoard);
   }
  });
  buttonPanel.add(reset);
  
  //play button
  JButton play = new JButton("Play");
  play.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent event) {
    //toggle playing and not playing
    playing = !playing;
    buttonPanel.getComponent(0).setEnabled(!playing);
    buttonPanel.getComponent(1).setEnabled(!playing);
    ((JButton)buttonPanel.getComponent(2)).setText((playing ? "Stop" : "Play"));
    
    //if playing, kick off a timer to drop dots and step them
    if(playing) {
     new javax.swing.Timer(100, new ActionListener() {
      public void actionPerformed(ActionEvent event) {
       //someone hit the stop button
       if(!playing) {
        ((javax.swing.Timer)event.getSource()).stop();
        return;
       }
       //there isn't a dot currently dropping
       else if(!game.dotIsDropping()) {
        //we can drop another one
        if(game.drop(game.getWidth()/2))
         ((BoardButton)gamePanel.getComponent(game.getWidth() + game.getWidth()/2)).update();
        //we can't drop any more from this location
        else {
         ((javax.swing.Timer)event.getSource()).stop();
         playing = false;
         buttonPanel.getComponent(0).setEnabled(true);
         buttonPanel.getComponent(1).setEnabled(true);
         ((JButton)buttonPanel.getComponent(2)).setText("Play");
        }
       }
       //there is a dot currently falling, so step
       else {
        try { step(); }
        catch(NoFallingDotException e) { }
       }
      }
     }).start();
    }
   }
  });
  buttonPanel.add(play);
  
  f.add(buttonPanel, 1);
  f.revalidate();
 }
 
 /**
  *  Calls the step button on the game and updates
  *  the GUI to display the result.
  *  
  *  @return whether or not the game was able to step
  *  @throws NoFallingDotException if the game was unable to step
  */
 public boolean step() throws NoFallingDotException {
  int x = game.getCurrentDropX();
  int y = game.getCurrentDropY();
  boolean ableToStep = game.step();
  if(ableToStep) {
    ((BoardButton)gamePanel.getComponent(game.getWidth() + (game.getWidth()*y) + x)).update();
    ((BoardButton)gamePanel.getComponent(game.getWidth() + (game.getWidth()*game.getCurrentDropY()) + game.getCurrentDropX())).update();
  }
  return ableToStep;
 }
 
 /**
  *  Load a new game board of the default size.
  */
 public void resetBoard() {
  game = new Dots(DEFAULT_BOARD_WIDTH,DEFAULT_BOARD_HEIGHT);
  makeGamePanel();
  makeBottomButtons();
  gameBoard = null;
 }
 
 /**
  *  Load a new game board from a file. Displays a message if
  *  either the file can not be found or the file format is
  *  invalid.
  *  
  *  @param file the file containing the game board.
  */
 public void resetBoard(File file) {
  if(file == null) {
   resetBoard();
   return;
  }
  
  try {
   game = new Dots(file);
   makeGamePanel();
   makeBottomButtons();
   gameBoard = file;
  }
  catch(FileNotFoundException e) {
   JOptionPane.showMessageDialog(f, "Could not find file.");
  }
  catch(InvalidBoardException e) {
   JOptionPane.showMessageDialog(f, "Board in file is not valid, check that all lines have the same number of characters.");
  }
 }
 
 public static void main(String[] args) {
  new GameGUI();
 }
 
 /**
  *  Inner class representing a single square in the game.
  */
 class BoardButton extends JButton {
  /**
   *  X location of this square in the game.
   */
  private int x;
  
  /**
   *  Y location of this square in the game.
   */
  private int y;
  
  /**
   *  Set the text to whatever the grid thinks it should be.
   */
  public BoardButton(int x, int y) {
   super(""+game.getPosition(x,y));
   this.x = x;
   this.y = y;
  }
  
  /**
   *  Add an action listener that toggles blank squares into ^ squares.
   */
  public void addDefaultActionListener() {
   addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent event) {
     if(game.getPosition(x,y) == ' ') game.setPosition(x, y, '^');
     else if(game.getPosition(x,y) == '^') game.setPosition(x, y, ' ');
     else return;
     
     setText(""+game.getPosition(x,y));
    }
   });
  }
  
  /**
   *  Update the text to reflect the current state of the game
   *  for this location.
   */
  public void update() {
   setText(""+game.getPosition(x,y));
  }
 }
 
 /**
  *  Inner class for buttons that "drop" a dot at the top of the board.
  */
 class DropButton extends JButton {
  /**
   *  Where along the top the dot will drop.
   */
  private int x;
  
  /**
   *  Set the text "D" for "Drop"
   */
  public DropButton(int x) {
   super("D");
   this.x = x;
  }
  
  /**
   *  Add an action listener that will drop a dot or give an
   *  error message saying it can't be done.
   */
  public void addDefaultActionListener() {
   addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent event) {
     if(game.drop(x)) {
      ((BoardButton)gamePanel.getComponent(game.getWidth() + x)).update();
     }
     else {
      JOptionPane.showMessageDialog(f, "Can not drop, either spot is full or another dot is falling.");
     }
    }
   });
  }
 }
}