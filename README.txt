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

  4. Recursion:
     -  When a Num with mineCount 0 is clicked, the program uses recursion
        to iterate through adjacent Boxes and reveal all Nums surrounding the
        Num with mineCount 0. If any of these Boxes are Nums with mineCount 0,
        then they too reveal all surrounding Boxes.
     -  This is specifically handled by recursively by first the leftClick(r, c)
        method in the Grid class, which relays this to leftClick() in the Box
        class, which relays this click to the Num class. If the num has a
        mineCount of 0, then it calls the cascade method in the Grid class at
        its position, which in turn will call the Grid's leftClick method on
        all surrounding boxes.
     -  In total, this is a recursive loop which reveals all desired boxes.
        This happens upon generation of the board and can happen with any other
        left click during the game.

================================================================================
=:                                  Overview                                  :=
================================================================================

Back-end Features:

  -  Grid
     -  Filled with Boxes which are either Mines or Nums
     -  Different difficulties correspond to different board sizes:
        1. Beginner: 8x8 grid, 10 bombs
        2. Intermediate: 16x16 grid, 40 bombs
        3. Expert: 16x32, 99 bombs
        4. Custom: height, width, num bombs
           -  Can be between 4 and 48 columns/rows wide
  -  To begin, every box is hidden and null
  -  If a null box is clicked, the board is generated by randomly placing mines
     anywhere except where the user clicked and immediately around there.
  -  When left clicked, a box is revealed
     -  When a mine is revealed, the user loses
     -  When a Num with mineCount 0 is revealed so too are all surrounding Boxes
        -  This causes a cascade effect if there are adjacent 0's
  -  When right clicked, if a box is not revealed, it toggles from hidden to
     marked to unsure
  -  When a revealed box is right clicked, nothing happens
  -  When a revealed box is double left clicked it reveals all adjacent unmarked
     boxes if the number of adjacent marked boxes equals the number of bombs
     it is touching. If this equality is not the case, then the box flashes a
     red x.
  -  A user wins when all Boxes which are instances of Num are revealed. This is
     equivalent to when the number of revealed Nums equals the total number of
     boxes minus the number of mines.

GUI Features:
  -  Top toolbar: includes option for "New Game", "Beginner", "Intermediate",
     "Expert", and "Custom". The current difficulty is darkened.
     -  Clicking "New Game" creates a new game of the current difficulty.
     -  Clicking any of the difficulty buttons creates a new game of the
        specified difficulty, even if the user is not changing the difficulty.
     -  Upon clicking "Custom", a pane comes up asking the user to enter the
        number of rows, columns, and mines which they desire. The number of
        rows and columns must each be between 4 and 48. The number of mines
        must be less than or equal to the number of rows entered multiuplied
        by the number of mines entered minus nine. If this is not the case,
        a message notifies the user of the input error, and the difficulty is
        not changed.
  -  Below the top toolbar is the grid itself.
     -  By default, the grid is set to difficulty "Beginner"
  -  Below the grid is a status toolbar.
     -  Game status is either "Game Not Started", "Game in Progress", "Game
        Lost", or "Game Won". This changes as the user progresses through the
        game.
     -  Mines remaining tells the user how mnay mines there are on the board
        minus the number of mines that the user has marked.
     -  Time tells the user how many seconds have passed since the user first
        clicked the board.
  -  Below the status toolbar is the information toolbar
     -  The highscores button opens a frame which lists the highscores and
        corresponding user name for the top five highscores for each difficulty.
        If there are not yet five high scores, the remaining are filled in with
        dashes.
     -  The instructions button opens a frame which discusses the objectives of
        the game and how to play it.
     -  Both frames which pop up have a button at the bottom which allows the
        user to close the frame and return to playing.

================================================================================
=:                               Implementation                               :=
================================================================================
        
====================
=: Position Class :=
====================

Overview:

  -  Makes it easier to get information about Boxes at different positions
     within the Grid 2D array. Also is used within various collections to
     generate the field of Mines and to determine which boxes should be revealed
     upon clicking a Num which touches zero mines.

Implementation:

  -  Implements Comparable<Position>
  -  private final int row
  -  private final int col
  -  Constructor takes in two ints
  -  Getters for row, col
  -  compareTo, hashCode, equals, toString methods all with standard
     implementations

===============
=: Box Class :=
===============

