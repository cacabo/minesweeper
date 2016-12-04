import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Box implements Comparable<Box> {

	  protected Grid grid;
	  private Position position;
	  public enum BoxState {HIDDEN, REVEALED, MARKED, UNSURE};
	  private BoxState state;
	  
	  public Box(Grid grid, Position position) {
		  this.grid = grid;
		  this.position = position;
		  this.state = BoxState.HIDDEN;
	  }
	  
	  public static Image createImage(String imgFile) {
			Image img = null;
			try {
				if (img == null) {
					img = ImageIO.read(new File(imgFile));
				}
			} catch (IOException e) {
				System.out.println("Internal Error:" + e.getMessage());
			}
			return img;
		}
	  
	  public abstract String toString();
	  
	  public abstract String toStringNoReveal();
	  
	  public abstract void leftClick();
	  
	  public void rightClick() {
		  if (this.grid.won() || this.grid.lost())
			  return;
		  if (this.state == BoxState.HIDDEN) {
			  this.state = BoxState.MARKED;
			  this.grid.incNumMarked();
			  this.grid.updateMinesRemaining();
		  }
		  else if (this.state == BoxState.MARKED) {
			  this.state = BoxState.UNSURE;
			  this.grid.decNumMarked();
			  this.grid.updateMinesRemaining();
		  }
		  else if (this.state == BoxState.UNSURE)
			  this.state = BoxState.HIDDEN;
	  }
	  
	  public Position getPosition() {
		  return position;
	  }
	  
	  public void reveal() {
		  this.state = BoxState.REVEALED;
	  }
	  
	  public boolean revealed() {
		  return this.state == BoxState.REVEALED;
	  }
	  
	  public boolean marked() {
		  return this.state == BoxState.MARKED;
	  }
	  
	  public boolean hidden() {
		  return this.state == BoxState.HIDDEN;
	  }
	  
	  public boolean unsure() {
		  return this.state == BoxState.UNSURE;
	  }
	  
	  public String boxState() {
		  if (this.state == BoxState.HIDDEN)
			  return "hidden";
		  else if (this.state == BoxState.MARKED)
			  return "marked";
		  else if (this.state == BoxState.UNSURE)
			  return "unsure";
		  else
			  return "hidden";
	  }
	  
	  public int compareTo(Num n) {
			return this.getPosition().compareTo(n.getPosition());
	  }
	  
	  abstract public void draw(Graphics g);
}
