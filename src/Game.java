import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game implements Runnable {
	public void run() {
		// NOTE : recall that the 'final' keyword notes immutability
		// even for local variables.

		// Top-level frame in which game components live
		final JFrame frame = new JFrame("Minesweeper");
		frame.setLocation(Grid.scale * 8, Grid.scale * 8);

		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Game Not Started");
		status_panel.add(status);
		
		final JLabel minesRemaining = new JLabel("Mines left: 10");
		status_panel.add(minesRemaining);
		
		final JLabel timeStatus = new JLabel("Time: 0");
		status_panel.add(timeStatus);

		// Main playing area
		final Grid grid = new Grid(Grid.Difficulty.BEGINNER, status, minesRemaining, timeStatus);
		frame.add(grid, BorderLayout.CENTER);

		// Reset button
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.NORTH);

		

		// Note here that when we add an action listener to the reset
		// button, we define it as an anonymous inner class that is
		// an instance of ActionListener with its actionPerformed()
		// method overridden. When the button is pressed,
		// actionPerformed() will be called.
		final JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grid.reset();
			}
		});
		
		final JButton beginner = new JButton("Beginner");
		beginner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setSize(new Dimension((int)(Grid.scale * 8.5), (int)(Grid.scale * 10) + 22));
				grid.reset(Grid.Difficulty.BEGINNER);
			}
		});
		
		final JButton intermediate = new JButton("Intermediate");
		intermediate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setSize(new Dimension((int)(Grid.scale * 16.5), Grid.scale * 18 + 22));
				grid.reset(Grid.Difficulty.INTERMEDIATE);
			}
		});
		
		final JButton expert = new JButton("Expert");
		expert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setSize(new Dimension((int)(Grid.scale * 32.5), Grid.scale * 18 + 22));
				grid.reset(Grid.Difficulty.EXPERT);
			}
		});
		
		final JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grid.instructions();
			}
		});
		
		
		control_panel.add(reset);
		control_panel.add(beginner);
		control_panel.add(intermediate);
		control_panel.add(expert);
		control_panel.add(instructions);

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start game
		//grid.reset();
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
