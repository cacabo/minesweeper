import java.awt.Graphics;

public class Num extends Box implements Comparable<Box> {
	private final int numMines;
	
	public Num(Grid grid, Position position) {
		super(grid, position);
		this.numMines = grid.mineCount(position);
	}

	public void leftClick() {
		if (this.grid.won() || this.grid.lost())
			  return;
		if (this.hidden()) {
			this.reveal();
			this.grid.incNumRevealed();
			if (this.grid.hasWon()) {
				grid.win();
				return;
			}
			if (this.numMines == 0)
				this.grid.cascade(this.getPosition());
		}
		else if (this.revealed())
			if (this.grid.markedCount(this.getPosition()) == this.numMines)
				this.grid.cascade(this.getPosition());
		this.grid.updateStatus();
		this.paint();
	}
	
	public int getNumMines() {
		return this.numMines;
	}
	
	public String toStringNoReveal() {
		if (this.revealed())
			return Integer.toString(this.numMines);
		else if (this.marked())
			return "!";
		else if (this.unsure())
			return "?";
		else
			return " ";
	}

	public String toString() {
		return Integer.toString(this.numMines);
	}
	
	public int compareTo(Box b) {
		return this.getPosition().compareTo(b.getPosition());
	}
	
	public void draw(Graphics g) {
		int x = this.getPosition().getCol() * Grid.scale;
		int y = this.getPosition().getRow() * Grid.scale;
		if (this.hidden())
			g.drawImage(createImage("hidden.png"), x, y, Grid.scale, Grid.scale, null);
		else if (this.marked())
			g.drawImage(createImage("marked.png"), x, y, Grid.scale, Grid.scale, null);
		else if (this.unsure())
			g.drawImage(createImage("unsure.png"), x, y, Grid.scale, Grid.scale, null);
		else
			for (int i = 0; i <= 8; i ++)
				if (this.numMines == i)
					g.drawImage(createImage(i + ".png"), x, y, Grid.scale, Grid.scale, null);
	}
}
