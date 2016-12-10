import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Grid extends JPanel {

	public int scale() {
		if (this.getRows() < 16)
			return 64;
		if (this.getRows() < 32)
			return 48;
		return 32;
	}
	
	private Box[][] grid;
	private int numMines;
	private int numMarked;
	private int numRevealed;
	private int time;
	private String name = "user";
	public enum GameStatus {NOT_STARTED, IN_PROGRESS, LOST, WON};
	private GameStatus gameStatus;
	private JLabel status;
	private JLabel minesRemaining;
	private JLabel timeStatus;
	public enum Difficulty {BEGINNER, INTERMEDIATE, EXPERT, CUSTOM};
	private Difficulty difficulty;
	
	private Timer timer = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			incTime();
		}
	});

	private void jLabelHelper(JLabel status, JLabel minesRemaining, JLabel timeStatus) {
		this.numMarked = 0;
		this.gameStatus = GameStatus.NOT_STARTED;
		this.status = status;
		this.updateStatus();
		this.minesRemaining = minesRemaining;
		this.updateMinesRemaining();
		this.time = 0;
		this.timeStatus = timeStatus;
		this.updateTime();
		this.numRevealed = 0;
	}
	
	public void mouseListenerHelper() {
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int c = (int) Math.floor(x / scale());
				int y = e.getY();
				int r = (int) Math.floor(y / scale());
				
				if (SwingUtilities.isLeftMouseButton(e))
					leftClick(r, c);
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

	public Grid(int rows, int columns, int numMines, JLabel status, JLabel minesRemaining, JLabel timeStatus) {
//		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		this.grid = new Box[rows][columns];
		this.numMines = numMines;
		this.difficulty = Difficulty.CUSTOM;
		this.jLabelHelper(status, minesRemaining, timeStatus);
		this.mouseListenerHelper();
	}
	
	public Grid(Difficulty d, JLabel status, JLabel minesRemaining, JLabel timeStatus) {
//		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		if (d == Difficulty.BEGINNER) {
			this.grid = new Box[8][8];
			this.numMines = 10;
		}
		else if (d == Difficulty.INTERMEDIATE) {
			this.grid = new Box[16][16];
			this.numMines = 40;
		}
		else {
			this.grid = new Box[16][32];
			this.numMines = 99;
		}
		this.difficulty = d;
		this.jLabelHelper(status, minesRemaining, timeStatus);
		this.mouseListenerHelper();
	}
	
