=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:
*   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *
  *   *   *   *   *   *   * M I N E S W E E P E R *   *   *   *   *   *   *   *
*   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:

    CIS 120 Final Project
    By: Cameron Cabo
    PennKey: ccabo

=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:

================================================================================
=:                               Major Concepts                               :=
================================================================================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays:
     -  The Grid used to display the game is a 2D array of instances of the
        abstract class Box.
          -  This is an appropriate use of 2D arrays because the board itself
             will be drawn in a grid of images, each corresponding to a box.
             There are different images depending on the state of the box.
          -  Display changes based on whether a box is revealed (left click,
             irreversible) , marked, unmarked, or unsure (states which the user
             toggles through right clicks). The positions of these Boxes will be
             final as they should not change throughout the course of the game.
     -  There are be four difficulties: easy, intermediate, expert, and custom
        which each correspond to a different grid sizes size (custom can be any
        size from 4 to 48 rows/columns).
        -  This is an appropriate use of 2D arrays because the grid does not
           have to change size while a game is in progress, but rather only when
           a new game is started. Further, arrays are very easy to instantiate
           and are efficient.
     -  Originally, each index in the array null (which works well as a 2D array
        begins filled with null objects upon instantiation).
        
  2. I/O:
     -  Input output is used in four different components of the game:
     1. Read in image files and display them in the grid
     2. Read in and play different audio files for left and right clicks within
        the grid and also for when the game is won or lost.
     3. Upon losing a game, the board is written out to a file called
        previous.txt. The user is then given the option to replay the game, in
        which case the file is read back in and the game is re-instantiated.
          -  This is a unique case within the game because normally a null array
             of Boxes is created. A unique constructor is used to create an
             array already filled with Mines and Nums.
     4. Upon winning a game, if the time in which the user completed the game is
        among the five quickest times for a given difficulty, the user is
        prompted to enter a name. Then, the name and score are written into a
        file unique to the difficulty along with the four other top scores from
        other games. This file is read in when the user clicks the "highscores"
        button on the bottom right of the GUI which lists the five highscores
        for each difficulty.

  3. Inheritance/Subtyping for Dynamic Dispatch:
     -  As mentioned, the Grid will be filled with abstract Boxes which will be
        either Nums or Mines.
     -  Boxes have a Position, Grid, and enum state (revealed, marked, unmarked,
        unsure, or checked). They will have setter and getter methods for their
        states and also will handle right clicks, left clicks, and double left
        clicks. Implementation of right click will be the same for both Mines
        and Nums, although each will have a different implementation of single
        and double left clicks.
     -  This is an appropriate use of dynamic dispatch because both Nums and
        Mines can have any of these states, but will handle them differently.
          -  For example, a user loses if a Mine is revealed, but gets closer to
             winning if a Num is revealed.
          -  Secondly, although it would be a misstep to mark a Num, a user can
             mark Nums or Mines alike.
          -  The Num class will have an int mineCount which is a record of how
             many mines are adjacent to it. This is a feature the Mine class
             will not have.
          -  Generally, both Nums and Mines will implement the same methods and
             some implementations overlap, yet so too will some implementations
             be slightly different. Thus, it makes sense for them to be
             connected by an overarching abstract class (Box) which cannot be
             instantiated itself.
     -  Having Num and Mine implement the same abstract class allows them to be
        stored in the grid array together as Boxes.

  4. Collections:
     -  Collections are used for four components of the game:
     1. To create and return a set of surrounding positions to a given position
          -  A Set is made of all positions adjacent to and containing the 
             position. Using a Set is appropriate because it can be easily
             resized, there are no duplicates, and order does not matter.
          -  This is also appropriate because it is easy to remove and add items
             to the Set solely by Positions, so using a Set makes it easy
             to manipulate the set of surroundings based off of the set of Boxes
             at these positions in order to manipulate the grid as needed.
     2. To create and use a set of surrounding positions to cascade to given
        a position.
          -  This builds off of the first use of collections: a new set to
             cascade to (reveal) is created from the set of surrounding
             positions of only Boxes that are not null and are hidden. A new Set
             is used while the original set is iterated through to avoid a
             concurrent modification exception.
     3. To efficiently randomly place mines upon grid generation
          -  This use of collections also builds off of the first use: to
             decide where a Mine can be placed, an ArrayList of Positions of the
             whole grid is made excepting those positions included in the
             surroundings of where the user clicks.
          -  A random index of the List is then chosen and a new Bomb is placed
             at that index in the Grid array. The Position at that index in
             the List is then removed. This process repeats until the number of
             mines placed is equal to the number of mines corresponding to the
             current game's difficulty.
               -  Using a list is appropriate here because it is easy to resize
                  but also can be accessed by index.
               -  This is a more efficient implementation than just using the
                  pre-existing array because the program would always have to
                  check if the randomly chosen position contains a mine or is
                  within the surroundings of where the user clicked. This would
                  be particularly inefficient in the case of a custom
                  difficulty with a high quantity of mines.
     4. To read high scores and efficiently manipulate them
          -  A LinkedList is used to read in highscores from each highscore
             file. On the most basic level, each line in the file is added to
             the List, and this can be further manipulated to isolate just
             usernames or just scores.
          -  Using a LinkedList is appropriate because maintaining the order of
             entries is essential. Secondly, it is easy to iterate through,
             making writing out quick. Lastly, it can be resized to add new
             highscores and remove ones which are replaced.

