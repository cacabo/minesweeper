public abstract class Box {

	  protected Grid grid;
	  private Position position;
	  public enum BoxState {HIDDEN, REVEALED, MARKED, UNSURE};
	  private BoxState state;
	  
	  public Box(Grid grid, Position position) {
		  this.grid = grid;
		  this.position = position;
		  this.state = BoxState.HIDDEN;
	  }
	  
	  public abstract String toString();
	  
	  public abstract void leftClick();
	  
	  public void rightClick() {
		  if (this.state == BoxState.HIDDEN) {
			  this.state = BoxState.MARKED;
			  this.grid.incNumMarked();
		  }
		  else if (this.state == BoxState.MARKED) {
			  this.state = BoxState.UNSURE;
			  this.grid.decNumMarked();
		  }
		  else if (this.state == BoxState.UNSURE)
			  this.state = BoxState.HIDDEN;
	  }
	  
	  public Position getPosition() {
		  return position;
	  }
	  
	  public void reveal() {
		  this.state = BoxState.REVEALED;
	  }
	  
	  public boolean revealed() {
		  return this.state == BoxState.REVEALED;
	  }
	  
	  public boolean marked() {
		  return this.state == BoxState.MARKED;
	  }
	  
	  public boolean hidden() {
		  return this.state == BoxState.HIDDEN;
	  }
	  
	  public boolean unsure() {
		  return this.state == BoxState.UNSURE;
	  }
	  
	  public String boxState() {
		  if (this.state == BoxState.HIDDEN)
			  return "hidden";
		  else if (this.state == BoxState.MARKED)
			  return "marked";
		  else if (this.state == BoxState.UNSURE)
			  return "unsure";
		  else
			  return "hidden";
	  }
}
