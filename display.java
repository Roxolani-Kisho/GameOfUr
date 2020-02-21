
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

import java.util.*;

public class display implements ActionListener, KeyListener, Runnable {

	private JFrame frame;
	private JLabel gameBoard, bannerLabel, optionsScreen;
	private static JTextArea statsArea;
	private JTextArea messageLabel, introQuestion, tutorial, alphaMessage1, alphaMessage2, lastMove,
			playAgain;
	private JButton button4, button3, button2, button1, button5, button6, button7, button8, button9,
			button10, button11, button12, button13, button14, button15, button16, button17,
			button18, button19, button20, button21, button22, yesButton, noButton, demo1, demo2,
			logo1, logo2, urGameBoard, playAgainButton;
	private JButton[] list1;
	private JButton[] list2;
	private static String TITLE = "The Royal Game of Ur";
	private static String BACKGROUND_IMAGE_FILE_NAME = "resources/RoyalGameofUr.jpg";
	private static String player1 = "resources/button.png";
	private static String player2 = "resources/button2.png";
	private static String music = "resources/EGS.wav";
	private static String path = "resources/path.png";
	private static String flower = "resources/tut.png";
	// private static String logo = "resources/frozen.png";
	private static String background = "resources/background.png";
	private static final Font FONT = new Font("SansSerif", Font.BOLD, 18);
	private static final Color COLOR = new Color(0, 0, 0);
	private Board b;
	private int movement;
	private JCheckBox musicBox, soundBox;
	private AudioClip musicClip;
	private static String SOUND_FILE_NAME = "resources/COIN.wav";
	private boolean ai;
	private boolean firstCheck = false;
	private boolean turn = false;
	private JButton temp;
	private ArrayList<String> lastMoveCounter = new ArrayList<String>();
	private boolean playGame;
	private boolean doYouWantToPlayAgain;

	public static void main(String[] args) throws FileNotFoundException {
		new display();
	}

	public display() throws FileNotFoundException {
		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.addKeyListener(this);

		gameBoard = new JLabel();
		gameBoard.setLayout(null);
		gameBoard.setIcon(new ImageIcon(display.class.getResource(background)));
		optionsScreen = new JLabel();
		optionsScreen.setIcon(new ImageIcon(display.class.getResource(background)));
		frame.setPreferredSize(new Dimension(1280, 720));
		frame.add(gameBoard);
		frame.pack();
		frame.add(optionsScreen);
		center(frame);

		bannerLabel = new JLabel();
		setupComponent(bannerLabel, new Point(0, 0), new Dimension(gameBoard.getWidth(), 30),
				false);
		bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);

		statsArea = new JTextArea();
		setupComponent(statsArea, new Point(1000, 10), new Dimension(250, 100), false);
		statsArea.setEditable(false);
		statsArea.setFocusable(false);

		messageLabel = new JTextArea();
		setupComponent(messageLabel, new Point(10, 10), new Dimension(365, 125), false);
		messageLabel.setLineWrap(true);
		messageLabel.setWrapStyleWord(true);
		messageLabel.setEditable(false);
		messageLabel.setFocusable(false);

		lastMove = new JTextArea();
		setupComponent(lastMove, new Point(600, 10), new Dimension(300, 100), false);
		lastMove.setLineWrap(true);
		lastMove.setWrapStyleWord(true);
		lastMove.setEditable(false);
		lastMove.setFocusable(false);

		introQuestion = new JTextArea();
		setupComponent(introQuestion, new Point(280, 50), new Dimension(365, 100), true);
		introQuestion.setLineWrap(true);
		introQuestion.setWrapStyleWord(true);
		introQuestion.setEditable(false);
		introQuestion.setFocusable(false);

		playAgain = new JTextArea();
		setupComponent(playAgain, new Point(175, 20), new Dimension(250, 100), false);
		playAgain.setLineWrap(true);
		playAgain.setWrapStyleWord(true);
		playAgain.setEditable(false);
		playAgain.setFocusable(false);

		tutorial = new JTextArea();
		setupComponent(tutorial, new Point(20, 320), new Dimension(800, 500), true);
		tutorial.setLineWrap(true);
		tutorial.setWrapStyleWord(true);
		tutorial.setEditable(false);
		tutorial.setFocusable(false);

