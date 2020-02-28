import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Node implements Comparable<Node> {

	static PrintStream out;
	static int tableSize;
	static int[] solution;
	int[] table;
	Boolean isFinished = true;
	Set<Node> children = new HashSet<Node>();
	Node parent;
	String how;
	int level ;

	public Node(int[] table, Node parent, String how,int level) {
		this.table = table;
		this.parent = parent;
		this.how = how;
		this.level = level;
		hasFinished();
		if (isFinished) {
			out.println(magic(this));
			//long endTime = System.currentTimeMillis();
			//System.out.println("It took " + (endTime - RunnableClass.startTime) + " milliseconds");
			System.exit(0);
		}
	}

	public void hasFinished() {

		if (Arrays.equals(this.table, Node.solution)) {
			isFinished = true;
		} else {
			isFinished = false;
		}
	}

	public static void setSolution(int tableSize) {

		int[] sol = new int[tableSize * tableSize];
		int i;
		for (i = 0; i < tableSize * tableSize - 1; i++) {
			sol[i] = i + 1;
		}
		sol[i] = 0;
		Node.solution = sol;

	}

	public static int indexOfZero(Node node) {
		int j, i;
		j = tableSize * tableSize;
		for (i = 0; i < j; i++) {
			if (node.table[i] == 0) {
				return i;
			}

		}
		return -1;
	}

	public static void makeMoves(Node node) {
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
			node.children.add(new Node(newTable, node, "U",node.level+1));
		}
		// down
		if (j < tableSize - 1) {
			newTable = Arrays.copyOf(node.table, node.table.length);
			newTable[i] = node.table[i + tableSize];
			newTable[i + tableSize] = 0;
			node.children.add(new Node(newTable, node, "D",node.level+1));

		}
		// left
		if (k > 0) {
			newTable = Arrays.copyOf(node.table, node.table.length);
			newTable[i] = node.table[i - 1];
			newTable[i - 1] = 0;
			node.children.add(new Node(newTable, node, "L",node.level+1));

		}
		// right
		if (k < tableSize - 1) {
			newTable = Arrays.copyOf(node.table, node.table.length);
			newTable[i] = node.table[i + 1];
			newTable[i + 1] = 0;
			node.children.add(new Node(newTable, node, "R",node.level+1));

		}

	}

	public static String magic(Node node) {

		if (node == null) {
			return "";
		}
		return magic(node.parent) + node.how;

	}

	public static void setTableSize(int n, PrintStream out) {
		Node.out = out;
		Node.tableSize = n;
		setSolution(n);
	}

	@Override
	public boolean equals(Object o) {
		if (Arrays.equals(table, ((Node) o).table)) {
			return true;
		}
		return false;

	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(table);

	}
	
	public void heuristic() {
		
	}

	@Override
	public int compareTo(Node arg0) {
		
		return 0;
	}

}
