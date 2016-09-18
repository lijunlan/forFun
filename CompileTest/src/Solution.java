import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

public class Solution {
	
	public static int[][] floyd(int map[][],int n){
		
//		return new int[][]{};
		Map<Integer, Integer> map ;map.containsKey(key)
	}

	public static int[] dijkstra(int map[][], int n, int start) {
		int[] record = new int[n];
		record[start] = 1;
		int r = 1;
		int[] dis = new int[n];
		for (int i = 0; i < n; i++) {
			if (i == start)
				continue;
			if (map[start][i] != 0) {
				dis[i] = map[start][i];
			} else {
				dis[i] = Integer.MAX_VALUE;
			}
		}

		while (r < n) {
			int index = 0;
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				if (record[i] == 0 && min > dis[i]) {
					min = dis[i];
					index = i;
				}
			}
			for (int i = 0; i < n; i++) {
				if (i == index || record[i] == 1 || map[index][i] == 0)
					continue;
				if (dis[i] > dis[index] + map[index][i]) {
					dis[i] = dis[index] + map[index][i];
				}
			}
			record[index] = 1;
			r++;
		}
		return dis;
	}

	public static void main(String[] args) {
		// Solution s = new Solution();
		// // Queue<int[]> q ; q.p
		// TreeNode root = create(s, 1);
		// s.upsideDownBinaryTree(root);"".sub
		// StringBuilder sb = new StringBuilder("");
		// sb.reverse();
		// int i = 1;
		// char t = (char)i;
		// sb.delete(start, end);sb.cha
		// long l = Calendar.getInstance().getTimeInMillis();
		// s.generateAbbreviations("word");
		// long n = Calendar.getInstance().getTimeInMillis();
		// System.out.println(n - l);
		// "".toCharArray();
		// s.canWin("+++++++++".toCharArray());
		// s.generatePalindromes("aabb");^[^\\(]{0,}(\\().*\\n[^\\)]{0,}$
		// System.out.println(Pattern.compile("(\\()[^\\(]*\\1")
		// .matcher("(1111(11(1(").find());
		int[][] map = new int[8][8];
		// for (int i = 0; i < 8; i++) {
		// for (int j = 0; j < 8; j++){
		// map[i][j] = 0;
		// }
		// }
		map[0][1] = map[1][0] = -1;
		map[0][2] = map[2][0] = 2;
		map[0][3] = map[3][0] = 3;
		map[1][4] = map[4][1] = 4;
		map[1][5] = map[5][1] = 1;
		map[2][4] = map[4][2] = 3;
		map[3][6] = map[6][3] = 2;
		map[5][7] = map[7][5] = 2;
		map[4][7] = map[7][4] = 3;
		map[4][6] = map[6][4] = 4;
		map[6][7] = map[7][6] = 1;
		int[] dis = dijkstra(map, 8, 0);
		System.out.println(dis);
	}

	public void getStr(int[] record, int length, String now, char middle,
			List<String> ans) {
		if (length == 0) {
			StringBuilder sb = new StringBuilder(now);
			sb.reverse();
			if (middle != '0') {
				sb.insert(0, middle);
			}
			now = now + sb.toString();
			ans.add(now);
		}
		for (int i = 0; i < 256; i++) {
			if (record[i] != 0) {
				StringBuilder s = new StringBuilder(now);
				s.append((char) i);
				record[i]--;
				getStr(record, length - 1, s.toString(), middle, ans);
				record[i]++;
			}
		}
	}

	public List<String> generatePalindromes(String s) {
		List<String> ans = new ArrayList<String>();
		if (s.length() == 1) {
			ans.add(s);
			return ans;
		}
		int[] record = new int[256];
		for (int i = 0; i < s.length(); i++) {
			record[s.charAt(i)]++;
		}
		int sum = 0;
		char middle = '0';
		for (int i = 0; i < 256; i++) {
			if (record[i] % 2 != 0) {
				middle = (char) i;
				sum++;
			}
			record[i] = record[i] / 2;
		}
		if (sum > 1)
			return ans;
		getStr(record, s.length() / 2, "", middle, ans);
		return ans;
	}

	public boolean check(String s, boolean you) {
		if (s.length() == 2) {
			if (s.charAt(0) == '+' && s.charAt(1) == '+')
				return you;
		}
		for (int i = 0; i < s.length() - 2; i++) {
			char one = s.charAt(i);
			char two = s.charAt(i + 1);
			char three = s.charAt(i + 2);
			if (one == two && one == '+') {
				if (one == three) {
					return check(s.substring(i + 2), !you)
							|| check(s.substring(i + 3), !you);
				} else {
					return check(s.substring(i + 2), !you);
				}
			}
		}
		return !you;
	}

	public boolean canWin(String s) {
		return check(s, true);
	}

	public void search(String word, String now, int count, List<String> ans) {
		if (word.length() == 0) {
			if (count != 0) {
				now = now.substring(0, now.length() - count);
				now = now + count;
			}
			ans.add(now);
			return;
		}

		search(word.substring(1), now + "1", count + 1, ans);

		if (count != 0) {
			now = now.substring(0, now.length() - count);
			now = now + count;
		}

		search(word.substring(1), now + word.substring(0, 1), 0, ans);

	}

	public List<String> generateAbbreviations(String word) {
		List<String> ans = new ArrayList<String>();
		search(word, "", 0, ans);
		return ans;
	}

	public static TreeNode create(Solution s, int i) {
		if (i == 0)
			return null;
		TreeNode node = s.new TreeNode(i);
		if (i == 1) {
			node.left = s.new TreeNode(i - 1);
			// node.right = s.new TreeNode(100);
			return node;
		}
		node.left = create(s, i - 1);
		return node;
	}

	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	public void ud(TreeNode root, TreeNode left, TreeNode right) {
		if (left == null)
			return;
		TreeNode newLeft1 = left.left;
		TreeNode newRight1 = left.right;
		TreeNode newRoot1 = left;
		newRoot1.right = root;
		newRoot1.left = right;
		ud(newRoot1, newLeft1, newRight1);
		if (right != null) {
			ud(right, right.left, right.right);
		}
	}

	public TreeNode upsideDownBinaryTree(TreeNode root) {
		if (root == null)
			return null;
		TreeNode r = root;
		while (r.left != null) {
			r = r.left;
		}
		TreeNode newLeft = root.left;
		TreeNode newRight = root.right;
		root.left = null;
		root.right = null;
		ud(root, newLeft, newRight);
		return r;
	}

	public int shortestDistance(int[][] grid) {
		if (grid.length < 1)
			return -1;

		Queue<Integer[]> queue = new LinkedList<Integer[]>();
		Map<String, Integer> record = new HashMap<String, Integer>();

		int num1 = 0;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 1) {
					num1++;
				}
			}
		}

		int ans = Integer.MAX_VALUE;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] != 0) {
					continue;
				}
				int sum = 0;
				int[][] map = new int[grid.length][grid[0].length];
				for (int index = 0; index < grid.length; index++) {
					for (int index2 = 0; index2 < grid[0].length; index2++) {
						map[index][index2] = grid[index][index2];
					}
				}
				insertQueue(queue, map, record, i, j, 0);
				int size = queue.size();
				while (!queue.isEmpty()) {
					for (int count = 1; count <= size; count++) {
						Integer[] cor = queue.poll();
						int x = cor[0];
						int y = cor[1];
						insertQueue(queue, map, record, x - 1, y, sum);
						insertQueue(queue, map, record, x + 1, y, sum);
						insertQueue(queue, map, record, x, y - 1, sum);
						insertQueue(queue, map, record, x, y + 1, sum);
						if (count == size) {
							sum++;
						}
					}
					size = queue.size();
				}
				if (record.size() == num1) {
					Iterator<String> it = record.keySet().iterator();
					int r = 0;
					while (it.hasNext()) {
						String key = it.next();
						int val = record.get(key);
						r += val;
					}
					if (ans > r)
						ans = r;
				}
				record.clear();
			}
		}

		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	public void insertQueue(Queue<Integer[]> queue, int[][] map,
			Map<String, Integer> record, int x, int y, int dis) {
		if (x < 0 || x >= map.length)
			return;
		if (y < 0 || y >= map[0].length)
			return;
		if (map[x][y] == 2)
			return;
		if (map[x][y] == 1) {
			String c = x + "," + y;
			if (record.get(c) == null) {
				record.put(c, dis + 1);
				return;
			}
		}
		if (map[x][y] == 0) {
			Integer[] cor = new Integer[] { x, y };
			queue.offer(cor);
			map[x][y] = 2;
		}
	}

	public int lengthOfLongestSubstringTwoDistinct(String s) {
		if (s.length() <= 2)
			return s.length();

		Map<Character, Integer> record = new HashMap<Character, Integer>();

		int j = 0;
		int ans = 2;
		for (int i = 0; i < s.length(); i++) {
			if (record.size() <= 2) {
				while (j < s.length()) {
					char c = s.charAt(j);
					if (record.get(c) != null) {
						record.put(c, record.get(c) + 1);
					} else {
						if (record.size() == 2) {
							break;
						}
						record.put(c, 1);
					}
					j++;
				}
				if (ans < j - i)
					ans = j - i;
			}

			char c1 = s.charAt(i);
			int sum = record.get(c1);
			if (sum > 1) {
				record.put(c1, record.get(c1) - 1);
			} else {
				record.remove(c1);
			}
		}

		return ans;
	}

	private List<Integer> bigHeap = new ArrayList<Integer>();

	private List<Integer> smallHeap = new ArrayList<Integer>();

	public void insertBig(int val) {
		if (bigHeap.size() == 0) {
			bigHeap.add(val);
			return;
		}
		bigHeap.add(val);
		maintainBig();
	}

	public void insertSmall(int val) {
		if (smallHeap.size() == 0) {
			smallHeap.add(val);
			return;
		}
		smallHeap.add(val);
		maintainSmall();
	}

	public void maintainBig() {
		int i = bigHeap.size() - 1;
		int number = bigHeap.get(i);
		int rootNum = bigHeap.get(i / 2);
		while (number > rootNum) {
			bigHeap.set(i / 2, number);
			bigHeap.set(i, rootNum);
			i = i / 2;
			rootNum = bigHeap.get(i / 2);
		}
	}

	public void maintainSmall() {
		int i = smallHeap.size() - 1;
		int number = smallHeap.get(i);
		int rootNum = smallHeap.get(i / 2);
		while (number < rootNum) {
			smallHeap.set(i / 2, number);
			smallHeap.set(i, rootNum);
			i = i / 2;
			rootNum = smallHeap.get(i / 2);
		}
	}

	// Adds a number into the data structure.
	public void addNum(int num) {
		if (bigHeap.size() < smallHeap.size())
			insertBig(num);
		else
			insertSmall(num);
	}

	// Returns the median of current data stream
	public double findMedian() {
		if (bigHeap.size() == smallHeap.size())
			return (double) (bigHeap.get(0) + smallHeap.get(0)) / (double) 2;
		return bigHeap.size() > smallHeap.size() ? bigHeap.get(0) : smallHeap
				.get(0);
	}

	public class TrieTree {
		public String str;
		public TrieTree[] sons;
		public boolean isRoot;
		public boolean isLeaf;

		public TrieTree() {
			sons = new TrieTree[26];
			isRoot = false;
			isLeaf = false;
		}
	}

	public void insertStr(TrieTree root, String str) {
		char t = str.charAt(0);
		TrieTree node = root.sons[t - 'a'];
		if (node == null) {
			TrieTree tt = new TrieTree();
			StringBuilder sb = new StringBuilder();
			if (root.isRoot) {
				sb.append(t);
			} else {
				sb.append(root.str);
				sb.append(t);
			}
			tt.str = sb.toString();
			root.sons[t - 'a'] = tt;
		}
		if (str.length() == 1) {
			root.sons[t - 'a'].isLeaf = true;
			return;
		}
		insertStr(root.sons[t - 'a'], str.substring(1));
	}

	public boolean findStr(TrieTree root, String str) {
		char t = str.charAt(0);
		TrieTree node = root.sons[t - 'a'];
		if (node == null)
			return false;
		if (str.length() == 1)
			return true;
		return findStr(node, str.substring(1));
	}

	public void dfs(TrieTree root, char[][] board, int[][] record, int x,
			int y, List<String> ans) {
		if (x < 0 || x >= board.length)
			return;
		if (y < 0 || y >= board[0].length)
			return;
		if (record[x][y] == 1)
			return;
		if (root == null)
			return;
		TrieTree node = root.sons[board[x][y] - 'a'];
		if (node == null)
			return;
		if (node.isLeaf) {
			if (!ans.contains(node.str)) {
				ans.add(node.str);
			}
		}
		record[x][y] = 1;
		dfs(node, board, record, x - 1, y, ans);
		dfs(node, board, record, x + 1, y, ans);
		dfs(node, board, record, x, y - 1, ans);
		dfs(node, board, record, x, y + 1, ans);
		record[x][y] = 0;

		return;
	}

	public List<String> findWords(char[][] board, String[] words) {
		List<String> ans = new ArrayList<String>();
		if (board.length == 0)
			return ans;
		TrieTree root = new TrieTree();
		root.isRoot = true;

		for (String str : words) {
			insertStr(root, str);
		}

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				int[][] record = new int[board.length][board[0].length];
				dfs(root, board, record, i, j, ans);
			}
		}

		return ans;
	}

	public class Interval {
		int start;
		int end;

		Interval() {
			start = 0;
			end = 0;
		}

		Interval(int s, int e) {
			start = s;
			end = e;
		}
	}

	public List<Interval> merge(List<Interval> intervals) {
		if (intervals == null || intervals.size() == 0)
			return new ArrayList<Interval>();
		Interval[] list = new Interval[intervals.size()];
		for (int i = 0; i < intervals.size(); i++) {
			list[i] = intervals.get(i);
		}
		Arrays.sort(list, new Comparator<Interval>() {
			public int compare(Interval o1, Interval o2) {
				int ans = o1.start - o2.start;
				if (o1.start == o2.start)
					ans = o1.end - o2.end;
				return ans;
			}
		});
		List<Interval> anslist = new ArrayList<Interval>();
		int nowStart = list[0].start;
		int nowEnd = list[0].end;
		for (int i = 1; i < list.length; i++) {
			if (list[i].start > nowEnd) {
				anslist.add(new Interval(nowStart, nowEnd));
				nowStart = list[i].start;
				nowEnd = list[i].end;
			} else if (list[i].end > nowEnd) {
				nowEnd = list[i].end;
			}
		}
		anslist.add(new Interval(nowStart, nowEnd));
		return anslist;
	}

	private Node rootNode;
	private Node tailNode;

	public class Node {
		public int start;
		public int end;
		public int high;
		public Node next = null;
		public Node last = null;

	}

	public void addTailNode(Node node) {
		tailNode.next = node;
		node.last = tailNode;
		tailNode = node;
	}

	public void replaceNode(Node node, Node... nodeList) {
		Node l = node.last;
		Node r = node.next;
		l.next = nodeList[0];
		nodeList[0].last = l;
		for (int i = 0; i < nodeList.length; i++) {
			if (i != 0) {
				nodeList[i].last = nodeList[i - 1];
			}
			if (i != nodeList.length - 1) {
				nodeList[i].next = nodeList[i + 1];
			}
		}
		nodeList[nodeList.length - 1].next = r;
		if (r != null) {
			r.last = nodeList[nodeList.length - 1];
		} else {
			tailNode = nodeList[nodeList.length - 1];
		}
	}

	public void insertNode(int start, int end, int high) {
		if (rootNode.next == null) {
			Node n0 = new Node();
			n0.start = start;
			n0.end = end;
			n0.high = high;
			rootNode.next = n0;
			n0.last = rootNode;
			tailNode = n0;
			return;
		}
		Node node = null;
		Node nl = rootNode;
		while (nl.next != null) {
			nl = nl.next;
			if (nl.end > start) {
				node = nl;
				break;
			}
		}
		if (node == null) {
			Node t = tailNode;
			if (t.end < start) {
				Node n1 = new Node();
				n1.start = t.end;
				n1.end = start;
				n1.high = 0;
				addTailNode(n1);
			}

			Node n2 = new Node();
			n2.start = start;
			n2.end = end;
			n2.high = high;
			addTailNode(n2);
		} else {
			if (node.end >= end) {
				if (node.high >= high) {
					return;
				} else {
					if (node.start == start && node.end == end) {
						node.high = high;
						return;
					} else {
						Node n3 = null;
						Node n5 = null;
						if (end != node.end) {
							n3 = new Node();
							n3.start = end;
							n3.end = node.end;
							n3.high = node.high;
						}

						Node n4 = new Node();
						n4.start = start;
						n4.end = end;
						n4.high = high;

						if (start != node.start) {
							n5 = new Node();
							n5.start = node.start;
							n5.end = start;
							n5.high = node.high;
						}

						if (n3 != null && n5 != null) {
							replaceNode(node, n5, n4, n3);
						} else if (n3 == null && n5 != null) {
							replaceNode(node, n5, n4);
						} else if (n3 != null && n5 == null) {
							replaceNode(node, n4, n3);
						}
					}
				}
			} else {
				if (node.high < high) {
					if (node.start != start) {
						Node n6 = new Node();
						n6.start = start;
						n6.end = node.end;
						n6.high = high;

						Node n7 = new Node();
						n7.start = node.start;
						n7.end = start;
						n7.high = node.high;

						replaceNode(node, n7, n6);
					} else {
						node.high = high;
					}
				}
				insertNode(node.end, end, high);
			}

		}
	}

	public List<int[]> getSkyline(int[][] buildings) {
		if (buildings.length == 0 || buildings[0].length != 3)
			return new ArrayList<int[]>();
		rootNode = new Node();
		for (int i = 0; i < buildings.length; i++) {
			insertNode(buildings[i][0], buildings[i][1], buildings[i][2]);
		}
		List<int[]> ans = new ArrayList<int[]>();
		Node nl = rootNode;
		int lastHigh = -1;
		while (nl.next != null) {
			nl = nl.next;
			if (lastHigh != nl.high) {
				ans.add(new int[] { nl.start, nl.high });
				lastHigh = nl.high;
			}
		}
		ans.add(new int[] { nl.end, 0 });
		return ans;
	}
}