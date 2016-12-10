import java.awt.Graphics;

public class Mine extends Box implements Comparable<Box> {
	
	public Mine(Grid grid, Position position) {
		super(grid, position);
	}
	
	public void leftClick() {
		if (this.grid.won() || this.grid.lost())
			  return;
		this.reveal();
		this.grid.lose();
		this.grid.updateStatus();
		this.paint();
	}
	
	public String toStringNoReveal() {
		if (this.revealed())
			return "M";
		else if (this.marked())
			return "!";
		else if (this.unsure())
			return "?";
		else
			return " ";
	}
	
	public String toString() {
		return "M";
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
		else
			g.drawImage(createImage("mine.png"), x, y, grid.scale(), grid.scale(), null);
	}
}
