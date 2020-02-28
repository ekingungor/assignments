package ekinn;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class EG2018400261{

	public static void main(String[] args) throws FileNotFoundException {
		Scanner input1 = new Scanner(new File("input.txt"));
		Scanner input2 = new Scanner(new File("input.txt"));

		String coordi = input1.next();
		int X = Integer.parseInt(coordi.substring(0, coordi.indexOf("x")));
		int Y = Integer.parseInt(coordi.substring(coordi.indexOf("x") + 1));

		char[] labels = new char[X * Y];
		readLabels(input1, labels, 0);

		int[] digits = new int[Y * X];
		input2.nextLine();
		readDigits(input2, digits, 0);

		int[] mDigits = Arrays.copyOf(digits, digits.length);

		change(mDigits, labels, 0, Y, X);
		print(digits, 0, Y, X);
		System.out.println();
		print(mDigits, 0, Y, X);

	}

	public static void readLabels(Scanner input1, char[] labels, int n) {

		if (!input1.hasNext()) {
			return;
		}
		labels[n] = input1.next().charAt(0);
		readLabels(input1, labels, n + 1);

	}

	public static void readDigits(Scanner input2, int[] digits, int n) {

		if (!input2.hasNext()) {
			return;
		}

		digits[n] = Integer.parseInt(input2.next().charAt(1) + "");
		readDigits(input2, digits, n + 1);

	}

	public static void change(int[] mDigits, char[] labels, int n, int Y, int X) {

		if (n > X * Y - 1)
			return;

		if (labels[n] == 'F') {
			mDigits[n] = mDigits[n];

		} else if (labels[n] == 'R') {
			int xcoordi = n / Y;
			mDigits[n] = maxOf(mDigits, xcoordi, 0, Y);

		} else if (labels[n] == 'C') {
			int[] medianarr = new int[X];
			int ycoordi = n % Y;
			mDigits[n] = median(mDigits, ycoordi, 0, Y, X, medianarr);

		} else if (labels[n] == 'D') {
			int ycoordi = n % Y;
			int xcoordi = n / Y;
			mDigits[n] = (diagonal(mDigits, xcoordi, ycoordi, Y, X, 1) + mDigits[n])
					/ (howManyDiagonalNumbers(xcoordi, ycoordi, Y, X, 1) + 1);

		} else if (labels[n] == 'N') {
			int ycoordi = n % Y;
			int xcoordi = n / Y;
			int num = mDigits[n];
			replaceNeighbour(mDigits, xcoordi, ycoordi, num, labels, Y, X);

		}

		change(mDigits, labels, n + 1, Y, X);

	}

	public static int maxOf(int[] mDigits, int xcoordi, int k, int Y) {

		if (k + 1 > Y) {
			return 0;
		}
		return Math.max(mDigits[xcoordi * Y + k], maxOf(mDigits, xcoordi, k + 1, Y));
	}

	public static int median(int[] mDigits, int ycoordi, int n, int Y, int X, int[] medianarr) {

		if (!(n > X - 1)) {
			medianarr[n] = mDigits[ycoordi + Y * n];
			median(mDigits, ycoordi, n + 1, Y, X, medianarr);
		}
		Arrays.sort(medianarr);
		return medianarr[(X - 1) / 2];

	}

	public static int diagonal(int[] mDigits, int xcoordi, int ycoordi, int Y, int X, int n) {

		if ((xcoordi - n < 0) && (ycoordi - n < 0) && (xcoordi + n > X - 1) && (ycoordi + n > Y - 1))
			return 0;

		return ((xcoordi - n < 0 || ycoordi - n < 0) ? 0 : mDigits[(xcoordi - n) * Y + (ycoordi - n)])
				+ ((xcoordi + n > X - 1 || ycoordi + n > Y - 1) ? 0 : mDigits[(xcoordi + n) * Y + (ycoordi + n)])
				+ ((xcoordi - n < 0 || ycoordi + n > Y - 1) ? 0 : mDigits[(xcoordi - n) * Y + (ycoordi + n)])
				+ ((xcoordi + n > X - 1 || ycoordi - n < 0) ? 0 : mDigits[(xcoordi + n) * Y + (ycoordi - n)])
				+ diagonal(mDigits, xcoordi, ycoordi, Y, X, n + 1);

	}

	public static int howManyDiagonalNumbers(int xcoordi, int ycoordi, int Y, int X, int n) {

		if ((xcoordi - n < 0) && (ycoordi - n < 0) && (xcoordi + n > X - 1) && (ycoordi + n > Y - 1))
			return 0;

		return ((xcoordi - n < 0 || ycoordi - n < 0) ? 0 : 1) + ((xcoordi + n > X - 1 || ycoordi + n > Y - 1) ? 0 : 1)
				+ ((xcoordi - n < 0 || ycoordi + n > Y - 1) ? 0 : 1)
				+ ((xcoordi + n > X - 1 || ycoordi - n < 0) ? 0 : 1)
				+ howManyDiagonalNumbers(xcoordi, ycoordi, Y, X, n + 1);

	}

	public static void replaceNeighbour(int[] mDigits, int xcoordi, int ycoordi, int num, char[] labels, int Y, int X) {

		if (!(ycoordi + 1 > Y - 1) && labels[xcoordi * Y + ycoordi + 1] == 'N') {

			mDigits[xcoordi * Y + ycoordi + 1] = num;
			labels[xcoordi * Y + ycoordi + 1] = 'p';
			replaceNeighbour(mDigits, xcoordi, ycoordi + 1, num, labels, Y, X);
		}
		if (!(ycoordi - 1 < 0) && labels[xcoordi * Y + ycoordi - 1] == 'N') {
			mDigits[xcoordi * Y + ycoordi - 1] = num;
			labels[xcoordi * Y + ycoordi - 1] = 'p';
			replaceNeighbour(mDigits, xcoordi, ycoordi - 1, num, labels, Y, X);
		}
		if (!(xcoordi + 1 > X - 1) && labels[(xcoordi + 1) * Y + ycoordi] == 'N') {
			mDigits[(xcoordi + 1) * Y + ycoordi] = num;
			labels[(xcoordi + 1) * Y + ycoordi] = 'p';
			replaceNeighbour(mDigits, xcoordi + 1, ycoordi, num, labels, Y, X);
		}
		if (!(xcoordi - 1 < 0) && labels[(xcoordi - 1) * Y + ycoordi] == 'N') {
			mDigits[(xcoordi - 1) * Y + ycoordi] = num;
			labels[(xcoordi - 1) * Y + ycoordi] = 'p';
			replaceNeighbour(mDigits, xcoordi - 1, ycoordi, num, labels, Y, X);
		} else
			return;
	}

	public static void print(int[] mDigits, int n, int Y, int X) {
		if (n > X * Y - 1)
			return;

		System.out.print(mDigits[n] + " ");
		if ((n + 1) % Y == 0)
			System.out.println();
		print(mDigits, n + 1, Y, X);
	}

}
