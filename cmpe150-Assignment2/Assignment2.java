package ekinn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class EG2018400261 {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner console = new Scanner(System.in);

		System.out.println("Welcome to the XOX Game");
		System.out.println("Would you like to load the board from file or create a new one? (L or C)");

		String data = "";//this is the data keeps board configuration during the whole game
							// (a 16-char String, consists of E's:space, X's and O's .)
		// this part gets the information whether user wants to load a file or create
		// with L and C ,with no error
		String input1 = console.next();
		char lc = input1.charAt(0);// this variable keeps 'L' or 'C'
		while ((lc != 'L' && lc != 'C') || input1.length() != 1) {// to prevent a runtime error loop works while input
																	// is wrong;
			System.out.println("Wrong input!");
			System.out.println("L for Load a Board or C for Create New Board)");
			input1 = console.next();// lc is assigned to the input of the user (L or C)
			lc = input1.charAt(0);

		}
		if (lc == 'C') {
			data = "EEEEEEEEEEEEEEEE";// this is the starting point of the board if user does not load file
		}

		else if (lc == 'L') {

			while (true) {// keeps working until the file exists, is readable, and has a valid
							// configuration to play

				System.out.println("Please enter the file name");
				String fileName = console.next();
				File f = new File(fileName);
				

				while (!f.exists() && !f.canRead()) {// checks if file is readable and exists, if not keeps asking for a
														// valid name
					System.out.println("File \"" + fileName + "\" not found! Check your file name");
					System.out.println("Please enter the file name");
					fileName = console.next();// input for file name
					f = new File(fileName);
				}

				data = fileToData(f);// board configuration in the file is converted to data as a String
				if (data.length() == 16 && isOkay(data)) {// this if checks, if the data is not finished,have valid X
															// and O numbers and have enough number of E's X's and O's
					data = fileToData(f);
					System.out.println("Load succesfull");
					break;
				} else {
					System.out.println("Wrong board configurations!");
				}

			}

		}

		System.out.println("Enter your symbol: (X or O)");

		String input = console.next();
		char playerSymbol = input.charAt(0);// variable keeps X or O for the player symbol

		while ((playerSymbol != 'X' && playerSymbol != 'O') || input.length() != 1) {// checks if the input is valid or
																						// not if not keeps asking for a
																						// valid input (X or O)
			System.out.println("Wrong input! Check your symbol");
			System.out.println("Enter your symbol: (X or O)");

			input = console.next();
			playerSymbol = input.charAt(0);
		}
		// this part of code assigns computer symbol to the other symbol which is not
		// chosen by the user
		char computerSymbol;// variable keeps the computer's symbol
		if (playerSymbol == 'X') {
			computerSymbol = 'O';
			System.out.println("You are player X and the computer is player O.");
		} else {
			computerSymbol = 'X';
			System.out.println("You are player O and the computer is player X.");
		}

		int computerCounter = 0;// how many times computer won
		int playerCounter = 0;// how many times player won

		char letter;// will keep 'Y' or 'N' (player wants to play again or not)
		do {
			data = gamePlay(playerSymbol, computerSymbol, data, console, lc);// gamePlay method starts the game and
																				// plays with user until game finishes

			if (hasItFinished(data) != 'T') {// if game is not Tie
				int t = hasItFinished(data) == computerSymbol ? computerCounter++ : playerCounter++;// counter of the
																									// winner is
																									// incremented by
																									// one
				System.out.println(hasItFinished(data) == computerSymbol ? "Computer wins!" : "You win!");// tells the
																											// user who
																											// won
			} else {// if game is tie
				System.out.println("Tie! No one wins.");
			}
			lc = 'C';// the variable lc (Load or Create) is assigned to 'C' after first round of any
						// games whether user loads or creates, we used it in the gamePlay method to
						// decide if the game is loaded or not
			data = "EEEEEEEEEEEEEEEE";// data reseted to starting configurations in case user wants to play again

			System.out.println("Do you want to play again? (Y or N)");// user asked if wants to play again
			String user = console.next();
			letter = user.charAt(0);//
			while ((letter != 'Y' && letter != 'N') || user.length() != 1) {// prevents wrong input from the user and
																			// gets only Y or N
				System.out.println("Wrong input! Do you want to play again? (Y or N)");
				user = console.next();
				letter = user.charAt(0);
			}

		} while (letter == 'Y');// if user wants to play again then while loop starts again,if not prints out
								// who won how many times,to the console
		System.out.println("You: " + playerCounter + " Computer: " + computerCounter);
		System.out.println("Thanks for playing!");

	}
	public static String gamePlay(char playerSymbol, char computerSymbol, String data, Scanner console, char lc) {

		boolean computerIsStarting = computerStarts();// variable keeps if computer starts or not with the help of the
														// computerStarts method
		if (lc == 'L' && whoStartsFile(data) != 'R') {// this means if this round is the first round of a loaded game we
														// can't randomly decide who starts.we need to check
			computerIsStarting = whoStartsFile(data) == computerSymbol ? true : false;// if it shouldn't start randomly
																						// whoStartsFile method says who
																						// should start and
																						// computerIsStarting variable
																						// gets assigned to it's
																						// value(True or False)
		}

		if (computerIsStarting) {// if computer should start
			System.out.println("Computer will start:");

			data = randomPlay(data, computerSymbol);// computer plays randomly and returns the new data
			drawTheTable(data);// and the new board printed to the user
		} else {// if user should start
			System.out.println("You will start:");
			drawTheTable(data);// draw the table and wait for coordinates
		}

		while (hasItFinished(data) == 'N') {// this loop will keep working until game finishes when this loop ends also
											// method ends

			int x = 0;// row number of the board
			int y = 0;// column number of the board
			System.out.println("Enter coordinates:");
			while (true) {	

				while (true) {// this while loop is to prevent wrong input from the user (non integer entry)

					if (console.hasNextInt()) {
						x = console.nextInt();
					} else {// if user does not enter an integer all loop starts again
						System.out.println("Wrong input! Only integers! Try again:");
						console.next();
						continue;
					} // here we have an integer x value now we should get an integer y
					if (console.hasNextInt()) {
						y = console.nextInt();
						break;// if user also enters an integer for y this loop ends
					} else {// but if does not enter an integer for y
						System.out.println("Wrong input! Only integers! Try again:");
						console.next();
						continue;// loop starts from beginning and asks for x
					}

				}

				if (isEmpty(x, y, data)) {// after we get an integer x,y couple if this couple represents an 'E' on the
											// data it is okay to use if not the outer loop starts again and program
											// asks for an integer x value
					break;
				} else {
					System.out.println("Wrong input! Try again:");
				}

			}

			data = putTheSymbol(data, x, y, playerSymbol);// after we get a valid x,y couple use this coordinate to
															// change the old data
			if (hasItFinished(data) != 'N') {// if game finishes program draws the boards and method finishes
				drawTheTable(data);
			}

			if (hasItFinished(data) == 'N') {// if game has not finished

				data = randomPlay(data, computerSymbol);// computer randomly plays and forms the new data,draws the
														// board
				drawTheTable(data);
				// then if game still has not finished,outer loop starts from beginning and asks
				// user for x and y coordinates
			}
		}
		return data;// when the game finishes method returns the last board configuration data to
					// main method to evaluate who won etc.
	}
	
	// this method takes the data, coordinates and the symbol as parameters and
	// changes the previous configurations to put the symbol in given coordinates
	// and returns new data(configurations)			
	public static String putTheSymbol(String data, int x, int y, char symbol) {

		String newData = "";// will be the new data after putting the symbol in its coordinates
		// this for loop examines data char by char,adds to the newData without
		// changing, unless the given coordinates are reached ,at given coordinates it
		// adds the given symbol instead of the data's char
		for (int i = 0; i < data.length(); i++) {// variable i is the number of the char in the given string(data)
			if (i != 4 * (x - 1) + y - 1) {
				newData += data.charAt(i);
			} else {
				newData += symbol;
			}
		}
		return newData;
	}

