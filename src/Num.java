import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

public class Num extends Box implements Comparable<Box> {
	private final int numMines;
	
	public Num(Grid grid, Position position) {
		super(grid, position);
		this.numMines = grid.mineCount(position);
	}
	
	public Num(Grid grid, Position position, int num) {
		super(grid, position);
		this.numMines = num;
	}

	public void leftClick() {
		if (this.grid.won() || this.grid.lost())
			  return;
		if (this.hidden()) {
			this.reveal();
			this.grid.incNumRevealed();
			if (this.grid.hasWon()) {
				try {
					grid.win();
				} catch (Exception e) {
				}
				return;
			}
			if (this.numMines == 0)
				this.grid.cascade(this.getPosition());
		}
		this.grid.updateStatus();
		this.paint();
	}
	
	public void doubleLeftClick() {
		if (this.grid.won() || this.grid.lost())
			  return;
		if (this.hidden()) {
			this.reveal();
			this.grid.incNumRevealed();
			if (this.grid.hasWon()) {
				try {
					grid.win();
				} catch (Exception e) {
				}
				return;
			}
			if (this.numMines == 0)
				this.grid.cascade(this.getPosition());
		}
		else if (this.revealed()) {
			if (this.grid.markedCount(this.getPosition()) == this.numMines)
				this.grid.cascade(this.getPosition());
			else {
				this.setState(BoxState.CHECKED);
				this.paint();
				try {
					TimeUnit.MILLISECONDS.sleep(125);
				} catch (Exception e) {
				}
				this.setState(BoxState.REVEALED);
			}
		}
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
		int x = this.getPosition().getCol() * grid.scale();
		int y = this.getPosition().getRow() * grid.scale();
		if (this.hidden())
			g.drawImage(createImage("hidden.png"), x, y, grid.scale(), grid.scale(), null);
		else if (this.marked())
			g.drawImage(createImage("marked.png"), x, y, grid.scale(), grid.scale(), null);
		else if (this.unsure())
			g.drawImage(createImage("unsure.png"), x, y, grid.scale(), grid.scale(), null);
		else if (this.checked()) {
			g.setColor(Color.RED);
			Graphics2D g2 = (Graphics2D) g;
		    g2.setStroke(new BasicStroke(3));
			g2.drawLine(x + 2, y + 2, x - 2 + grid.scale(), y - 2 + grid.scale());
			g2.drawLine(x - 2 + grid.scale(), y + 2, x + 2, y - 2 + grid.scale());
		}
		else
			for (int i = 0; i <= 8; i ++)
				if (this.numMines == i)
					g.drawImage(createImage(i + ".png"), x, y, grid.scale(), grid.scale(), null);
	}
}
