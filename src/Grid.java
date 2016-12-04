import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Grid extends JPanel {

	public static int scale = 64;
	private Box[][] grid;
	private int numMines;
	private int numMarked;
	private int numRevealed;
	private int time;
	public enum GameStatus {NOT_STARTED, IN_PROGRESS, LOST, WON};
	private GameStatus gameStatus;
	private JLabel status;
	private JLabel minesRemaining;
	private JLabel timeStatus;
	public enum Difficulty {BEGINNER, INTERMEDIATE, EXPERT, CUSTOM};
	private Difficulty difficulty;
	
	Timer timer = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			incTime();
		}
	});

	// Figure out how to implement the timer,
	// timer should be started * * * upon generation * * *

	public Grid(int rows, int columns, int numMines, JLabel status, JLabel minesRemaining, JLabel timeStatus) {
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		this.grid = new Box[rows][columns];
		this.numMines = numMines;
		this.numMarked = 0;
		this.gameStatus = GameStatus.NOT_STARTED;
		this.difficulty = Difficulty.CUSTOM;
		this.status = status;
		this.updateStatus();
		this.minesRemaining = minesRemaining;
		this.updateMinesRemaining();
		this.time = 0;
		this.timeStatus = timeStatus;
		this.updateTime();
		this.numRevealed = 0;
		
		setFocusable(true);
		
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int c = (int) Math.floor(x / scale);
				int y = e.getY();
				int r = (int) Math.floor(y / scale);
				
				if (SwingUtilities.isLeftMouseButton(e)) {
					leftClick(r, c);
				}
				else if (SwingUtilities.isRightMouseButton(e))
					rightClick(r, c);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}
	// Only works for BEGINNER, intermediate, expert
	
	//
	//
	//
	//
	//
	public Grid(Difficulty d, JLabel status, JLabel minesRemaining, JLabel timeStatus) {
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		if (d == Difficulty.BEGINNER) {
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
		this.gameStatus = GameStatus.NOT_STARTED;
		this.difficulty = d;
		this.status = status;
		this.updateStatus();
		this.minesRemaining = minesRemaining;
		this.updateMinesRemaining();
		this.numRevealed = 0;
		this.time = 0;
		this.timeStatus = timeStatus;
		this.updateTime();
		//Perhaps use user input?
		
		setFocusable(true);
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int c = (int) Math.floor((double)x / scale);
				int y = e.getY();
				int r = (int) Math.floor((double)y / scale);
				if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length) {
					Box b = grid[r][c];
				
					if (SwingUtilities.isLeftMouseButton(e)) {
						if (b == null)
							generate(r, c);
						b = grid[r][c];
						b.leftClick();
					}
					else if (SwingUtilities.isRightMouseButton(e))
						if (b != null)
							b.rightClick();
				}
			}
		});
	}
	
	public void reset() {
		this.grid = new Box[this.getRows()][this.getCols()];
		this.time = 0;
		this.numRevealed = 0;
		this.numMarked = 0;
		this.gameStatus = GameStatus.NOT_STARTED;
		this.updateStatus();
		this.updateMinesRemaining();
		this.updateTime();
		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}
	
	public void reset(Difficulty d) {
		if (this.difficulty == d)
			this.reset();
		Grid g = new Grid(d, new JLabel("Game Not Started"), new JLabel("Mines left: 10"), new JLabel("Time: 0"));
		
		this.grid = g.grid;
		this.difficulty = g.difficulty;
		this.numMines = g.numMines;
		this.numMarked = 0;
		this.numRevealed = 0;
		this.time = 0;
		this.gameStatus = GameStatus.NOT_STARTED;
		this.updateStatus();
		this.updateMinesRemaining();
		this.updateTime();
	}
	
	public void incNumRevealed() {
		this.numRevealed ++;
	}
	
	public int getNumRevealed() {
		return this.numRevealed;
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
	
	public void leftClick(int row, int col) {
		if (row >= 0 && col >= 0 && row < this.getRows() && col < this.getCols()) {
			Box b = this.grid[row][col];
			if (b == null)
				this.generate(row, col);
			b = this.grid[row][col];
			b.leftClick();
		}
	}
	
	public void rightClick(int row, int col) {
		if (row >= 0 && col >= 0 && row < this.getRows() && col < this.getCols()) {
			Box b = this.grid[row][col];
			if (b != null)
				b.rightClick();
		}
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
			int randomIndex = (int) Math.floor(Math.random() * openPositions.size());
			Position randomPos = openPositions.get(randomIndex);
			grid[randomPos.getRow()][randomPos.getCol()] = new Mine(this, randomPos);
			openPositions.remove(randomIndex);
			count++;
		}
		for (int r = 0; r < this.getRows(); r++)
			for (int c = 0; c < this.getCols(); c++) {
				Box b = this.grid[r][c];
				if (b == null) {
					Position p = new Position(r, c);
					this.grid[r][c] = new Num(this, p);
				}
			}
		this.leftClick(row, col);
		this.gameStatus = GameStatus.IN_PROGRESS;
		this.timer.start();
	}
	
	private void incTime() {
		this.time++;
		this.updateTime();
	}
	
	private Box getBox(Position pos) {
		return this.grid[pos.getRow()][pos.getCol()];
	}
	
	public int getNumMines() {
		return this.numMines;
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
		this.timer.stop();
		this.gameStatus = GameStatus.LOST;
	}
	
	public boolean hasWon() {
		if (this.lost()) {
			return false;
		}
//		for (int r = 0; r < grid.length; r++)
//			for (int c = 0; c < grid[0].length; c++) {
//				Box b = this.grid[r][c];
//				if (b == null || (b instanceof Num && !b.revealed()))
//					return false;
//			}
//		this.gameStatus = GameStatus.WON;
		int nums = this.getRows() * this.getCols() - this.numMines;
		if (nums == this.numRevealed) {
			this.gameStatus = GameStatus.WON;
			this.updateStatus();
			this.timer.stop();
			return true;
		}
		return false;
	}

	public int getRows() {
		return this.grid.length;
	}
	
	public int getCols() {
		Box[] arr = grid[0];
		if (arr == null)
			return 0;
		return arr.length;
	}
	
	public int getTime() {
		return time;
	}

	public boolean won() {
		return this.gameStatus == GameStatus.WON;
	}
	
	public boolean lost() {
		return this.gameStatus == GameStatus.LOST;
	}
	
	public boolean notStarted() {
		return this.gameStatus == GameStatus.NOT_STARTED;
	}
	
	public boolean inProgress() {
		return this.gameStatus == GameStatus.IN_PROGRESS;
	}
	
	public String gameStatusToString() {
		if (this.gameStatus == GameStatus.NOT_STARTED)
			return "Not Started";
		if (this.gameStatus == GameStatus.IN_PROGRESS)
			return "In Progress";
		if (this.gameStatus == GameStatus.WON)
			return "Won";
		else
			return "Lost";
	}
	
	public String difficultyToString() {
		if (this.difficulty == Difficulty.BEGINNER)
			return "BEGINNER";
		if (this.difficulty == Difficulty.INTERMEDIATE)
			return "Intermediate";
		if (this.difficulty == Difficulty.EXPERT)
			return "Expert";
		else
			return "Custom";
	}
	
	public void cascade(Position pos) {
		Set<Position> cascadeSetPositions = this.getSurroundings(pos);
		cascadeSetPositions.remove(pos);
		Set<Box> cascadeSet = new TreeSet<Box>();
		for (Position p: cascadeSetPositions) {
			Box b = this.getBox(p);
			if (b != null && b.hidden())
				cascadeSet.add(b);
		}
		for (Box b : cascadeSet) {
			b.leftClick();
		}
	}
	
	public void leftClick(Position pos) {
		Box b = this.getBox(pos);
		if (b == null)
			this.generate(pos.getRow(), pos.getCol());
		else {			
			b.leftClick();
			b.reveal();
			this.hasWon();
		}
	}
	
	public void rightClick(Position pos) {
		Box b = this.getBox(pos);
		if (!(b == null))
			b.rightClick();
	}
	
	
	public String toString() {
		String output = "   ";
		for (int i = 0; i < this.grid[0].length; i ++)
			output += " " + i;
		output += "\n";
		for (int r = 0; r < this.grid.length; r++) {
			output += r + " | ";
			for (int c = 0; c < this.grid[0].length; c++) {
				Box b = this.grid[r][c];
				if (b == null)
					output += "  ";
				else
					output += b.toStringNoReveal() + " ";
			}
			output += "|\n";
		}
		output += "Game " + this.gameStatusToString();
		return output;
	}
	
	public String toStringReveal() {
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
		output += "Game " + this.gameStatusToString();
		return output;
	}
	
	public void updateStatus() {
		this.status.setText("Game " + this.gameStatusToString());
	}
	
	public void updateMinesRemaining() {
		int minesLeft = this.numMines - this.numMarked;
		this.minesRemaining.setText("Mines left: " + minesLeft);
	}
	
	public void updateTime() {
		this.timeStatus.setText("Time: " + this.time);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.gray);
		for (int r = 0; r < this.getRows(); r++) {
			for (int c = 0; c < this.getCols(); c++) {
				Box b = grid[r][c];
				if (b == null) {
					int x = c * scale;
					int y = r * scale;
					g.drawImage(Box.createImage("hidden.png"), x, y, scale, scale, null);
				}
				else
					b.draw(g);
			}
		}
		if (this.lost()) {
			g.setColor(new Color(255, 0, 0, 48));
			g.fillRect(0, 0, this.getCols() * scale, this.getRows() * scale);
		}
		else if (this.won() || this.hasWon()) {
			g.setColor(new Color(0, 255, 0, 48));
			g.fillRect(0, 0, this.getCols() * scale, this.getRows() * scale);
		}
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.getCols() * scale, this.getRows() * scale);
	}
	
	public void instructions() {
		/*
		 * 
		 */
	}
	
