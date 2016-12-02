=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Final Project README
PennKey: ccabo
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
* * * * M I N E S W E E P E R * * * *
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

==============
=: Overview :=
==============

Recreation of Microsoft Minesweeper

Features:
  -  Grid
     -  Filled with Boxes which are either Mines or Nums
     -  Different sized board options:
        1. Beginner: 8x8 grid, 10 bombs
        2. Intermediate: 16x16 grid, 40 bombs
        3. Expert: 16x32, 99 bombs
        4. Custom: height, width, num bombs
        -  Ensure num bombs < height * width - 9
           -  This ensure where the user clicks will be a Num with mineCount 0
     -  To begin, every box is hidden

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

Position:
  -  int x
  -  int y

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