================================================================================
=:                                  Overview                                  :=
================================================================================

Back-end Features:

  -  Grid
     -  Filled with Boxes which are either Mines or Nums
     -  Different sized board options:
        1. Beginner: 8x8 grid, 10 bombs
        2. Intermediate: 16x16 grid, 40 bombs
        3. Expert: 16x32, 99 bombs
        4. Custom: height, width, num bombs
  -  To begin, every box is hidden
  -  When left clicked, a box is revealed
     -  When a mine is revealed, the user loses
     -  When a Num with mineCount 0 is revealed so too are all surrounding Boxes
        -  This can cause a cascade effect if there are adjacent 0's
  -  When right clicked, if a box is not revealed, it toggles from hidden to
     marked to unsure
  -  When a revealed box is left clicked it reveals all adjacent unmarked
     boxes if the number of adjacent marked boxes equals the number of bombs
     it is touching.
  -  A user wins when all Boxes which are instances of Num are revealed

User Interface Features:

  -  Open presenting a grid
     -  First time: display beginner
     -  Subsequent time: display grid of previous difficulty setting
  -  At top:
     -  New game button
        -  Opens dropdown menu to choose difficulty
        -  If "CUSTOM" is chosen, give options to enter rows, columns, and
           number of mines
           -  Ensure num mines < height * width - 9, unless there are less than
              9 positions, in which case there can be no bombs.
     -  Makes sure where the user clicks will be a Num with mineCount 0
     -  High scores button: displays high scores for the different difficulties
        (top five)    
     -  Instructions button
        -  Will reveal game controls, objective 
     -  Smiley face when game in progress, frowny face when game over, cool face
        when game is won
     -  Timer display
        -  Starts at 0
        -  Timer begins when the grid is generated (with first click)
  -  When a user loses they should be prompted to start a new game or replay
     the one they just played
     -  If the user got a high score, they should be prompted to enter a
        username

================================================================================
=:                       Initial Backend Implementation                       :=
================================================================================
        
====================
=: Position Class :=
====================

Overview:

  -  Makes it easier to get information about Boxes at different positions
     within the Grid 2D array

Implementation:

  -  Implements Comparable<Position>
  -  private final int row
  -  private final int col
  -  Constructor takes in two ints
  -  Getters for row, col
  -  compareTo, hashCode, equals, toString methods all with standard
     implementations
  
=================
=: Timer Class :=
=================

Overview:

  -  Keep track of time once the grid is generated

Implementation: 

  -  private int time
  -  No arguments constructor
  -  Getter for time
  -  Increment method to increase time by one second

===============
=: Box Class :=
===============

Overview:

  -  Abstract implementation of a Box which has a position within the grid.
  -  A box will be either a Num or a Mine

Implementation:

  -  protected Grid grid
     -  Makes it directly accessible by child classes
     -  Public helper function that returns the grid would void encapsulation
  -  private Position position;
  -  public enum BoxState {HIDDEN, REVEALED, MARKED, UNSURE};
  -  private BoxState state;
  -  Constructor takes in a grid and position
  -  public abstract String toString()
     -  Used for debugging
     -  Mines and Nums will have different implementations / outputs
  -  public abstract void leftClick()
  -  public void rightClick()
     -  Toggles from HIDDEN to MARKED to UNSURE
     -  If the state becomes marked, increment grid's numMarked
     -  If the state becomes unsure, decrement grid's numMarked
     -  Getter for position
  -  public void reveal
     -  Sets the box's state to revealed
  -  public booleans for revealed, marked, hidden, unsure return if each is the
     state of the box, respectively
  -  public String boxState returns the state as a String

================
=: Mine Class :=
================

Overview:

  -  The Mine class represents a mine in the grid. When a mine is revealed, it
     explodes and the user has lost the game. The objective is to mark all Mine
     Boxes and reveal all of the Num Boxes.

Implementation:

  -  Constructor takes in a grid and position
     -  Calls super constructor
  -  public void leftClick()
     -  Reveals the box
     -  Calls lose on the grid
  -  public String toString returns "M"
  
===============
=: Num Class :=
===============

Overview:

  -  The Num class represents a number in the grid. When a number is revealed,
     it displays its associated number representing the number of mines that it
     is touching. After being revealed, if the num is left clicked and the
     number of marked boxes it is touching equals the number of mines it is
     touching, then all adjacent, unmarked boxes are revealed. Otherwise, an x
     is flashed to signify that there are not enough marked boxes adjacent.