//	public static void main(String[] args) {
//		Scanner scan = new Scanner(System.in);
//		Grid g = new Grid(Grid.Difficulty.BEGINNER, new JLabel("Game Not Started"));
//		System.out.println(g);
//		System.out.println("Enter click coords:\nRow: ");
//		int r = scan.nextInt();
//		System.out.println("Col: ");
//		int c = scan.nextInt();
//		System.out.println("Click at (" + r + "," + c + ")");
//		g.leftClick(r, c);
//		System.out.println(g);
//		while (g.gameStatus == GameStatus.IN_PROGRESS) {
//			String click = null;
//			System.out.println("Enter click type:");
//			System.out.print("l/r: ");
//			String s = scan.next();
//			if (s.equals("l"))
//				click = s;
//			else if (s.equals("r"))
//				click = s;
//			while (click == null) {
//				System.out.println("Invalid input, l: left, r: right");
//				s = scan.next();
//				if (s.equals("l"))
//					click = s;
//				else if (s.equals("r"))
//					click = s;
//			}
//			System.out.println("Enter click coords:");
//			System.out.print("Row: ");
//			r = scan.nextInt();
//			System.out.print("Col: ");
//			c = scan.nextInt();
//			System.out.println("Click at (" + r + "," + c + ")");
//			if (s.equals("l"))
//				g.leftClick(r, c);
//			else
//				g.rightClick(r, c);
//			System.out.println(g);
//		}
//		scan.close();
//	}
}
