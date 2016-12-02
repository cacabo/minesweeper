=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:
* * * * * * * * * * * * * * M I N E S W E E P E R * * * * * * * * * * * * * * *
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:

    CIS 120 Final Project
    By: Cameron Cabo
    PennKey: ccabo

=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:

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
           -  Ensure num mines < height * width - 9
              -  Makes sure where the user clicks will be a Num with mineCount 0
     -  High scores button: displays high scores for the different difficulties
        (top five)     
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
=:                           Initial Implementation                           :=
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
  -  private Position position;
  -  public enum BoxState {HIDDEN, REVEALED, MARKED, UNSURE};
  -  private BoxState state;
  -  Constructor takes in a grid and position
  -  public abstract String toString()
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