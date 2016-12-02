
public class Num extends Box {
	private int numMines;
	
	public Num(Grid grid, Position position) {
		super(grid, position);
		this.numMines = grid.mineCount(position);
	}

	public void leftClick() {
		if (this.revealed())
			if (this.grid.markedCount(this.getPosition()) == this.numMines)
				this.grid.cascade(this.getPosition());
		else {
			this.reveal();
			if (this.numMines == 0)
				this.grid.cascade(this.getPosition());
		}
	}
	
	public int getNumBombs() {
		return this.numMines;
	}
	
	public String toString() {
		return Integer.toString(this.numMines);
	}
}
