import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class RunnableClass {

	// public static long startTime = System.currentTimeMillis();

	public static void main(String[] args) throws FileNotFoundException {

		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));
		String[] st = in.nextLine().split("-");
		int[] table = new int[st.length];
		int tableSize = (int) Math.sqrt(st.length);
		for (int i = 0; i < st.length; i++) {
			table[i] = Integer.parseInt(st[i]);
		}

		if (tableSize == 3) {
			Node.setTableSize(tableSize, out);
			Node root = new Node(table, null, "", 0);
			run(root);
			out.println("N");
		} else {
			Node45.setTableSize(tableSize, out);
			Node45 root = new Node45(table, null, "", 0);
			int[] sol = new int[tableSize * tableSize];
			int i;
			for (i = 0; i < tableSize * tableSize - 1; i++) {
				sol[i] = i + 1;
			}
			sol[i] = 0;
			Node45 soluroot = new Node45(sol, null, "", 0);
			runX(root, soluroot, tableSize);
		}

	}

	public static void run(Node root) {

		Queue<Node> q = new LinkedList<Node>();
		Set<Node> s = new HashSet<Node>();
		q.add(root);
		s.add(root);
		Node current;

		while (!q.isEmpty()) {

			current = q.poll();
			Node.makeMoves(current);

			for (Node nod : current.children) {
				if (!s.contains(nod)) {
					s.add(nod);
					q.add(nod);
				}
			}
		}

	}

	public static void runX(Node45 root, Node45 soluroot, int tableSize) {

		Queue<Node45> q = new LinkedList<Node45>();

		Queue<Node45> q2 = new LinkedList<Node45>();

		Set<Node45> s = new HashSet<Node45>();

		Set<Node45> s2 = new HashSet<Node45>();

		Set<Node45> controlLevel1 = new HashSet<Node45>();

		Set<Node45> controlLevel2 = new HashSet<Node45>();

		q.add(root);

		s.add(root);

		q2.add(soluroot);

		s2.add(soluroot);

		controlLevel2.add(soluroot);

		Node45 current;

		Node45 current2;

		int currentLevel1 = 0;

		int currentLevel2 = 0;

		int i = 0;

		boolean isFirst = true;

		while (!q.isEmpty()) {

			current = q.poll();

			if (currentLevel1 != current.level) {

				q.add(current);

				currentLevel1++;

			//	System.out.println(" Bad Level : " + (currentLevel1 - 1) + "has finished");

				for (Node45 nod : controlLevel2) {

					if (controlLevel1.contains(nod)) {

						controlLevel1.retainAll(controlLevel2);
						controlLevel2.retainAll(controlLevel1);

						a:
						for (Node45 nodex : controlLevel1) {

							for (Node45 nodex2 : controlLevel2) {

								if (nodex.equals(nodex2)) {

									Node45.out.println(Node45.magicForInitial(nodex) + Node45.magicForSolution(nodex2));
									break a;
									
									

								}
							}

						}

						System.exit(0);

					}

				}

				controlLevel2.clear();

				while (!q2.isEmpty()) {

					current2 = q2.poll();

					if (currentLevel2 != current2.level) {

						q2.add(current2);

						currentLevel2++;

						//System.out.println(" Good Level : " + (currentLevel2 - 1) + "has finished");

						for (Node45 nod : controlLevel2) {

							if (controlLevel1.contains(nod)) {

								controlLevel1.retainAll(controlLevel2);

								controlLevel2.retainAll(controlLevel1);

								b:
								for (Node45 nodex : controlLevel1) {

									for (Node45 nodex2 : controlLevel2) {

										if (nodex.equals(nodex2)) {

											Node45.out.println(
													Node45.magicForInitial(nodex) + Node45.magicForSolution(nodex2));
											break b;

										}
									}

								}

								System.exit(0);

							}

						}

						controlLevel1.clear();

						break;

					}

					Node45.makeMoves(current2);

					for (Node45 node : current2.children) {

						if (!s2.contains(node)) {

							controlLevel2.add(node);

							s2.add(node);

							q2.add(node);

						}

					}

				}

				continue;

			}

			Node45.makeMoves(current);

			controlLevel1.add(current);

			for (Node45 nod : current.children) {

				if (!s.contains(nod)) {

					s.add(nod);

					q.add(nod);

				}

			}

		}

	}

}