Overview:

  -  Abstract implementation of a Box which has a position within the grid.
  -  A box can be either a Num or a Mine

Implementation:

  -  Implements Comparable
  -  protected final Grid grid
     -  Makes it directly accessible by child classes
     -  Public helper function that returns the grid would void encapsulation
  -  private Position position;
  -  private BoxState state is either{HIDDEN, REVEALED, MARKED, UNSURE, CHECKED}
  -  Constructor takes in a grid and position
  -  public static Image createImage(String imgFile)
     -  Helper method for creating images
  -  public abstract String toString()
     -  Mines and Nums will have different implementations
  -  public abstract void leftClick()
     -  Implemented differently by Nums and Mines
  -  public abstract void doubleLeftClick()
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
  -  Getter for position, box state, and booleans checking which state is used.
  -  public int compareTo(Num n) compares nums by Position
  -  abstract public void draw is implemented by subclasses
  -  public void paint() draws the Box based on the grid's graphics

================
=: Mine Class :=
================

Overview:

  -  The Mine class represents a mine in the grid. When a mine is revealed, it
     explodes and the user has lost the game. The objective is to mark all Mine
     Boxes and reveal all of the Num Boxes.

Implementation:

  -  Extends Box, Implmenets Comparble
  -  Constructor takes in a grid and position, calls the super position
     -  Calls super constructor
  -  public void leftClick()
     -  Does nothing if the game is finished
     -  Reveals the box
     -  Calls lose on the grid
     -  Updates the status of the grid and paints the Mine
  -  public void doubleLeftClick()
     -  Calls the leftClick() method because it operates the same way
  -  public String toString returns "M"
  -  compareTo() compares based on position
  -  public void draw(Graphics g)
     -  Draws an image dependent on the state and the size is dependent on how
        many rows and columns there are in the grid.  
  
===============
=: Num Class :=
===============

Overview:

  -  The Num class represents a number in the grid. When a number is revealed,
     it displays a number representing the number of mines that it is touching.
     After being revealed, if the num is double left clicked and the number of
     marked boxes it is touching equals the number of mines it is touching, then
     all adjacent, unmarked boxes are revealed. Otherwise, a red x is flashed to
     signify that there are not enough or too many adjacent marked boxes.

Implementation:

  -  Extends Box, implements Comparable
  -  private final int numMines
     -  Number of adjacent mines
     -  This is final because it cannot change throughout the game
  -  Constructor takes in a grid and position
     -  Calls super constructor
     -  Sets numMines to grid.mineCount(position)
        -  Calculated by the grid class by counting number of mines in
           adjacent boxes based on the positions surroundings
  -  Another constructor takes in a grid, position, and number
     -  Specifically used to replay a game after losing
     -  The board is preset, so there is no need to recalculate the number of
        mines around the Num.
  -  public void leftClick()
     -  Does nothing if the game is finished
     -  Reveal the Box if it is not already revealed
        -  Call cascade at that position (class within grid) if numMines is 0
        -  Increment the number of revealed boxes in the grid
        -  Check if the user hasWon() in the grid class
        -  Update the grid status and paint
  -  public void doubleLeftClick()
     -  Does nothing if the game is finished
     -  Does nothing if the box is not revealed
     -  If the box is revealed, checks if the number of mines the box should be
        touching equals the number of marked boxes surrounding the box. If this
        is the case, the cascade method is called, revealing all adjacent,
        unmarked boxes. If this is not the case, a red x is flashed for 1/8th of
        a second.
  -  Getter for num mines
  -  public String toString() returns numMines as a string
  -  Draw draws a different image depending on the state of the Num

================
=: Grid Class :=
================

Overview:

  -  The Grid class contains a 2D array of Boxes. It handles all of the JLabels
     which tell the user the status of the game. It also handles making noises
     and dispatching clicks to subcomponents of the 2D array. If the user wins,
     the grid is painted a shade of green. If the user loses, the grid is
     painted a shade of red. 