//this method takes the data (board configurations) as parameter and prints out the board picture at that configuration
	public static void drawTheTable(String data) {

		for (int j = 1; j <= 4; j++) {// j is the number of the rows

			for (int i = 0; i < 4; i++) {// i is the number of the columns

				System.out.print("| ");

				System.out.print(data.charAt(i));

				System.out.print(" |");

			}
			System.out.println();
			data = data.substring(4);// after each row finishes data loses its first 4 char so we can print its first
										// 4 char easily at each row

		}
	}

	// this method takes the data as parameter, checks all conditions to decide
	// whether the game has finished or not, and returns the winner symbol or
	// 'N'(for Not finished) or 'T'(for Tie)
	public static char hasItFinished(String data) {
		// variable i is the chars' location number at given data(board configuration)
		// following for loops and if conditions checks if the 3 consecutive X's or O's
		// are placed in any direction.
		// and if they are so, returns which symbol is that (which is winner)
		// areCharsEqual method takes 3 integers and data as parameters and checks if at
		// that three location the chars are the same and not equal to 'E' (which is
		// empty and meaningless).then returns true or false
		for (int i = 0; i < data.length(); i += 4) {
			if (areCharsEqual(data, i, i + 1, i + 2)) {
				return data.charAt(i) == 'X' ? 'X' : 'O';

			}
			if (areCharsEqual(data, i + 1, i + 2, i + 3)) {
				return data.charAt(i) == 'X' ? 'X' : 'O';

			}
		}
		for (int i = 0; i < 4; i++) {
			if (areCharsEqual(data, i, i + 4, i + 8)) {
				return data.charAt(i) == 'X' ? 'X' : 'O';

			}
			if (areCharsEqual(data, i + 4, i + 8, i + 12)) {
				return data.charAt(i) == 'X' ? 'X' : 'O';

			}
		}
		for (int i = 0; i < 2; i++) {
			if (areCharsEqual(data, i, i + 5, i + 10)) {
				return data.charAt(i) == 'X' ? 'X' : 'O';

			}
		}

		for (int i = 4; i < 6; i++) {
			if (areCharsEqual(data, i, i + 5, i + 10)) {
				return data.charAt(i) == 'X' ? 'X' : 'O';

			}
		}
		for (int i = 2; i < 4; i++) {
			if (areCharsEqual(data, i, i + 3, i + 6)) {
				return data.charAt(i) == 'X' ? 'X' : 'O';

			}
		}

		for (int i = 6; i < 8; i++) {
			if (areCharsEqual(data, i, i + 3, i + 6)) {
				return data.charAt(i) == 'X' ? 'X' : 'O';

			}
		}
		if (!data.contains("E")) {// if no one wins method is able to come here and checks if there is no space
									// for new symbols and declares that game is tie (by returning 'T')
			return 'T';
		}

		return 'N';// if neither there is a winner nor game is tie, then it should be playing, so
					// it declares the game has not finished (by returning 'N')

	}

	// this method takes 3 integers and a string,which is data,(the board
	// configuration)
	// as parameters and checks if the chars at these three integer locations of
	// string are the same and not equal to 'E'
	// -because we use it to determine who won 'E' should be ignored-
	// if they are equal return true else, false
	public static boolean areCharsEqual(String data, int a, int b, int c) {

		if (data.charAt(a) == data.charAt(b) && data.charAt(a) == data.charAt(c) && data.charAt(a) != 'E') {
			return true;
		}
		return false;

	}

	// this method takes a file as parameters and ignores every character in the
	// text, except 'X's 'O's and 'E's.
	// and adds every 'X' 'O' and 'E' to a new string, to form a data, then returns
	// that data
	public static String fileToData(File f) throws FileNotFoundException {
		String data = "";// to store the board configuration which is read from the file
		String datum;// to temporarily store the each line of the file
		Scanner input = new Scanner(f);

		while (input.hasNextLine()) {

			datum = input.nextLine();
			int i = 0;
			while (i < datum.length()) {

				char letter = datum.charAt(i);
				if (letter == 'E' || letter == 'O' || letter == 'X') {
					data += letter;// each 'X' 'O' and 'E'is added to new string called data
				}

				i++;
			}

		}

		return data;
	}

	// this methods randomly decides whether computer starts or not by generating a
	// random integer (1 or 0)
	// 1 is true and 0 is false for this method's return value
	// if yes, return true and vice versa
	public static boolean computerStarts() {

		Random random = new Random();

		int randomNumber = random.nextInt(2);
		return randomNumber == 0 ? true : false;
	}
	// this method is to execute one whole game play, starts with the player's entry
	// of his symbol and ends with a win or tie. method takes 3 chars (two of them
	// are the player and computer symbol and the other (lc) is to handle an
	// exception of a classic game play )
	// 1 string(data) and a scanner to get input from user

	

	// this methods takes a string which is data and a char which is the computer's
	// symbol in the game
	// finds a random suitable place to put the computer's symbol to respond the
	// user's moves
	// after finding a valid coordinate uses those coordinates to change the data by
	// putTheSymbol method and returns the new data(string) after computer's move
	public static String randomPlay(String data, char computerSymbol) {

		Random random = new Random();
		int x = 0;// move's row at the board
		int y = 0;// move's column at the board

		do {
			x = random.nextInt(4) + 1;// these two lines produce an integer between 1 to 4
			y = random.nextInt(4) + 1;// (which s the boards limits)
		} while (!isEmpty(x, y, data));// this method checks if the char at that coordinates is 'E'
										// if not random x's and y's will be generated to find a suitable one

		return putTheSymbol(data, x, y, computerSymbol);// returns the new data
	}

	// this method takes to integer parameters and a string(data) and
	// checks if the char at the given coordinates of the data is 'E'
	// and the coordinates are between the of the board
	// if yes return true and vice versa
	public static boolean isEmpty(int x, int y, String data) {
		if (x <= 4 && x >= 0 && y <= 4 && y >= 0) {
			if (data.charAt(4 * (x - 1) + y - 1) == 'E') {
				return true;
			}
		}
		return false;
	}

	// this method takes a string (data) as parameter and checks if this
	// configuration is possible for the XOX game and available to play further
	// this method is for the uploaded files from the user otherwise no need to this
	// method
	// if yes return true and vice versa
	public static boolean isOkay(String data) {
		int X = 0;// number of X's in the data
		int O = 0;// number of Y's in the data
		for (int i = 0; i < data.length(); i++) {// for loop counts the numbers of X's and O's
			if (data.charAt(i) == 'X') {
				X++;
			} else if (data.charAt(i) == 'O') {
				O++;
			}
		}
		if (Math.abs(X - O) <= 1 && hasItFinished(data) == 'N') {// to make configuration possible difference of the X's
																	// and O's number should be equal or lower than 1
									 								// and the given board configuration should be
																	// unfinished to be able play further
			return true;
		}
		if (hasItFinished(data) != 'N') {
			System.out.println("This board has already finished! ");
		}

		return false;
	}

	// this method takes a string (data) as parameter and returns 'X' for X starts,
	// 'O' for O starts and 'R' for Random because both X and O have the same number
	// of moves played

	public static char whoStartsFile(String data) {
		int X = 0;// number of X's
		int O = 0;// number of O's
		for (int i = 0; i < data.length(); i++) {// for loop counts the number of X's and Y's
			char letter = data.charAt(i);
			if (letter == 'X') {
				X++;
			}
			if (letter == 'O') {
				O++;
			}
		}
		if (X == O) {// returns R if they are equal
			return 'R';
		}
		return X < O ? 'X' : 'O';// if not equal returns the bigger of two's symbol as char

	}
}