		alphaMessage1 = new JTextArea();
		setupComponent(alphaMessage1, new Point(10, 10), new Dimension(200, 50), true);
		alphaMessage1.setLineWrap(true);
		alphaMessage1.setWrapStyleWord(true);
		alphaMessage1.setEditable(false);
		alphaMessage1.setFocusable(false);

		alphaMessage2 = new JTextArea();
		setupComponent(alphaMessage2, new Point(10, 620), new Dimension(200, 50), false);
		alphaMessage2.setLineWrap(true);
		alphaMessage2.setWrapStyleWord(true);
		alphaMessage2.setEditable(false);
		alphaMessage2.setFocusable(false);

		button1 = makeButton(null, new Point(495, 149), new Dimension(125, 127), false);
		button2 = makeButton(null, new Point(345, 149), new Dimension(125, 127), false);
		button3 = makeButton(null, new Point(195, 149), new Dimension(125, 127), false);
		button4 = makeButton(null, new Point(45, 149), new Dimension(125, 127), false);
		button5 = makeButton(null, new Point(45, 299), new Dimension(125, 127), false);
		button6 = makeButton(null, new Point(195, 299), new Dimension(125, 127), false);
		button7 = makeButton(null, new Point(345, 299), new Dimension(125, 127), false);
		button8 = makeButton(null, new Point(495, 299), new Dimension(125, 127), false);
		button9 = makeButton(null, new Point(645, 299), new Dimension(125, 127), false);
		button10 = makeButton(null, new Point(795, 299), new Dimension(125, 127), false);
		button11 = makeButton(null, new Point(945, 299), new Dimension(125, 127), false);
		button12 = makeButton(null, new Point(1095, 306), new Dimension(125, 127), false);
		button13 = makeButton(null, new Point(1095, 149), new Dimension(125, 127), false);
		button14 = makeButton(null, new Point(945, 149), new Dimension(125, 127), false);
		button15 = makeButton(null, new Point(784, 126), new Dimension(139, 150), false);
		button16 = makeButton(null, new Point(500, 449), new Dimension(125, 127), false);
		button17 = makeButton(null, new Point(345, 449), new Dimension(125, 127), false);
		button18 = makeButton(null, new Point(195, 449), new Dimension(125, 127), false);
		button19 = makeButton(null, new Point(45, 449), new Dimension(125, 127), false);
		button20 = makeButton(null, new Point(1095, 449), new Dimension(125, 127), false);
		button21 = makeButton(null, new Point(945, 449), new Dimension(125, 127), false);
		button22 = makeButton(null, new Point(782, 450), new Dimension(139, 150), false);

		list1 = new JButton[] { button1, button2, button3, button4, button5, button6, button7,
				button8, button9, button10, button11, button12, button13, button14, button15 };
		list2 = new JButton[] { button16, button17, button18, button19, button5, button6, button7,
				button8, button9, button10, button11, button12, button20, button21, button22 };

		yesButton = makeButton("yes", new Point(300, 100), new Dimension(125, 125), true);

		noButton = makeButton("no", new Point(500, 100), new Dimension(125, 125), true);

		playAgainButton = makeButton(null, new Point(460, 20), new Dimension(100, 100), false);

		frame.setVisible(true);
		gameBoard.setVisible(false);
		new Thread(this).start();

		musicBox = makeCheckBox("MUSIC", true, new Point(1040, 610), new Dimension(120, 20));
		musicBox.addKeyListener(this);

		soundBox = makeCheckBox("SOUNDFX", true, new Point(1040, 640), new Dimension(120, 20));
		soundBox.addKeyListener(this);

		demo2 = makeButton(null, new Point(550, 50), new Dimension(800, 300), true);
		demo2.setIcon(new ImageIcon(display.class.getResource(path)));
		demo2.setBounds(550, 50, 800, 300);
		demo2.setBorderPainted(false);

		demo1 = makeButton(null, new Point(850, 450), new Dimension(125, 125), true);
		demo1.setIcon(new ImageIcon(display.class.getResource(flower)));
		demo1.setBounds(850, 450, 125, 125);
		demo1.setBorderPainted(false);

		logo1 = makeButton(null, new Point(200, 50), new Dimension(80, 80), true);
		// logo1.setIcon(new ImageIcon(display.class.getResource(logo)));
		logo1.setBounds(50, 50, 80, 80);
		logo1.setBorderPainted(false);

