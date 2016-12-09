import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class Game implements Runnable {
	
	private static void toggleDifficultyButtons(JButton a, JButton b, JButton c, JButton d) {
		a.setBackground(Color.LIGHT_GRAY);
		b.setBackground(null);
		c.setBackground(null);
		d.setBackground(null);
	}

	public void run() {
		// NOTE : recall that the 'final' keyword notes immutability
		// even for local variables.

		// Top-level frame in which game components live
		final JFrame frame = new JFrame("Minesweeper");
		frame.setLocation(Grid.scale * 8, Grid.scale * 8);

		EmptyBorder empty = new EmptyBorder(0, 0, 0, 0);
		EmptyBorder paddingWide = new EmptyBorder(5, 20, 5, 20);
		EmptyBorder padding = new EmptyBorder(18, 18, 18, 18);
		
		// Status panel
		
		final JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		
		final JPanel status_panel = new JPanel();
		
		final JLabel status = new JLabel("Game Not Started");
		status.setBorder(paddingWide);
		status.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		status_panel.add(status);
		
		final JLabel minesRemaining = new JLabel("Mines left: 10");
		minesRemaining.setBorder(paddingWide);
		minesRemaining.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		status_panel.add(minesRemaining);
		
		final JLabel timeStatus = new JLabel("Time: 0");
		timeStatus.setBorder(paddingWide);
		timeStatus.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		status_panel.add(timeStatus);

		// Main playing area
		final Grid grid = new Grid(Grid.Difficulty.BEGINNER, status, minesRemaining, timeStatus);
		grid.setBorder(empty);
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
		reset.setFont(new Font("Sans Serif", Font.PLAIN, 16));
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grid.reset();
			}
		});
		
		final int addH = 90;
		
		final JButton beginner = new JButton("Beginner");
		beginner.setFont(new Font("Sans Serif", Font.PLAIN, 16));
		
		
		final JButton intermediate = new JButton("Intermediate");
		intermediate.setFont(new Font("Sans Serif", Font.PLAIN, 16));
		
		final JButton expert = new JButton("Expert");
		expert.setFont(new Font("Sans Serif", Font.PLAIN, 16));
		
		final JButton custom = new JButton("Custom");
		custom.setFont(new Font("Sans Serif", Font.PLAIN, 16));
		
		beginner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setSize(new Dimension((int)(Grid.scale * 8.5), (int)(Grid.scale * 10) + addH));
				grid.reset(Grid.Difficulty.BEGINNER);
				toggleDifficultyButtons(beginner, intermediate, expert, custom);
			}
		});
		
		intermediate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setSize(new Dimension((int)(Grid.scale * 16.5), Grid.scale * 18 + addH));
				grid.reset(Grid.Difficulty.INTERMEDIATE);
				toggleDifficultyButtons(intermediate, beginner, expert, custom);
			}
		});
		
		expert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setSize(new Dimension((int)(Grid.scale * 32.5), Grid.scale * 18 + addH));
				grid.reset(Grid.Difficulty.EXPERT);
				toggleDifficultyButtons(expert, beginner, intermediate, custom);
			}
		});
		
		custom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLabel r1 = new JLabel("Number of rows:  ");
				r1.setFont(new Font("Sans Serif", Font.PLAIN, 18));
				JLabel c1 = new JLabel("Number of columns:  ");
				c1.setFont(new Font("Sans Serif", Font.PLAIN, 18));
				JLabel m1 = new JLabel("Number of mines:  ");
				m1.setFont(new Font("Sans Serif", Font.PLAIN, 18));
						
				JTextField r2 = new JTextField(2);
				JTextField c2 = new JTextField(2);
				JTextField m2 = new JTextField(2);
				
				JPanel rs = new JPanel();
				rs.add(r1);
				rs.add(r2);
				
				JPanel cs = new JPanel();
				cs.add(c1);
				cs.add(c2);
				
				JPanel ms = new JPanel();
				ms.add(m1);
				ms.add(m2);
				
				JPanel customDif = new JPanel();
				customDif.setLayout(new BoxLayout(customDif, BoxLayout.Y_AXIS));
				
				customDif.add(rs);
				customDif.add(cs);
				customDif.add(ms);
				
				int result = JOptionPane.showConfirmDialog(
						frame,
						customDif,
						"Custom Difficulty",
						JOptionPane.OK_CANCEL_OPTION
						);
				if (result == JOptionPane.OK_OPTION) {
					try {
						int row = Integer.parseInt(r2.getText());
						int col = Integer.parseInt(c2.getText());
						int mines = Integer.parseInt(m2.getText());
						if (row < 4 || row > 48 || col < 4 || col > 48 || mines < 0 || mines > row * col - 9)
							throw new IllegalArgumentException();
						frame.setSize(new Dimension((int)(Grid.scale * (row + .5)), Grid.scale * (col + 2) + addH));
						grid.reset(row, col, mines);
						toggleDifficultyButtons(custom, beginner, intermediate, expert);
					} catch(Exception error) {
						JOptionPane.showMessageDialog(frame,
							    "Input Exception.",
							    "Inane error",
							    JOptionPane.ERROR_MESSAGE
							    );
					}
				}
				else
					return;
			}
		});
		
		toggleDifficultyButtons(beginner, intermediate, expert, custom);
		
		final JButton highscores = new JButton("Highscores");
		highscores.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		
		
		final JButton instructions = new JButton("Instructions");
		instructions.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame instr = new JFrame("Instructions");
				instr.setLocation(frame.getMousePosition());
				
				final JLabel text = new JLabel(
					"<html>"
						+ "<b>M I N E S W E E P E R</b><br><br>"
						+ "Built by Cameron Cabo for his CIS 120 final project. Enjoy!<br><br>"
						+ "<b>Overview:</b><br>"
						+ "The objective of the game is to reveal all boxes which do not contain mines. The<br>"
						+ "number within a box is equal to the number of mines it is touching. Do this as<br>"
						+ "quickly as possible to make it on the leaderboard!<br><br>"
						+ "<b>Instructions:</b><br>"
						+ "<ol>"
							+ "<li>Left click anywhere on the board to start the game.</li>"
							+ "<li>From there, left click any unrevealed box to see what's below!</li>"
							+ "<li>Right click unrevealed boxes to toggle marked, unsure, and unmarked states.</li>"
							+ "<li>If you left click a box already touching as many marked mines as the box<br> "
							+ "should be touching, then all adjacent unmarked boxes will be revealed.</li>"
						+ "</ol><br><br>"
						+ "<b>Additional Settings:</b><br>"
						+ "<ul>"
							+ "<li>Choose your difficulty from the buttons on the top. A custom difficulty will<br>"
							+ "prompt you to enter the number of rows, columns, and mines of your choosing.</li>"
							+ "<li>On the bottom you will see the game status, how many mines are left, and the<br>"
							+ "duration of the game!</li>"
							+ "<li>Click the \"Highscores\" button to see highscores for each difficulty.</li>"
						+ "</ul><br>"
					+ "</html>"
				);
				
				text.setBorder(padding);
				text.setFont(new Font("Sans Serif", Font.PLAIN, 18));
				
				JPanel playContainer = new JPanel();
				playContainer.setBorder(padding);
				JButton play = new JButton("Let's Play!");
				play.setFont(new Font("Sans Serif", Font.ITALIC, 24));
				play.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						instr.dispose();
					}
				});
				
				instr.add(text);
				playContainer.add(play);
				instr.add(playContainer, BorderLayout.SOUTH);
				
				//instr.getGraphics().setFont(instr.getGraphics().getFont().deriveFont((float) 18));
				//frame.setLocation(Grid.scale * 8, Grid.scale * 8);
