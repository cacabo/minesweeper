
public abstract class Box {

	  protected Grid grid;
	  private Position position;
	  private boolean revealed;
	  public enum BoxState {UNMARKED, MARKED, UNSURE};
	  private BoxState state;
	  
	  public Box(Grid grid, Position position) {
		  this.grid = grid;
		  this.position = position;
		  this.revealed = false;
		  this.state = BoxState.UNMARKED;
	  }
	  
	  public abstract String toString();
	  
	  public abstract void leftClick();
	  
	  public void rightClick() {
		  if (this.state == BoxState.UNMARKED) {
			  this.state = BoxState.MARKED;
			  this.grid.incNumMarked();
		  }
		  else if (this.state == BoxState.MARKED) {
			  this.state = BoxState.UNSURE;
			  this.grid.decNumMarked();
		  }
		  else
			  this.state = BoxState.UNMARKED;
	  }
	  
	  public Position getPosition() {
		  return position;
	  }
	  
	  public void reveal() {
		  this.revealed = true;
	  }
	  
	  public boolean revealed() {
		  return revealed;
	  }
	  
	  public boolean marked() {
		  return state == BoxState.MARKED;
	  }
}
