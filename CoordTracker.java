// a class for tracking of the falling
// dots location
public class CoordTracker{
  // fields to keep track if a dot is falling
  // and if it has been called step() again
  // after it has stopped
  boolean falling = true;
  boolean stuckCall;
  int row;
  int col;
  public CoordTracker(int row, int col){
    this.row = row;
    this.col = col;
  }   
  public void setFalling(){
   this.falling = false;
  }
  public boolean isFalling(){
    return this.falling;
  }
  // evaluate the dot from being not movable
  // to actually not falling after being called
  // step() once more after it already stopped
  public void setStuckCall(){
    this.stuckCall = true;
  }
  public boolean isStuckCall(){
    return this.stuckCall;
  }
  // a convinient method should the game
  // board later have multiple falling
  // at a time (easier to check the object
  // at row,col location)
  public boolean equal(int row, int col){    
    return this.row == row && this.col == col;
  }
  

}