//				final JLabel title = new JLabel("<html>M I N E S W E E P E R<br><br></html>");
//				title.setFont(new Font("Serif", Font.PLAIN, 18));
//				final JLabel about = new JLabel("Built by Cameron Cabo for my CIS 120 final project. Enjoy!");
//				final JLabel text = new JLabel("<html>Instructions:<br><br></html>");
//				final JLabel how = new JLabel("How to play:");
//				final JLabel steps = new JLabel("<html>Left click anywhere on the board to start the game.<br>"
//												+ "From there, left click any unrevealed box to see what's below!<br>"
//												+ "Right click to mark unrevealed boxes; you can mark them as mines<br>"
//												+ "or as unsure, with a question mark, to help you count.<br>"
//												+ "If you left click a number already touching as many marked mines<br>"
//												+ "as it actually touches, then all adjacent unmarked boxes will be<br>"
//												+ "revealed.</html>");
				
				
//				instr.add(text, BorderLayout.NORTH);
//				instr.add(about, BorderLayout.AFTER_LINE_ENDS);
//				instr.add(how, BorderLayout.AFTER_LINE_ENDS);
//				instr.add(steps, BorderLayout.AFTER_LINE_ENDS);
				
//				instr.setLayout(new GridBagLayout());
//				GridBagConstraints gbc = new GridBagConstraints();
//				gbc.gridwidth = GridBagConstraints.REMAINDER;
//				
//				instr.add(title, gbc);
//				instr.add(about, gbc);
//				instr.add(text,gbc);
//				instr.add(how, gbc);
//				instr.add(steps, gbc);
				
				
				instr.pack();
				//instr.setSize(128, 256);
				//instr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				instr.setVisible(true);
			}
		});
		
		JPanel info = new JPanel();
		info.add(instructions);
		info.add(highscores);
		
		control_panel.add(reset);
		control_panel.add(beginner);
		control_panel.add(intermediate);
		control_panel.add(expert);
		control_panel.add(custom);

		bottom.add(status_panel);
		bottom.add(info);
		frame.add(bottom, BorderLayout.SOUTH);

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