//	public Grid(char[][] arr, Difficulty d, JLabel status, JLabel minesRemaining, JLabel timeStatus) {
//		System.out.println("Arr length: " + arr.length);
//		System.out.println(arr[0] == null);
//		this.grid = new Box[arr.length][arr[0].length];
//		for (int r = 0; r < arr.length; r++) {
//			for (int c = 0; c < arr[0].length; c++) {
//				char s = arr[r][c];
//				Position p = new Position(r, c);
//				if (s == 'M')
//					this.grid[r][c] = new Mine(this, p);
//				for (int num = 0; num <= 8; num++) {
//					if (s == 48 + num)
//						this.grid[r][c] = new Num(this, p, num);
//				}
//			}
//		}
//		this.difficulty = d;
//		this.jLabelHelper(status, minesRemaining, timeStatus);
//		this.mouseListenerHelper();
//	}
	
	private void resetHelper() {
		this.time = 0;
		this.numMarked = 0;
		this.numRevealed = 0;
		this.gameStatus = GameStatus.NOT_STARTED;
		this.timer.stop();
		this.name = "user";
		this.updateStatus();
		this.updateMinesRemaining();
		this.updateTime();
		this.repaint();
	}
	
	public void reset() {
		this.grid = new Box[this.getRows()][this.getCols()];
		this.resetHelper();
	}
	
	public void reset(Difficulty d) {
		if (this.difficulty == d)
			this.reset();
		Grid g = new Grid(d, new JLabel("Game Not Started"), new JLabel("Mines left: 10"), new JLabel("Time: 0"));
		this.grid = g.grid;
		this.difficulty = g.difficulty;
		this.numMines = g.numMines;
		this.resetHelper();
	}
	
	public void reset(int row, int col, int mines) {
		this.grid = new Box[row][col];
		this.difficulty = Difficulty.CUSTOM;
		this.numMines = mines;
		this.resetHelper();
	}
	
	public void replay() throws IOException {
		char[][] arr = Grid.readGrid();
		this.grid = new Box[arr.length][arr[0].length];
		for (int r = 0; r < arr.length; r++) {
			for (int c = 0; c < arr[0].length; c++) {
				char s = arr[r][c];
				Position p = new Position(r, c);
				if (s == 'M')
					this.grid[r][c] = new Mine(this, p);
				for (int num = 0; num <= 8; num++) {
					if (s == 48 + num)
						this.grid[r][c] = new Num(this, p, num);
				}
			}
		}
		
		
		this.resetHelper();
		
		System.out.println("\nGraphics null in constructor: ");
		System.out.println(this.getGraphics() == null);
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
	
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
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
			if (this.gameStatus == GameStatus.NOT_STARTED)
				this.gameStatus = GameStatus.IN_PROGRESS;
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
		if (this.time == 999)
			return;
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
		System.out.println("Game lost");
		for (int r = 0; r < grid.length; r++)
			for (int c = 0; c < grid[0].length; c++) {
				Box b = this.grid[r][c];
				b.reveal();
			}
		this.timer.stop();
		this.gameStatus = GameStatus.LOST;
		this.repaint();
		try {
			this.writeGrid();
		} catch (IOException e) {
			System.out.println("Exception caught while writing!");
		}
		
		JFrame lose = new JFrame();
			
		JLabel nameLabel = new JLabel("<html>Oh no! you lost the game.");
	    nameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 18));
			
		JButton replay = new JButton("Replay");
		replay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					replay();
				} catch (IOException exception) {
					System.out.println("Caught IO Exception");
				}
				lose.dispose();
			}
		});
		
		JButton reset = new JButton("New Game");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
				lose.dispose();
			}
		});
		
		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JPanel buttons = new JPanel();
		buttons.add(replay);
		buttons.add(reset);
		buttons.add(quit);
		
		JPanel wrapper = new JPanel();
		wrapper.setAlignmentY(TOP_ALIGNMENT);
		wrapper.add(nameLabel);
		wrapper.add(buttons);
		
		lose.add(nameLabel);
		lose.add(buttons);
		lose.pack();
		lose.setVisible(true);
		
		System.out.println("Lost method completed");
	}
	
	public boolean hasWon() {
		if (this.lost()) {
			return false;
		}
		int nums = this.getRows() * this.getCols() - this.numMines;
		return nums == this.numRevealed;
	}

	public void win() throws IOException {		
		this.gameStatus = GameStatus.WON;
		this.updateStatus();
		this.timer.stop();
		this.repaint();
		try {
			if (this.name.equals("user") && this.isHighScore()) {
				JLabel nameLabel = new JLabel("<html>Congrats! You got a high score.<br>"
						+ "please enter your name:</html>");
				nameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 18));
						
				JTextField nameField = new JTextField(8);
				
				JPanel namePanel = new JPanel();
				namePanel.add(nameLabel);
				namePanel.add(nameField);
				
				int result = JOptionPane.showConfirmDialog(
						null,
						namePanel,
						"Enter Your Name",
						JOptionPane.OK_CANCEL_OPTION
						);
				if (result == JOptionPane.OK_OPTION) {
					try {
						String name = nameField.getText();
						
						if (name == null || name.length() < 2 || name.length() > 18)
							throw new IllegalArgumentException();
						else
							this.setName(name);
					} catch(Exception error) {
						JOptionPane.showMessageDialog(null,
							    "Input Exception.",
							    "Inane error",
							    JOptionPane.ERROR_MESSAGE
							    );
					}
				}					
			}
		} catch (IOException e) {
		}
		
		this.writeHighScores();
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
					int x = c * scale();
					int y = r * scale();
					g.drawImage(Box.createImage("hidden.png"), x, y, scale(), scale(), null);
				}
				else
					b.draw(g);
			}
		}
		if (this.lost()) {
			g.setColor(new Color(255, 0, 0, 48));
			g.fillRect(0, 0, this.getCols() * scale(), this.getRows() * scale());
		}
		else if (this.won()) {
			g.setColor(new Color(0, 255, 0, 48));
			g.fillRect(0, 0, this.getCols() * scale(), this.getRows() * scale());
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.getCols() * scale(), this.getRows() * scale());
	}
	
	private String scoreToString() {
		return "" + this.name + "," + this.time;
	}
	
	public boolean isHighScore() throws IOException {
		if (this.lost() || !this.won())
			return false;
		List<Integer> scores = Grid.highScoresToInts(this.difficulty);
		if (scores.size() < 5)
			return true;
		return this.time < scores.get(4);
	}
	
	private List<String> newHighScoresToString() throws IOException {		
		List<String> strings = Grid.highScoresToStrings(this.difficulty);		
		List<Integer> ints = Grid.highScoresToInts(strings);
		if (strings == null || strings.isEmpty()) {
			List<String> l = new LinkedList<String>();
			l.add(this.scoreToString());
			return l;
		}
		int index = 0;
		for (int i : ints) {
			if (this.time >= i)
				index++;
		}
		if (index < strings.size()) {
			strings.add(index, "" + this.scoreToString());
			if (strings.size() == 6)
				strings.remove(5);
		}
		else if (index < 5)
			strings.add(this.scoreToString());
		return strings;
	}
	
	public void writeHighScores() throws IOException {
		if (!this.isHighScore())
			return;
		List<String> strings = newHighScoresToString();
		PrintWriter writer = new PrintWriter(this.difficultyToString().toString() + ".txt");
		for (String s : strings)
			writer.println(s);
		writer.close();
	}
	
	public static List<String> highScoresToStrings(Difficulty d) throws IOException {
		List<String> l = new LinkedList<String>();
		BufferedReader r = new BufferedReader(new FileReader(d.toString() + ".txt"));
		try {
			boolean done = false;
			while (!done) {
				String line = r.readLine();
				if (line == null)
					done = true;
				else
					l.add(line);
			}
		}
		catch (IOException e) {
		} finally {
			r.close();
		}
		return l;
	}
	
	private static List<Integer> highScoresToInts(Difficulty d) throws IOException {
		List<String> strings = highScoresToStrings(d);
		return highScoresToInts(strings);
	}
	
	public static List<Integer> highScoresToInts(List<String> strings) throws IOException {
		List<Integer> ints = new LinkedList<Integer>();
		try {
			for (String s : strings) {
				int i = s.indexOf(",");
				if (i == -1)
					throw new IOException();
				Integer score = Integer.parseInt(s.substring(i + 1));
				ints.add(score);
			}
		}
		catch (IOException e) {
		}
		return ints;
	}
	
	public static List<String> highScoresToNames(List<String> strings) throws IOException {
		List<String> names = new LinkedList<String>();
		try {
			for (String s : strings) {
				int i = s.indexOf(",");
				if (i == -1)
					throw new IOException();
				String name = s.substring(0, i);
				names.add(name);
			}
		}
		catch (IOException e) {
		}
		return names;
	}
	
	public void writeGrid() throws IOException {
		PrintWriter writer = new PrintWriter("previous.txt");
		writer.println(this.difficultyToString());
		writer.println(this.getRows());
		writer.println(this.getCols());
		for (int r = 0; r < this.getRows(); r++) {
			for (int c = 0; c < this.getCols(); c++) {
				writer.print(this.grid[r][c].toString());
			}
			writer.println();
		}
		writer.close();
	}
	
	public static char[][] readGrid() throws IOException {
		try {
			BufferedReader r = new BufferedReader(new FileReader("previous.txt"));
			String diff = r.readLine();
			Difficulty d = Difficulty.CUSTOM;
			if (diff.equals(Difficulty.BEGINNER.toString()))
				d = Difficulty.BEGINNER;
			if (diff.equals(Difficulty.INTERMEDIATE.toString()))
				d = Difficulty.INTERMEDIATE;
			if (diff.equals(Difficulty.EXPERT.toString()))
				d = Difficulty.EXPERT;
			
			String rows = r.readLine();
			int row = Integer.parseInt(rows);
			String cols = r.readLine();
			int col = Integer.parseInt(cols);
			
			char[][] arr = new char[row][col];
				
			boolean done = false;
			
			int currRow = 0;
			
			while(!done) {
				String str = r.readLine();
				if (str == null)
					done = true;
				else {
					for (int i = 0; i < str.length(); i++)
						arr[currRow][i] = str.charAt(i);
					currRow ++;
				}
			}
			return arr;
		} catch (NoSuchFileException e) {
			System.out.println("No such file");
		}
		System.out.println("Program should not reach here");
		return new char[0][0];
	}
	
//	public static void main(String[] args) {
//		Scanner scan = new Scanner(System.in);
//		Grid g = new Grid(Grid.Difficulty.BEGINNER, new JLabel("Game Not Started"), new JLabel(""), new JLabel(""));
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
