import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Node45 {

	static PrintStream out;
	static int tableSize;
	int[] table;
	Set<Node45> children = new HashSet<Node45>();
	Node45 parent;
	String how;
	int level;

	public Node45(int[] table, Node45 parent, String how, int level) {
		this.table = table;
		this.parent = parent;
		this.how = how;
		this.level = level;
	}

	public static int indexOfZero(Node45 node) {
		int j, i;
		j = tableSize * tableSize;
		for (i = 0; i < j; i++) {
			if (node.table[i] == 0) {
				return i;
			}

		}
		return -1;
	}

	public static void makeMoves(Node45 node) {
		int i, j, k;
		i = indexOfZero(node);
		j = i / tableSize;
		k = i % tableSize;
		int[] newTable;
		// up
		if (j > 0) {
			newTable = Arrays.copyOf(node.table, node.table.length);
			newTable[i] = node.table[i - tableSize];
			newTable[i - tableSize] = 0;
			node.children.add(new Node45(newTable, node, "U", node.level + 1));
		}
		// down
		if (j < tableSize - 1) {
			newTable = Arrays.copyOf(node.table, node.table.length);
			newTable[i] = node.table[i + tableSize];
			newTable[i + tableSize] = 0;
			node.children.add(new Node45(newTable, node, "D", node.level + 1));

		}
		// left
		if (k > 0) {
			newTable = Arrays.copyOf(node.table, node.table.length);
			newTable[i] = node.table[i - 1];
			newTable[i - 1] = 0;
			node.children.add(new Node45(newTable, node, "L", node.level + 1));

		}
		// right
		if (k < tableSize - 1) {
			newTable = Arrays.copyOf(node.table, node.table.length);
			newTable[i] = node.table[i + 1];
			newTable[i + 1] = 0;
			node.children.add(new Node45(newTable, node, "R", node.level + 1));

		}

	}

	public static String magicForInitial(Node45 node) {

		if (node == null) {
			return "";
		}
		return magicForInitial(node.parent) + node.how;

	}

	public static String magicForSolution(Node45 node) {

		if (node == null) {
			return "";
		}
		String s = "";
		String c = "";
		while (node.parent != null) {
			c = node.how;

			if (c.equals("U")) {
				c = "D";
			} else if (c.equals("D")) {
				c = "U";
			} else if (c.equals("L")) {
				c = "R";
			} else {
				c = "L";
			}

			s = s + c;
			node = node.parent;
		}
		return s;

	}

	public static void setTableSize(int n, PrintStream out) {
		Node45.out = out;
		Node45.tableSize = n;
	}

	@Override
	public boolean equals(Object o) {
		if (Arrays.equals(table, ((Node45) o).table)) {
			return true;
		}
		return false;

	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(table);

	}

}