Implementation:

  -  Suppress serial warnings, extend JPanel
  -  2D array of Box elements make up the grid
  -  private int numMines: count of number of mines in the grid
  -  private int numMarked: count of number of marked mines in the grid
     -  Originally 0
  -  private int numRevealed: count of revealed nums in the grid
     -  Originally 0
  -  private int time count of duration of the current game in seconds
     -  Originally 0
  -  private String name: user's name
     -  Originally set to "user" but can be updated if the user gets a highscore
  -  private GameStatus gamestatus is one of {NOT_STARTED, IN_PROGRESS, LOST,
     WON}; Begins as NOT_STARTED
  -  private JLabel status shows the gameStatus to the user
     -  Passed in from the Game class
  -  private JLabel minesRemaing shows the number of mines remaining to the user
  -  private JLabel timeStatus shows the time duration to the user
  -  private Difficulty difficulty is one of {BEGINNER, INTERMEDIATE, EXPERT,
     CUSTOM};
  -  private Timer timer increments the time every second upon being started

  -  private void jLabelHelper(JLabel status, JLabel minesRemaining,
     JLabel timeStatus)
     -  Sets general variables to their starting state, particularly JLabels
  -  private void beepHelper(String s) helps in playing a beep sound based on
     file name
  -  public int scale() sets width and height of each Box in the grid
     depending on the size of the grid
  -  private void beep() plays the first beep noise
  -  private void beep2() plays the second beep noise
  -  public void mouseListenerHelper() sets up grid mouseListener
  
  -  public Grid(int rows, int columns, int numMines, JLabel status, JLabel
     minesRemaining, JLabel timeStatus)
     -  Constructor for custom difficulty
  -  public Grid(Difficulty d, JLabel status, JLabel minesRemaining, JLabel
     timeStatus)
     - Constructor for other difficulties
  -  private void resetHelper(): helper method for standardizing a grid when
     resetting, sets values back to their starting states.
  -  public void reset(): general method to reset the grid, creating a new,
     empty one, ready to play again
  -  public void reset(Difficulty d): reset method to a new difficulty
  -  public void reset(int row, int col, int mines): reset method for a custom
     difficulty
  -  public void replay() throws IOException: adapted reset method to replay a
     copy of the past game (same mines and number locations)
    
  -  public void incNumRevealed(): increment the number of revealed numbers
  -  public void incNumMarked(): increment the number of marked boxes
  -  public void decNumMarked()
  -  public void incTime(): increments the time of the game, capping it at 999
  -  Getter for numRevealed, numMarked, difficulty, name, numMines, rows, cols,
     time, whether the user has won, whether the user has lost, whether the
     game is started, whether the game is in progress, private getter method for
     the Box at a passed in position
  -  Setter for name
  -  gameStatusToString() returns game status with first letter capitalized
  -  difficultyToString() does the same but for difficulty
  -  public void updateStatus(): update the status JLabel's text
  -  public void updateMinesRemaining(): update the text of mines left JLabel
  -  public void updateTime(): update the text of the time left JLabel
  -  private Set<Position> getSurroundings(Position p)
     -  Returns a set of the positions of all boxes within the grid adjacent to
        the passed in position
     -  Ensures that these positions are within the array
  -  public int mineCount(Position pos): returns the number of mines adjacent to
     the passed in position
  -  public int markedCount(Position pos): returns the number of marked boxes
     adjacent to the passed in position
  -  public boolean hasWon(): returns if the user has won determined by if the
     number of revealed nums is equal to the number of nums in total
     
  -  public void leftClick(int row, int col): handles a left click in the grid
     by finding the position the click was in and passing it on to that box
  -  public void doubleLeftClick(int row, int col): handles a double left click
     in the grid by distributing it to the position it was at.
  -  public void rightClick(int row, int col): handles a right click in the grid
     -  Does nothing if the grid is not generated
     -  Otherwise distributes teh click to the corresponding Box
  -  public void generate(int row, int col): generates the grid with random mine
     distribution with the limitation that a mine cannot be placed immediately
     next to where the user clicked. 
  -  public void cascade(Position pos): reveals all hidden boxes adjacent to the
     passed in position
  -  public void lose: handles the user losing a game, stops the timer, makes a
     boom noise, writes the grid to the previous.txt file, and repaints. Also
     creates a frame which permits the user to replay the game, play a new game,
     or quit. 

  -  public void win(): handles the user wining a game, outputs a tada sound,
     creates a pane asking the user for a highscore if the user's time is a
     highscore for the difficulty.
     
  -  public void paintComponent(Graphics g) paints each individual box. Paints
     a green tinge if the game is won & a red tinge if the game is lost.
  -  public Dimension getPreferredSize() returns the size of the grid as
     determined by the scale and the number of rows and columns. 
     
  -  public void writeGrid() throws IOException: writes the current grid to the
     previous.txt file
  -  public static char[][] readGrid() throws IOException: reads the grid from
     previous.txt into a 2D char array
     
  -  private String scoreToString(): returns the game status formatted to
     highScore output
  -  public static List<String> highScoresToStrings(Difficulty d) throws
     IOException: returns a list of highscores formated as: name,score
  -  public static List<Integer> highScoresToInts(List<String> strings) throws
     IOException: returns a list of highscores with just the scores from a
     string list
  -  private static List<Integer> highScoresToInts(Difficulty d) throws
     IOException: helper method that returns a list of highscores with just the
     scores from a difficulty
  -  public static List<String> highScoresToNames(List<String> strings) throws
     IOException: returns a list of highscores with just the names from a string
     list
  -  public boolean isHighScore() throws IOException: returns if a user's score
     is a high score
  -  private List<String> newHighScoresToString() throws IOException: helper
     method which returns a list of highscores updated with that of the user
  -  public void writeHighScores() throws IOException: writes the highscores to
     the file corresponding to the current difficulty

