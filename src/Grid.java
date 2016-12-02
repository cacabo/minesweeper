import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Grid {
	private Box[][] grid;
	private int numMines;
	private int numMarked;
	private boolean won;
	private boolean lost;
	public enum Difficulty {EASY, INTERMEDIATE, EXPERT, CUSTOM}
	private Difficulty difficulty;
	// Figure out how to implement the timer,
	// timer should be started * * * upon generation * * *

	public Grid(int rows, int columns, int numMines) {
		this.grid = new Box[rows][columns];
		this.numMines = numMines;
		this.numMarked = 0;
		this.won = false;
		this.lost = false;
		this.difficulty = Difficulty.CUSTOM;
	}
	
	// Only works for easy, intermediate, expert
	public Grid(Difficulty d) {
		if (d == Difficulty.EASY) {
			this.grid = new Box[8][8];
			this.numMines = 10;
		}
		else if (d == Difficulty.INTERMEDIATE) {
			this.grid = new Box[16][16];
			this.numMines = 40;
		}
		else { //Assumes expert
			this.grid = new Box[16][32];
			this.numMines = 99;
		}
		this.numMarked = 0;
		this.won = false;
		this.lost = false;
		this.difficulty = d;
		//Perhaps use user input?
	}
	
	private Set<Position> getSurroundings(Position p) {
		int spanUp = 1, spanDown = 1, spanRight = 1, spanLeft = 1;
		if (p.getRow() - 1 < 0)
			spanUp = 0;
		if (p.getCol() - 1 < 0)
			spanLeft = 0;
		if (p.getRow() + 1 >= grid.length)
			spanDown = 0;
		if (p.getCol() + 1 >= grid[0].length)
			spanRight = 0;
		Set<Position> positions = new TreeSet<Position>();
		for (int r = p.getRow() - spanUp; r <= p.getRow() + spanDown; r++)
			for (int c = p.getCol() - spanLeft; c <= p.getCol() + spanRight; c++)
				positions.add(new Position(r, c));
		return positions;
	}
	
	public int mineCount(Position pos) {
		int mineCount = 0;
		Set<Position> surroundings = this.getSurroundings(pos);
		for (Position p : surroundings) {
			Box b = this.grid[p.getRow()][p.getCol()];
			if (!(b == null) && b instanceof Mine)
				mineCount++;
		}
		return mineCount;
	}
	
	public void generate(int row, int col) {
		Set<Position> surroundings = getSurroundings(new Position(row, col));
		
		List<Position> openPositions = new ArrayList<Position>();
		for (int r = 0; r < grid.length; r++)
			for (int c = 0; c < grid[0].length; c++) {
				Position p = new Position(r, c);
				if (!surroundings.contains(p))
					openPositions.add(p);
			}
		
		int count = 0;
		while (count < this.numMines) {
			int randomIndex = (int) Math.round(Math.random() * openPositions.size());
			Position randomPos = openPositions.get(randomIndex);
			grid[randomPos.getRow()][randomPos.getCol()] = new Mine(this, randomPos);
			openPositions.remove(randomIndex);
			count++;
		}
		for (int r = 0; r < this.getHeight(); r++)
			for (int c = 0; c < this.getWidth(); c++) {
				Box b = this.grid[r][c];
				if (b == null) {
					Position p = new Position(r, c);
					this.grid[r][c] = new Num(this, p);
				}
			}
	}
	
	private Box getBox(Position pos) {
		return this.grid[pos.getRow()][pos.getCol()];
	}
	
	public int getNumMines() {
		return this.numMines;
	}
	
	public void incNumMarked() {
		this.numMarked++;
	}
	
	public void decNumMarked() {
		this.numMarked--;
	}
	
	public int getNumMarked() {
		return this.numMarked;
	}
	
	public int getNumUnmarked() {
		return this.numMines - this.numMarked;
	}
	
	public int markedCount(Position pos) {
		int markedCount = 0;
		Set<Position> surroundings = this.getSurroundings(pos);
		for (Position p : surroundings) {
			Box b = this.grid[p.getRow()][p.getCol()];
			if (!(b == null) && b.marked())
				markedCount++;
		}
		return markedCount;
	}

	// When a user loses the whole board should be revealed
	public void lose() {
		for (int r = 0; r < grid.length; r++)
			for (int c = 0; c < grid[0].length; c++) {
				Box b = this.grid[r][c];
				b.reveal();
			}
		this.lost = true;
	}
	
	public boolean hasWon() {
		for (int r = 0; r < grid.length; r++)
			for (int c = 0; c < grid[0].length; c++) {
				Box b = this.grid[r][c];
				if (b instanceof Num && !b.revealed())
					return false;
			}
		this.won = true;
		return true;
	}

	public int getHeight() {
		return this.grid.length;
	}
	
	public int getWidth() {
		Box[] arr = grid[0];
		if (arr == null)
			return 0;
		return arr.length;
	}

	public boolean won() {
		return won;
	}
	
	public boolean lost() {
		return lost;
	}
	
	public String getDifficulty() {
		if (this.difficulty == Difficulty.EASY)
			return "Easy";
		if (this.difficulty == Difficulty.INTERMEDIATE)
			return "Intermediate";
		if (this.difficulty == Difficulty.EXPERT)
			return "Expert";
		else
			return "Custom";
	}
	
	public void cascade(Position pos) {
		Set<Position> cascadeSetPositions = this.getSurroundings(pos);
		cascadeSetPositions.remove(this.getBox(pos));
		Set<Box> cascadeSet = new TreeSet<Box>();
		for (Position p: cascadeSetPositions)
			cascadeSet.add(this.getBox(p));
		for (Box b : cascadeSet)
			if (!(b == null) && b instanceof Num && ((Num)b).getNumBombs() == 0)
				cascade(b.getPosition());
	}
	
	public void leftClick(Position pos) {
		Box b = this.getBox(pos);
		if (b == null)
			this.generate(pos.getRow(), pos.getCol());
		else
			b.leftClick();
	}
	
	public void rightClick(Position pos) {
		Box b = this.getBox(pos);
		if (!(b == null))
			b.rightClick();
	}
	
	public String toString() {
		String output = "";
		for (int r = 0; r < this.grid.length; r++) {
			for (int c = 0; c < this.grid[0].length; c++) {
				Box b = this.grid[r][c];
				if (b == null)
					output += "n ";
				else
					output += b.toString() + " ";
			}
			output += "\n";
		}
		if (this.lost)
			output += "Game lost";
		else if (this.hasWon())
			output += "Game won";
		else
			output += "\nGame still in progress";
		return output;
	}
	
	public static void main(String[] args) {
		Grid g = new Grid(Grid.Difficulty.EASY);
		System.out.println("Grid width: " + g.grid.length);
		System.out.println("Grid height: " + g.grid[0].length + "\n");
		System.out.println("Click at (2,2):\n");
		g.generate(2, 2);
		System.out.println(g);
	}
}
