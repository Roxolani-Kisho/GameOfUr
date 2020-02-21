
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class Board {
	private static boolean[] player2Board = new boolean[15];
	private static boolean[] player1Board = new boolean[15];
	private int left1;
	private int left2;
	private int score1;
	private int score2;
	private int roll;
	private boolean turn;
	private int val = -1;
	private int winner;
	private int returnTemp;
	private boolean extraTurn;
	private boolean skip;
	private static final int SPECIAL_SPACE1 = 3;
	private static final int SPECIAL_SPACE2 = 7;
	private static final int SPECIAL_SPACE3 = 13;
	private static final int TOTAL_PIECES = 7;
	private static final int CENTERBOARD_START = 4;
	private static final int CENTERBOARD_END = 11;
	private boolean another;
	private static final String LOG_FILE = "log.txt";
	private PrintStream output;

	public Board() throws FileNotFoundException {
		left1 = TOTAL_PIECES;
		left2 = TOTAL_PIECES;
		score1 = 0;
		score2 = 0;
		roll();
		turn = start();
		another = true;
		output = new PrintStream(LOG_FILE);
	}

	public void userImput(int i) {
		val = i;
	}

	public boolean[] getPlayerOne() {
		return player1Board;
	}

	public boolean[] getPlayerTwo() {
		return player2Board;
	}

	public int getTest() {
		return returnTemp;
	}

	public int getWinner() {
		return winner;
	}

	public boolean turnSkiped() {
		return skip;
	}

	public void play(Boolean ai) {
		skip = false;
		winner = winner();
		if (winner != 0) {
			System.out.println("player " + winner);
			output.println("player " + winner);
			roll = -1;
		}
		if (roll != -1) {
			if (turn) {
				System.out.println("player1 turn");
				output.println("player1 turn");
			}
			else {
				System.out.println("player2 turn");
				output.println("player2 turn");
			}

			System.out.println("moves " + roll);
			output.println("moves " + roll);
			int test = -1;
			if (roll == 0 || aiMove(roll, turn, false) == -1) {
				System.out.println("can not move turn forfit");
				output.println("can not move turn forfit");
				skip = true;
			}
			else {
				do {
					try {
						Thread.sleep(200);
					}
					catch (InterruptedException e) {

					}
					if (ai == true && !turn) {
						test = aiMove(roll, turn, true);
						returnTemp = test;
					}
					if (val > -1) {
						System.out.println("which spot?");
						output.println("which spot?");
						test = move(roll, turn, val);
						returnTemp = test;
					}
				}
				while (test == -1);
				val = -1;
			}
			if (extraTurn) {
				extraTurn = false;
				turn = !turn;
			}
			if (another) {
				turn = !turn;
				roll();
			}
			System.out.println();
			output.println();
		}
	}

	public int rollGet() {
		return roll;
	}

	public int score1() {
		return score1;
	}

	public int score2() {
		return score2;
	}

	public int left2() {
		return left2;
	}

	public int left1() {
		return left1;
	}

	public boolean turn() {
		return turn;
	}

	public boolean start() throws FileNotFoundException {
		Random r = new Random();
		return r.nextBoolean();
	}

	public int winner() {
		if (score1 == TOTAL_PIECES) {
			return 1;
		}
		else if (score2 == TOTAL_PIECES) {
			return 2;
		}
		else {
			return 0;
		}

	}

	public void roll() {
		Random r = new Random();
		roll = 0;
		for (int i = 0; i < 4; i++) {
			int vale = r.nextInt() % 2;
			if (vale == 1 || vale == -1) {
				roll++;
			}
		}
	}

	public int aiMove(int spaces, boolean turn, boolean ai) {
		if (turn) {
			return aiMove(spaces, player1Board, player2Board, ai, left1, left2, score1);
		}
		else {
			return aiMove(spaces, player2Board, player1Board, ai, left2, left1, score2);
		}
	}

	// first attempts to move to special spot
	// Second attempts to hit
	// Third attempt to bring out a new piece
	// fourth moves piece
	private int specialSpaceMove(ArrayList<Integer> pieces, int spaces, boolean[] playerYou,
			boolean[] playerOther, boolean ai, int specialSpace) {
		int val = -1;
		for (int i : pieces) {
			if (i + spaces == specialSpace && !playerYou[specialSpace]) {
				if (ai) {
					extraTurn = true;
					System.out.println("extra turn");
					output.println("extra turn");
					val = move(i, spaces, playerYou, playerOther);
				}
				else {
					val = 1;
				}
			}
		}
		return val;
	}

	private int aiMove(int spaces, boolean[] playerYou, boolean[] playerOther, boolean ai, int left,
			int leftOther, int playerScore) {
		boolean pieceInMiddle = false;
		boolean standStill = false;
		ArrayList<Integer> pieces = new ArrayList<>();
		for (int i = 0; i < playerYou.length; i++) {
			if (playerYou[i]) {
				pieces.add(i);
				if (i >= CENTERBOARD_START && i <= CENTERBOARD_END && i != SPECIAL_SPACE2) {
					pieceInMiddle = true;
				}
			}
		}
		// Agressive go for piece
		if (leftOther <= 1) {
			for (int i : pieces) {
				int finalPostion = i + spaces;
				if (finalPostion >= CENTERBOARD_START && finalPostion <= CENTERBOARD_END
						&& finalPostion < playerYou.length - 1) {
					if ((finalPostion != SPECIAL_SPACE2) && playerOther[finalPostion]) {
						return hit(i, spaces, playerYou, playerOther, ai, leftOther);
					}
				}
			}
		}
		// special space
		if (!playerOther[SPECIAL_SPACE2]) {
			int val = specialSpaceMove(pieces, spaces, playerYou, playerOther, ai, SPECIAL_SPACE2);
			if (val != -1) {
				return val;
			}
		}
		int val = specialSpaceMove(pieces, spaces, playerYou, playerOther, ai, SPECIAL_SPACE3);
		if (val != -1) {
			return val;
		}
		if (spaces == 4 && !playerYou[SPECIAL_SPACE1] && left != 0) {
			if (ai) {
				extraTurn = true;
				System.out.println("extra turn");
				output.println("extra turn");
				return firstMove(playerYou, left, spaces);
			}
			return 1;
		}
		val = specialSpaceMove(pieces, spaces, playerYou, playerOther, ai, SPECIAL_SPACE1);
		if (val != -1) {
			return val;
		}
		// hit
		for (int i : pieces) {
			int finalPostion = i + spaces;
			if (finalPostion >= CENTERBOARD_START && finalPostion <= CENTERBOARD_END
					&& finalPostion < playerYou.length - 1) {
				if ((finalPostion != SPECIAL_SPACE2) && playerOther[finalPostion]) {
					return hit(i, spaces, playerYou, playerOther, ai, leftOther);
				}
			}
		}
		// move piece on middle track extra priority to those in danger
		if (pieceInMiddle) {
			for (int i = pieces.size() - 1; i > 0; i--) {
				if (pieces.get(i) >= CENTERBOARD_START && pieces.get(i) <= CENTERBOARD_END
						&& pieces.get(i) + spaces < playerYou.length - 1) {
					if (!playerYou[pieces.get(i) + spaces]
							&& pieces.get(i) + spaces != SPECIAL_SPACE2) {
						if (playerOther[pieces.get(i) - 1] || playerOther[pieces.get(i) - 2]
								|| playerOther[pieces.get(i) - 3]) {
							if (pieces.get(i) != SPECIAL_SPACE2) {
								if (pieces.get(i) + spaces == playerYou.length - 1) {
									if (playerScore == TOTAL_PIECES - 1) {
										another = false;
									}
									return move(pieces.get(i), spaces, playerYou, playerOther, ai);
								}
							}
						}
					}
				}
			}
			for (int i : pieces) {
				if (i + spaces == playerYou.length - 1) {
					if (playerScore == TOTAL_PIECES - 1) {
						another = false;
					}
					return move(i, spaces, playerYou, playerOther, ai);
				}
			}
		}
		// move piece in middle
		for (int i : pieces) {
			if (i >= CENTERBOARD_START && i <= CENTERBOARD_END && i + spaces < playerYou.length - 1
					&& i != SPECIAL_SPACE2) {
				if (!playerYou[i + spaces] && i + spaces != SPECIAL_SPACE2) {
					return move(i, spaces, playerYou, playerOther, ai);
				}
			}
		}
		// start piece on middle path if safe
		for (int i : pieces) {
			if (i + spaces < playerYou.length && !playerYou[i + spaces] && i <= CENTERBOARD_START) {
				if (i + spaces >= CENTERBOARD_START && i + spaces <= CENTERBOARD_END) {
					if (!playerOther[i + spaces - 1] && !playerOther[i + spaces - 2]
							&& !playerOther[i + spaces - 3]) {
						if (i + spaces == SPECIAL_SPACE2) {
							if (!playerOther[SPECIAL_SPACE2]) {
								return move(i, spaces, playerYou, playerOther, ai);
							}
						}
						else {
							return move(i, spaces, playerYou, playerOther, ai);
						}
					}
				}
			}
		}
		// bring new piece out
		if (left != 0 && !playerYou[spaces - 1]) {
			if (!ai) {
				return 1;
			}
			return firstMove(playerYou, left, spaces);
		}
		// get piece off
		for (int i : pieces) {
			if (i + spaces == playerYou.length - 1) {
				if (playerScore == TOTAL_PIECES - 1) {
					another = false;
				}
				return move(i, spaces, playerYou, playerOther, ai);
			}
		}
		// Figure out if standstill
		if (!pieceInMiddle) {
			standStill = true;
			for (int i = pieces.size() - 1; i > 0; i--) {
				if (pieces.get(i) + spaces < playerYou.length
						&& !playerYou[pieces.get(i) + spaces]) {
					if (pieces.get(i) + spaces < CENTERBOARD_START) {
						standStill = false;
					}
				}
			}
		}
		// get a piece as far away from the standstill as possible
		if (standStill) {
			standStill = false;
			for (int i = pieces.size() - 1; i > 0; i--) {
				if (pieces.get(i) + spaces < playerYou.length
						&& !playerYou[pieces.get(i) + spaces]) {
					if (pieces.get(i) + spaces == SPECIAL_SPACE2) {
						if (!playerOther[SPECIAL_SPACE2]) {
							return move(pieces.get(i), spaces, playerYou, playerOther, ai);
						}
					}
					else {
						return move(pieces.get(i), spaces, playerYou, playerOther, ai);
					}
				}
			}
		}
		// basic move
		for (int i : pieces) {
			if (i + spaces < playerYou.length && !playerYou[i + spaces]) {
				if (i + spaces == SPECIAL_SPACE2) {
					if (!playerOther[SPECIAL_SPACE2]) {
						return move(i, spaces, playerYou, playerOther, ai);
					}
				}
				else {
					return move(i, spaces, playerYou, playerOther, ai);
				}
			}
		}
		return -1;
	}

	private int hit(int i, int spaces, boolean[] playerYou, boolean[] playerOther, boolean ai,
			int leftOther) {
		int finalPostion = i + spaces;
		if (!ai) {
			return 1;
		}
		playerOther[finalPostion] = false;
		leftOther++;
		if (!turn) {
			left1 = leftOther;
			System.out.println(left1);
			output.println(left1);
		}
		else {
			left2 = leftOther;
			System.out.println(left2);
			output.println(left2);
		}
		System.out.println("hit");
		output.println("hit");
		return move(i, spaces, playerYou, playerOther);
	}

	private int move(int i, int spaces, boolean[] playerYou, boolean[] playerOther, boolean ai) {
		if (ai) {
			playerYou[i] = false;
			playerYou[i + spaces] = true;
			System.out.println(Arrays.toString(player1Board));
			System.out.println(Arrays.toString(player2Board));
			output.println(Arrays.toString(player1Board));
			output.println(Arrays.toString(player2Board));
			return i + spaces;
		}
		return 1;
	}

	private int move(int i, int spaces, boolean[] playerYou, boolean[] playerOther) {
		return move(i, spaces, playerYou, playerOther, true);
	}

	private int firstMove(boolean[] playerYou, int left, int spaces) {
		playerYou[spaces - 1] = true;
		left--;
		if (turn) {
			left1 = left;
			System.out.println(left1);
			output.println(left1);
		}
		else {
			left2 = left;
			System.out.println(left2);
			output.println(left2);
		}
		System.out.println(Arrays.toString(player1Board));
		System.out.println(Arrays.toString(player2Board));
		output.println(Arrays.toString(player1Board));
		output.println(Arrays.toString(player2Board));
		return spaces - 1;
	}

	public void scoreCheck() {
		if (player1Board[player1Board.length - 1]) {
			player1Board[player1Board.length - 1] = false;
			score1++;
		}
		if (player2Board[player2Board.length - 1]) {
			player2Board[player2Board.length - 1] = false;
			score2++;
		}
	}

	public int move(int spaces, boolean player1, int moveAttempt) {
		if (moveAttempt == -1) {
			return -1;
		}
		if (player1) {
			return move(spaces, player1, moveAttempt, player1Board, player2Board, left1, left2);
		}
		else {
			return move(spaces, player1, moveAttempt, player2Board, player1Board, left2, left1);
		}
	}

	private int move(int spaces, boolean player1, int moveAttempt, boolean[] playerYou,
			boolean[] playerOther, int leftYou, int leftOther) {
		int move = moveAttempt - spaces;
		if (move < -1 || (moveAttempt == SPECIAL_SPACE2 && playerOther[SPECIAL_SPACE2])) {
			return -1;
		}
		if (move == -1) {
			if (leftYou != 0 && !playerYou[moveAttempt]) {
				playerYou[moveAttempt] = true;
				leftYou--;
				if (player1) {
					left1 = leftYou;
					System.out.println(left1);
					output.println(left1);
				}
				else {
					left2 = leftYou;
					System.out.println(left2);
					output.println(left2);
				}
				System.out.println(Arrays.toString(player1Board));
				System.out.println(Arrays.toString(player2Board));
				output.println(Arrays.toString(player1Board));
				output.println(Arrays.toString(player2Board));
				if (moveAttempt == SPECIAL_SPACE1) {
					System.out.println("extra turn");
					output.println("extra turn");
					extraTurn = true;
				}
				return moveAttempt;
			}
			return -1;
		}
		if (playerYou[move] && !playerYou[moveAttempt]) {
			playerYou[move] = false;
			playerYou[moveAttempt] = true;
			if ((playerOther[moveAttempt] && moveAttempt != SPECIAL_SPACE2)
					&& moveAttempt >= CENTERBOARD_START && moveAttempt <= CENTERBOARD_END) {
				playerOther[moveAttempt] = false;
				leftOther++;
				if (player1) {
					left2 = leftOther;
					System.out.println(left2);
					output.println(left2);
				}
				else {
					left1 = leftOther;
					System.out.println(left1);
					output.println(left1);
				}
				System.out.println("hit");
				output.println("hit");
			}
			else if (moveAttempt == SPECIAL_SPACE1 || moveAttempt == SPECIAL_SPACE2
					|| moveAttempt == SPECIAL_SPACE3) {
				System.out.println("extra turn");
				output.println("extra turn");
				extraTurn = true;
			}
			System.out.println(Arrays.toString(player1Board));
			System.out.println(Arrays.toString(player2Board));
			output.println(Arrays.toString(player1Board));
			output.println(Arrays.toString(player2Board));
			return moveAttempt;
		}
		return -1;
	}
}
