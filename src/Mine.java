
public class Mine extends Box {
	public boolean exploded;
	
	public Mine(Grid grid, Position position) {
		super(grid, position);
		this.exploded = false;
	}
	
	public void leftClick() {
		this.reveal();
		this.exploded = true;
		this.grid.lose();
	}
	
	public String toString() {
		return "M";
	}
}