================
=: Game Class :=
================

Overview:

  -  Main class that specifies the Grid and widgets of the game. Specifically
     makes a button bar along the top to adjust difficulty, paints the grid in
     the middle with a padding, and creates a status toolbar along the bottom
     paried with an isntructions button and a highscores button which each
     create their own frames.
     
Implementation:

  -  public void run()
     -  Creates a toplevel frame called "Minesweeper"
     -  Wrapper panel bottom for the status and information
        -  Status panel to contain game status, mines remaining, and time
           duration
        -  Highscores and Instructions each trigger frams popping up with
           JLabels to give information and a JButton at the bottom to exit that
           frame.
     -  Wrapper panel for grid
     -  Wrapper control_panel for buttons to set difficulty and game
        -  Buttons for: new game & each difficulty
           -  Custom button asks user for row, column, & mine number input
  -  public static void main(String[] args): main method to make the game run:
     initializes GUI elements specified in Game and runs it

================================================================================
=:                                 Adjustment                                 :=
================================================================================

In order to update the status of JPanels, they had to be passed into the grid
constructor so that they grid could alias their states. As such, I had to adapt
my constructors as I added more JPanels to the GUI.

Creating the high scores panel ended up being far more of an endeavor than I
anticipated. I decided to record a username and time. For the username, rather
than asking for it at the start of every game, I instead ask for it when the
user gets a highscore the first time. This limits the number of times a popup
pane must come up. This was a pivot from my original plan to not record
usernames at all.

I added double clicks in order to make the game a little more playable. I had
always wanted to flash a red x when the user clicks on a revealed box that is
not touching as many marked boxes as it does mines, however when I first
implemented this, it made it so that the red x flashed upon board generation
and other cascades, because the red x was flashed through the leftClick()
method. To resolve this, I implemented a doubleLeftClick() method to handle
these double clicks, and removed all single left click functionality upon
clicking a revealed box. This made it so that the red box only flashes when
triggered by a user's double click.

I also decided to add sound to left and right clicks and winning and losing
the game. This did not require changing the structure of any of my classes,
and only required adding a few lines of code.

Another thing which I considered is to draw each Box as a JButton with text
within it rather than an image. If I could have succeeded in making this
appear as I did with the images, then this would have been a more efficient 
and more effective means of painting the grid.

================================================================================
=:                                 Reflection                                 :=
================================================================================

I feel like my implementation is efficient and effective: I distribute methods
well and use the 2D array when it makes sense, but also use collections when
using those better makes sense. I think I could have better isolated my GUI
fuctionality, because it is in large part distributed throughout both the Game
and Grid classes. I think that when I better understand Java Swing. Even still,
I think I was organized in my implementation, particularly the distribution of
net functionality from Game --> Grid --> Position --> Box --> Num, Mine.

I think I chose to repaint in a way that was relatively efficient. Initially,
with every click I repainted the whole board, but I then shifted to only repaint
the Boxes which are affected by every click. This makes the Grid load box by box
which can be a bit distracting and is not as clean, however it is quicker. In
the future perhaps I should consider using a tick interval to cover the
repainting while it is being done, if this is possible. Again, this would
require more reaserch into and understanding of Swing.

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
