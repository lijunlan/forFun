import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {

	public static void main(String args[]) {

		// int[] q = { 10, 9, 8, 7, 6, 5, 4, 3, 1 };
		// System.out.println(fractionToDecimal(-2147483648, -10));
		// int a = -2147483648;
		// int[][] r = new int[5][6];
		// System.out.println(r[0].length);
		// char board[][] = { { '0', '0' }, { '0', '0' } };
		// solve(board);
		// System.out.println();
		// qsort(q, 0, q.length - 1);
		// for (int i : q) {
		// System.out.print(i + " ");
		// }
		// // Main m = new Main();
		// // Main m2 = new M2();
		// M2 m3 = new M2();
		// m3.test();
		// // m3.t2();
		// m3.t3();
		// m3.t4();
		// Your NumArray object will be instantiated and called as such:
		// int[] nums = { 0, 9, 5, 7, 3 };
		// NumArray numArray = new NumArray(nums);
		// System.out.println(numArray.sumRange(4, 4));
		// System.out.println(numArray.sumRange(2, 4));
		// System.out.println(numArray.sumRange(3, 3));
		// numArray.update(4, 5);
		// numArray.update(1, 7);
		// numArray.update(0, 8);
		// System.out.println(numArray.sumRange(1, 2));
		// numArray.update(1, 9);
		// System.out.println(numArray.sumRange(4, 4));
		// numArray.update(3, 4);
		//int[] nums = { 1, 3, 1 };
		//containsNearbyAlmostDuplicate(nums, 1, 1);
//		List<Integer> list = new LinkedList<Integer>();
//		Map<Integer, Integer> map ;
//		TreeSet<String> set = new TreeSet<String>();
//		set.add("hot");
//		set.add("dot");
//		set.add("dog");
//		set.add("lot");
//		set.add("log");
//		System.out.println(ladderLength("hit", "cog", set));
//		char[][] grid = {{'1','1'}};
//		System.out.println(numIslands(grid));
//		TreeSet<Integer> set = new TreeSet<Integer>();
//		Linked
	}
	
	public static void dfs(char[][] grid, int x, int y) {
        if (x < 0 || x >= grid.length) return;
        if (y < 0 || y >= grid[0].length) return;
        if (grid[x][y] == '0' || grid[x][y] == 'D') return;
        
        grid[x][y] = 'D';
        
        dfs(grid, x - 1, y);
        dfs(grid, x + 1, y);
        dfs(grid, x, y - 1);
        dfs(grid, x, y + 1);
    }
    
    public static int numIslands(char[][] grid) {
        if (grid.length < 1 || grid[0].length < 1) return 0;
        int ans = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++){
                if (grid[i][j] == '1') {
                    ans++;
                    dfs(grid, i, j);
                }
            }
        }
        return ans;
    }
	
    private static Map<String,Integer> record;
    
    private static Set<String> list;

    public static boolean dfs(int step, String begin, String end){
        if (step < 0) return false;
        Iterator<String> it = list.iterator();
        while(it.hasNext()){
			String word = it.next();
			if (record.get(word) != null) continue;
			if (suit(word, begin)){
			    if (suit(word,end)) return true;
                record.put(word, 1);
			    if(dfs (--step, word, end)){
			        return true;
			    } else {
			        record.remove(word);
			        step++;
			    }
			}
		}
		return false;
    }
    
    public static boolean suit(String a, String b) {
        int sum = 0;
        for (int i = 0; i < a.length(); i++) {
            if(a.charAt(i) != b.charAt(i)) sum++;
        }
        return sum <= 1;
    }
    
    public static int ladderLength(String beginWord, String endWord, Set<String> wordList) {
        if(wordList.size() == 0) return 0;
        list = wordList;
        record = new HashMap<String,Integer>();
        for(int i = 1; i <= wordList.size(); i++){
            if (dfs(i, beginWord, endWord)) return i+3;
        }
        return 0;
    }

	public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
		if (k < 1 || t < 0 || nums == null || nums.length < 2)
			return false;
		TreeSet<Long> set = new TreeSet<Long>();
		set.add((long) nums[0]);
		for (int i = 1; i < nums.length; i++) {
			SortedSet<Long> sset = set.subSet((long) nums[i] - (long) t,
					(long) nums[i] + (long) t + 1L);
			if (!sset.isEmpty()) {
				return true;
			}
			Iterator<Long> it = set.iterator();
			while(it.hasNext()){
				it.next();
			}
			
			set.add((long) nums[i]);
			if (i + 1 > k) {
				set.remove((long) nums[i - k]);
			}
		}
		return false;
	}


	private static Queue<Integer[]> queue = null;
	private static char[][] mboard;
	private static int rows = 0;
	private static int cols = 0;

	public static void solve(char[][] board) {
		if (board.length == 0 || board[0].length == 0)
			return;
		queue = new LinkedList<Integer[]>();
		mboard = board;
		rows = board.length;
		cols = board[0].length;

		for (int i = 0; i < rows; i++) {
			inQueue(i, 0);
			inQueue(i, cols - 1);
		}

		for (int i = 0; i < cols - 1; i++) {
			inQueue(0, i);
			inQueue(rows - 1, i);
		}

		while (!queue.isEmpty()) {
			Integer[] xy = queue.poll();
			int x = xy[0];
			int y = xy[1];

			mboard[x][y] = 'D';

			inQueue(x - 1, y);
			inQueue(x + 1, y);
			inQueue(x, y - 1);
			inQueue(x, y + 1);
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (mboard[i][j] == 'D') {
					mboard[i][j] = 'O';
				} else if (mboard[i][j] == 'O') {
					mboard[i][j] = 'X';
				}
			}
		}
	}

	public static void inQueue(int x, int y) {
		if (x >= 0 && x < mboard.length && y >= 0 && y < mboard[0].length
				&& mboard[x][y] == 'O') {
			queue.offer(new Integer[] { x, y });
		}
	}

	public static String fractionToDecimal(int numerator, int denominator) {
		if (numerator == 0)
			return "0";
		boolean isNegative = false;
		if ((numerator < 0 && denominator < 0)
				|| (numerator > 0 && denominator > 0)) {
			isNegative = false;
		} else {
			isNegative = true;
		}

		HashMap<Long, Integer> record = new HashMap<Long, Integer>();
		long a = Math.abs((long) numerator);
		long b = Math.abs((long) denominator);
		int circle = -1;
		StringBuilder result = new StringBuilder();
		long t = a / b;
		a = a % b;
		a *= 10;
		result.append(String.valueOf(t));
		while (a > 0) {
			int zeroLength = 0;
			record.put(a, result.length());
			while (a < b) {
				zeroLength++;
				a *= 10;
				record.put(a, result.length() + zeroLength);
			}
			for (int i = 1; i <= zeroLength; i++) {
				result.append("0");
			}
			t = a / b;
			a = a % b;
			a *= 10;
			result.append(String.valueOf(t));
			if (record.get(a) != null) {
				circle = record.get(a);
				break;
			}

		}

		long c = Math.abs((long) numerator) / Math.abs((long) denominator);
		int l = String.valueOf(c).length();
		if (l < result.length()) {
			if (circle != -1) {
				result.insert(circle, "(");
				result.append(")");
			}
			result.insert(l, ".");
		}
		if (isNegative)
			result.insert(0, "-");
		return result.toString();
	}

	public class Solution {

		public Solution() {

		}

		/**
		 * @param nums
		 *            a list of integer
		 * @return void
		 */
		public void wiggleSort(int[] nums) {
			// Write your code here
			qsort(nums, 0, nums.length - 1);
			int[] odds = new int[(nums.length / 2) + (nums.length % 2)];
			int[] even = new int[nums.length / 2];
			for (int i = 0; i < nums.length; i++) {
				if (i < (nums.length + 1) / 2) {
					odds[i] = nums[i];
				} else {
					even[i - (nums.length + 1) / 2] = nums[i];
				}
			}
			for (int i = 0; i < nums.length; i++) {
				if (i % 2 == 0) {
					nums[i] = odds[i / 2];
				} else {
					nums[i] = even[i / 2];
				}
			}
		}

		public int select(int[] nums, int start, int end, int rank) {
			if (start == end)
				return nums[start];
			int mid = nums[(start + end) / 2];
			int i = start;
			int j = end;
			while (i <= j) {
				while (nums[i] < mid) {
					i++;
				}
				while (nums[j] > mid) {
					j--;
				}
				if (i <= j) {
					int temp = nums[i];
					nums[i] = nums[j];
					nums[j] = temp;
					i++;
					j--;
				}
			}
			if (rank - 1 > j) {
				return select(nums, i, end, rank - 1 - j);
			} else {
				return select(nums, start, j, rank);
			}
		}

		public void qsort(int[] nums, int start, int end) {
			int mid = nums[(start + end) / 2];
			int i = start;
			int j = end;
			while (i <= j) {
				while (nums[i] < mid)
					i++;
				while (nums[j] > mid)
					j--;
				if (i <= j) {
					int temp = nums[i];
					nums[i] = nums[j];
					nums[j] = temp;
					i++;
					j--;
				}
			}
			if (i < end) {
				qsort(nums, i, end);
			}
			if (j > start) {
				qsort(nums, start, j);
			}
		}
	}

	public static int divide(int dividend, int divisor) {
		if (divisor == 0)
			return Integer.MAX_VALUE;
		if (dividend == 0)
			return 0;

		int ans = 0;
		long divedendT = dividend;
		long divisorT = divisor;
		boolean negative = false;
		if ((divisorT < 0 && divedendT > 0) || (divisorT > 0 && divedendT < 0)) {
			negative = true;
		}
		if (divisorT < 0)
			divisorT = -divisorT;
		if (divedendT < 0) {
			divedendT = -divedendT;
		}

		if (divisorT == 1) {
			if (negative) {
				return (int) -divedendT;
			} else {
				if (divedendT >= 2147483648L)
					return Integer.MAX_VALUE;
				else
					return (int) divedendT;
			}
		}
		if (divisorT > dividend)
			return 0;

		int bit = 0;
		long temp = divedendT;
		while (temp > 1) {
			temp = temp >> 1;
			bit++;
		}
		int remain = (int) (divedendT - (1 << bit));

		int dbit = 0;
		temp = divisorT;
		while (temp > 1) {
			temp = temp >> 1;
			dbit++;
		}
		int t = (int) (divisorT - (1 << dbit));
		int dremain = (int) ((1 << (dbit + t > 0 ? 1 : 0)) - divisorT);

		ans = bit - dbit + 1;
		// ans += divide(multi(dremain, ans), divisor > 0 ? divisor : -divisor);

		ans += divide(remain, divisor > 0 ? divisor : -divisor);
		StringBuilder sb = new StringBuilder();

		if (negative) {
			return -ans;
		} else {
			return ans;
		}
	}

	public static int select(int[] nums, int start, int end, int rank) {
		int mid = nums[(start + end) / 2];
		int i = start;
		int j = end;
		while (i <= j) {
			while (nums[i] < mid) {
				i++;
			}
			while (nums[j] > mid) {
				j--;
			}
			if (i <= j) {
				int temp = nums[i];
				nums[i] = nums[j];
				nums[j] = temp;
				i++;
				j--;
			}
		}
		if (rank >= i) {
			return select(nums, i, end, rank);
		} else if (rank > j && rank < i) {
			return nums[rank];
		} else {
			if (start <= j) {
				return select(nums, start, j, rank);
			} else {
				return nums[rank];
			}
		}
	}

	public static void qsort(int[] nums, int start, int end) {
		int mid = nums[(start + end) / 2];
		int i = start;
		int j = end;
		while (i <= j) {
			while (nums[i] < mid)
				i++;
			while (nums[j] > mid)
				j--;
			if (i <= j) {
				int temp = nums[i];
				nums[i] = nums[j];
				nums[j] = temp;
				i++;
				j--;
			}
		}
		if (i < end) {
			qsort(nums, i, end);
		}
		if (j > start) {
			qsort(nums, start, j);
		}
	}
}
