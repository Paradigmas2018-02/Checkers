package checkers;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GUI implements ActionListener{
	/** Border width between squares in the game board */
	private final int borderWidth = 1;
	
	/** The frame that will serve to holds the contents of our game */
	private JFrame frame;

	/** The panel that will hold our Board */
	private JPanel boardpanel;

	/** The label that will keep track of remaining pieces for each side */
	private JLabel piecesLabel;

	/** Menubar containing Exit and New Game options */
	private JMenuBar menubar;

	/** File menu */
	private JMenu fileMenu;

	/** New Game menu item */
	private JMenuItem newGame;

	/** Exit menu item */
	private JMenuItem exit;
	
	private Checkers checkers;

	public GUI(Checkers checkers, Board board) {
		this.checkers = checkers;
		// Set up the window information
		frame = new JFrame("Multi Agent - Checkers");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());

		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		// Give our Board a visual representation
		boardpanel = new JPanel(new GridLayout(8, 8));
		boardpanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Keep track of how many pieces are left
		piecesLabel = new JLabel(" ");
		piecesLabel.setHorizontalTextPosition(JLabel.LEFT);
		piecesLabel.setVerticalTextPosition(JLabel.BOTTOM);

		// Add the menubar to the window
		menubar = new JMenuBar();
		fileMenu = new JMenu("File");

		newGame = new JMenuItem("New Game");
		newGame.addActionListener(this);

		exit = new JMenuItem("Exit");
		exit.addActionListener(this);

		fileMenu.add(newGame);
		fileMenu.add(exit);
		menubar.add(fileMenu);

		// Add our board to boardpanel and add everything to the window
		addBoardToPanel(board, boardpanel);
		frame.add(boardpanel);
		frame.add(piecesLabel);
		frame.setJMenuBar(menubar);
		frame.pack();

		// Resize the frame because for some reason it wants to cut off the last
		// character of our JLabel
		Rectangle boundingRect = frame.getBounds();
		frame.setBounds(boundingRect.x, boundingRect.y, boundingRect.width + 5, boundingRect.height);

		frame.setVisible(true);
	}

	/**
	 * Add the Board to a Panel to create the appearance of a checkerboard
	 * 
	 * @param b The Board to add to a JPanel
	 * @param p the JPanel to be added to
	 */
	public void addBoardToPanel(Board b, JPanel p) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Square sq = b.getSquare(i, j);
				// sq.addMouseListener(this);

				JPanel ContainerPanel = new JPanel(new FlowLayout());
				ContainerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderWidth));
				ContainerPanel.add(sq);
				if (sq.getBackgroundColor() == Square.BackgroundColor.DARK)
					ContainerPanel.setBackground(Color.DARK_GRAY);
				else
					ContainerPanel.setBackground(Color.LIGHT_GRAY);
				p.add(ContainerPanel);
			}
		}
	}
	
	public void updateLabel(String newLabel) {
		piecesLabel.setText(newLabel);
	}
	
	
	@Override
	/** Perform the appropriate action when a menu item is clicked */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGame) {
			frame.setVisible(false);
			frame.remove(boardpanel);
			boardpanel = new JPanel(new GridLayout(8, 8));
			boardpanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			Board b = checkers.newBoard();
			addBoardToPanel(b, boardpanel);
			
			frame.pack();
			frame.setVisible(true);
			frame.add(boardpanel, 0);

			checkers.restartGame();
		} else if (e.getSource() == exit) {
			close();
		}

	}

	public void close() {
		frame.setVisible(false);
		frame.dispose();
	}
}