		urGameBoard = makeButton(null, new Point(20, -250), new Dimension(1227, 1227), false);
		urGameBoard.setIcon(new ImageIcon(display.class.getResource(BACKGROUND_IMAGE_FILE_NAME)));
		urGameBoard.setBorderPainted(false);

		logo2 = makeButton(null, new Point(200, 50), new Dimension(80, 80), false);
		// logo2.setIcon(new ImageIcon(display.class.getResource(logo)));
		logo2.setBounds(50, 50, 80, 80);
		logo2.setBorderPainted(false);

		b = new Board();
		introQuestion.setText("Would you like to play agaist a computer?");
		alphaMessage1.setText("Early Alpha: Made By Frozen Matchstick");
		alphaMessage2.setText("Early Alpha: Made By Frozen Matchstick");
		playAgain.setText("Player debug is the winner\nClick star to play again");
		tutorial.setText(
				"0 - move 0 squares - i.e. miss a go.\n1 - move 1 square\n2 - move 2 squares\n3 - move 3 squares\n4 - move 4 squares\n5. If a counter lands on a rosette, throw the dice again (and again if another rosette is landed upon). The same piece need not be moved on the additional throw.\n6. Pieces can be moved onto the board at any stage of the game as long as the square that is moved to upon the first turn is vacant.\n7. A player must always move a counter if it is possible to do so but if it is not possible, the turn is lost.\n8. Exact throws are needed to bear pieces off the board.\n9. To move a piece just click the space you want to move to. \n10. Roll probability for 0,4 is 1/16 for 1,3 is 1/4 and 2 is 6/16");
	}

	private JCheckBox makeCheckBox(String text, boolean selected, Point location, Dimension size) {
		JCheckBox box = new JCheckBox(text, selected);
		box.setMnemonic(text.charAt(0));
		setupComponent(box, location, size, false);
		box.setOpaque(true);
		box.setContentAreaFilled(false);
		box.addActionListener(this);
		box.setFocusPainted(false);
		return box;
	}

	private JButton makeButton(String id, Point location, Dimension size, boolean board) {
		JButton button = new JButton(id);
		setupComponent(button, location, size, board);
		button.setOpaque(true);
		button.setContentAreaFilled(false);
		button.addActionListener(this);
		button.setFocusPainted(false);
		return button;
	}

	private void setupComponent(JComponent comp, Point location, Dimension size, boolean board) {
		comp.setLocation(location);
		comp.setSize(size);
		comp.setForeground(COLOR);
		comp.setFont(FONT);
		comp.setOpaque(false);
		if (board) {
			optionsScreen.add(comp);
		}
		else {
			gameBoard.add(comp);
		}
	}

	private void center(JFrame frame) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screen.width - frame.getWidth()) / 2,
				(screen.height - frame.getHeight()) / 2);
	}

	public void run() {
		do {
			musicClip = playAudioClip(music, true);
			try {
				b = new Board();
			}
			catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			playGame();
		}
		while (true);
	}

	private void playGame() {
		statsArea.setText(String.format("Player One score: " + b.score1() + "\nPlayer Two score: "
				+ b.score2() + "\nPlayer One pieces left: " + b.left1()
				+ "\nPlayer Two pieces left: " + b.left2()));
		lastMove.setVisible(true);
		playAgain.setVisible(false);
		playAgainButton.setVisible(false);
		boolean continueGame = true;
		while (continueGame) {
			turn = b.turn();
			boolean temp = turn;
			int roll = b.rollGet();
			String temp2 = "Player ";
			if (turn) {
				temp2 += "One ";
			}
			else {
				temp2 += "Two ";
			}
			String temp3 = ("Roll# " + roll);
			messageLabel.setText(temp2 + "Turn\n" + temp3);

			while (!firstCheck) {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {

				}
			}
			b.play(ai);
			if (roll != -1) {
				if (ai && !turn) {
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {

					}
				}
				if (b.getWinner() != 0) {
					continueGame = false;
				}
				if (roll == 0) {
					temp = !temp;
				}
				if (b.turnSkiped()) {
					try {
						Thread.sleep(500);
					}
					catch (InterruptedException e) {
					}
					if (turn) {
						messageLabel.setText("Player One \nhas no legal move turn skiped");
					}
					else {
						messageLabel.setText("Player Two \nhas no legal move turn skiped");
					}
					try {
						Thread.sleep(1500);
					}
					catch (InterruptedException e) {
					}
				}
				if (!b.turnSkiped() && b.getTest() != -1 && roll != 0) {
					pieceMove(roll, temp);
				}
			}
			b.scoreCheck();
			statsArea.setText(String.format("Player One score " + b.score1() + "\nPlayer Two score "
					+ b.score2() + "\nPlayer One pieces left " + b.left1()
					+ "\nPlayer Two pieces left " + b.left2()));
			lastMoveCounter.add(0, temp2 + " " + temp3 + "\n");

			String record = "";
			if (lastMoveCounter.size() > 4) {
				lastMoveCounter.remove(4);
			}
			int size = lastMoveCounter.size();
			for (int i = 1; i <= size; i++) {
				record += "[" + i + "] " + lastMoveCounter.get(i - 1);
			}
			lastMove.setText(record);
		}
		String winner = "Player ";
		if (b.getWinner() == 1) {
			winner += "One ";
		}
		else if (b.getWinner() == 2) {
			winner += "Two ";
		}
		playAgain.setText(String.format(winner + " is the winner\nClick star to play again"));
		lastMove.setVisible(false);
		playAgain.setVisible(true);
		playAgainButton.setVisible(true);
		while(!doYouWantToPlayAgain) {
			try {
				Thread.sleep(1500);
			}
			catch (InterruptedException e) {
			}
		}
		doYouWantToPlayAgain = false;
	}

	public void keyPressed(KeyEvent arg0) {

	}

	public void keyReleased(KeyEvent arg0) {

	}

	public void keyTyped(KeyEvent arg0) {

	}

	private void printPiece(boolean player) {
		if (player) {
			(temp).setIcon(new ImageIcon(display.class.getResource(player1)));
		}
		else {
			(temp).setIcon(new ImageIcon(display.class.getResource(player2)));
		}
	}

	public void pieceMove(int roll, boolean turn) {
		int cheecker = movement - roll;
		if (ai && !turn) {
			temp = list2[b.getTest()];
			cheecker = b.getTest() - roll;
		}
		printPiece(turn);
		if (cheecker >= 0) {
			if (turn) {
				list1[cheecker].setIcon(null);
			}
			else {
				list2[cheecker].setIcon(null);
			}
		}
		list2[list2.length - 1].setIcon(null);
		list1[list1.length - 1].setIcon(null);
		playSound();
	}

	private class SoundPlayer implements Runnable {
		public void run() {
			playSoundReally();
		}
	}

	private void playSound() {
		if (soundBox.isSelected()) {
			new Thread(new SoundPlayer()).start();
		}
	}

	private void playSoundReally() {
		String filename = String.format(SOUND_FILE_NAME);
		playAudioClip(filename, false);
	}

	private AudioClip playAudioClip(String filename, boolean loop) {
		URL url = ClassLoader.getSystemResource(filename);
		if (url == null) {
			try {
				url = new File(filename).toURI().toURL();
			}
			catch (MalformedURLException e) {
			}
		}

		AudioClip clip = null;
		try {
			clip = Applet.newAudioClip(url);
			if (loop) {
				clip.loop();
			}
			else {
				clip.play();
			}
		}
		catch (NullPointerException e) {
		}
		return clip;
	}

	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		int val = 0;
		if (src == musicBox) {
			if (musicClip != null) {
				if (musicBox.isSelected()) {
					musicClip.loop();
				}
				else {
					musicClip.stop();
				}
			}
			else {
				musicClip = playAudioClip(music, true);
			}
		}
		else if (src == soundBox) {
			soundBox.isSelected();
		}
		else if (src == yesButton || src == noButton) {
			optionsScreen.setVisible(false);
			gameBoard.setVisible(true);
			firstCheck = true;
			if (src == yesButton) {
				ai = true;
			}
			else {
				ai = false;
			}
		}
		else if (src == playAgainButton) {
			for (int i = 0; i < list1.length; i++) {
				(list1[i]).setIcon(null);
			}
			for (int i = 0; i < list2.length; i++) {
				(list2[i]).setIcon(null);
			}
			doYouWantToPlayAgain = true;
		}

		else if (ai && !turn) {

		}
		else {
			temp = (JButton) src;
			if (turn) {
				val = Arrays.asList(list1).indexOf(temp);
			}
			else {
				val = Arrays.asList(list2).indexOf(temp);
			}
			b.userImput(val);
			movement = val;
		}
	}
}