Implementation:

  -  private int numMines;
  -  Constructor takes in a grid and position
     -  Calls super constructor
     -  Sets numMines to grid.mineCount(position)
        -  Calculated by the grid class by counting number of mines in
           adjacent boxes
  -  public void leftClick()
     -  Reveal the Box if it is not already revealed
        -  Call cascade at that position (class within grid) if numMines is 0
     -  If the box is revealed, check if the number of mines equals the number
        of marked mines adjacent to the box. If this is true, then call cascade
        at that position
  -  Getter for numBombs
  -  public String toString() returns numMines as a string

================
=: Grid Class :=
================

Overview:



Implementation:





public class Grid {
    private Box[][] grid;
    private int numMines;
    private int numMarked;
    public enum GameStatus {UNSTARTED, IN_PROGRESS, LOST, WON};
    private GameStatus gameStatus;
    public enum Difficulty {EASY, INTERMEDIATE, EXPERT, CUSTOM};
    private Difficulty difficulty;
    // Figure out how to implement the timer,
    // timer should be started * * * upon generation * * *

    public Grid(int rows, int columns, int numMines) {
        this.grid = new Box[rows][columns];
        this.numMines = numMines;
        this.numMarked = 0;
        this.gameStatus = GameStatus.UNSTARTED;
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
        this.gameStatus = GameStatus.UNSTARTED;
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
        this.gameStatus = GameStatus.IN_PROGRESS;
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
        this.gameStatus = GameStatus.LOST;
    }
    
    public boolean hasWon() {
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++) {
                Box b = this.grid[r][c];
                if (b instanceof Num && !b.revealed())
                    return false;
            }
        this.gameStatus = GameStatus.WON;
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
        return this.gameStatus == GameStatus.WON;
    }
    
    public boolean lost() {
        return this.gameStatus == GameStatus.LOST;
    }
    
    public boolean unstarted() {
        return this.gameStatus == GameStatus.UNSTARTED;
    }
    
    public boolean inProgress() {
        return this.gameStatus == GameStatus.IN_PROGRESS;
    }
    
    public String gameStatusToString() {
        if (this.gameStatus == GameStatus.UNSTARTED)
            return "Unstarted";
        if (this.gameStatus == GameStatus.IN_PROGRESS)
            return "In Progress";
        if (this.gameStatus == GameStatus.WON)
            return "Won";
        else
            return "Lost";
    }
    
    public String difficultyToString() {
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
        if (this.lost())
            output += "Game lost";
        else if (this.won() || this.hasWon())
            output += "Game won";
        else
            output += "\nGame still in progress";
        return output;
    }
    
    public static void main(String[] args) {
        Grid g = new Grid(Grid.Difficulty.INTERMEDIATE);
        System.out.println("Grid width: " + g.grid.length);
        System.out.println("Grid height: " + g.grid[0].length + "\n");
        System.out.println("Click at (2,2):\n");
        g.generate(7, 7);
        System.out.println(g);
    }
}







/ / / / Classes:

Enum boards:
  1. Beginner: 8x8 grid, 10 bombs
  2. Intermediate: 16x16 grid, 40 bombs
  3. Expert: 16x32, 99 bombs
  4. Custom: height, width, num bombs

Timer:
  -  Int time-elapsed

Board:
  -  2D array of Boxes
  -  Num-bombs
  -  Num-marked
  -  Boolean won
  -  Boolean lost
  -  Takes in height and width
  -  Starting position
       -  Throws exception if out of bounds
  -  When generating: starting position must be blank
  -  Int bombCount(Position pos)
       -  Returns number of adjacent bombs
  -  Int markedCount(Position pos)
       -  Returns number of adjacent marked boxes

Box (Abstract):
  -  Position position
  -  Boolean revealed
  -  Abstract void right-click
  -  void left-click
       toggle marked, unmarked
  -  Boolean marked (right click to mark)

Num extends Box:
  -  Int count
  -  Boolean safe
       -  Check if count == marked boxes it is adjacent to
       -  If all bombs it touches are revealed
       -  clicking it if safe will reveal all remaining adjacent boxes
  -  Can be marked even though they shouldn't be -- still count as marked bombs
  -  If revealed and clicked will check if it is safe
       -  Reveals other squares if it is safe
       -  Flashes an "x" if not safe

Mine extends Box:
  -  Game ends if revealed (right clicked)
       -  public void lose(Board b)
       
================================================================================
=:                                 Adjustment                                 :=
================================================================================

high scores panel
double clicks


================================================================================
=:                             External Resources                             :=
================================================================================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  Help with Implementation:
  
      http://stackoverflow.com/questions/22653592/how-to-give-spaces-between-jlabel
      http://stackoverflow.com/questions/685521/multiline-text-in-jlabel
      http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
      http://stackoverflow.com/questions/15526255/best-way-to-get-sound-on-button-press-for-a-java-calculator
  
  Sound Files:
  
      https://www.freesoundeffects.com/free-sounds/explosion-10070/
      http://soundbible.com/1003-Ta-Da.html
      http://soundbible.com/1133-Beep-Ping.html
      http://soundbible.com/819-Checkout-Scanner-Beep.html
