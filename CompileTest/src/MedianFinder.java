import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class MedianFinder {

	private List<Integer> bigHeap = new ArrayList<Integer>();

	private List<Integer> smallHeap = new ArrayList<Integer>();

	public void insertBig(int val) {
		if (bigHeap.size() == 0) {
			bigHeap.add(val);
			return;
		}
		bigHeap.add(val);
		int i = bigHeap.size();
		int number = bigHeap.get(i - 1);
		int rootNum = bigHeap.get(i / 2 - 1);
		while (number > rootNum) {
			bigHeap.set(i / 2 - 1, number);
			bigHeap.set(i - 1, rootNum);
			i = i / 2;
			if (i <= 1)
				break;
			rootNum = bigHeap.get(i / 2 - 1);
		}
	}

	public void insertSmall(int val) {
		if (smallHeap.size() == 0) {
			smallHeap.add(val);
			return;
		}
		smallHeap.add(val);
		int i = smallHeap.size();
		int number = smallHeap.get(i - 1);
		int rootNum = smallHeap.get(i / 2 - 1);
		while (number < rootNum) {
			smallHeap.set(i / 2 - 1, number);
			smallHeap.set(i - 1, rootNum);
			i = i / 2;
			if (i <= 1)
				break;
			rootNum = smallHeap.get(i / 2 - 1);
		}
	}

	public void maintainBig() {
		int i = 1;
		while (i <= bigHeap.size() / 2) {
			int m = bigHeap.get(i - 1);
			int l = bigHeap.get(i * 2 - 1);
			int r = Integer.MIN_VALUE;
			if (i * 2 < bigHeap.size()) {
				r = bigHeap.get(i * 2);
			}
			if (m < l || m < r) {
				if (l < r) {
					bigHeap.set(i - 1, r);
					bigHeap.set(i * 2, m);
					i = i * 2 + 1;
				} else {
					bigHeap.set(i - 1, l);
					bigHeap.set(i * 2 - 1, m);
					i = i * 2;
				}
			} else {
				break;
			}
		}
	}

	public void maintainSmall() {
		int i = 1;
		while (i <= smallHeap.size() / 2) {
			int m = smallHeap.get(i - 1);
			int l = smallHeap.get(i * 2 - 1);
			int r = Integer.MAX_VALUE;
			if (i * 2 < smallHeap.size()) {
				r = smallHeap.get(i * 2);
			}
			if (m > l || m > r) {
				if (l > r) {
					smallHeap.set(i - 1, r);
					smallHeap.set(i * 2, m);
					i = i * 2 + 1;
				} else {
					smallHeap.set(i - 1, l);
					smallHeap.set(i * 2 - 1, m);
					i = i * 2;
				}
			} else {
				break;
			}
		}
	}

	public int getMax() {
		int r = bigHeap.get(0);
		int t = bigHeap.get(bigHeap.size() - 1);
		bigHeap.set(0, t);
		bigHeap.remove(bigHeap.size() - 1);
		maintainBig();
		return r;
	}

	public int getMin() {
		int r = smallHeap.get(0);
		int t = smallHeap.get(smallHeap.size() - 1);
		smallHeap.set(0, t);
		smallHeap.remove(smallHeap.size() - 1);
		maintainSmall();
		return r;
	}

	// Adds a number into the data structure.
	public void addNum(int num) {
		int big = Integer.MIN_VALUE;
		if (bigHeap.size() > 0) {
			big = bigHeap.get(0);
		}
		if (num > big) {
			insertSmall(num);
			if (smallHeap.size() - bigHeap.size() > 1) {
				int t = getMin();
				insertBig(t);
			}
		} else {
			insertBig(num);
			if (bigHeap.size() - smallHeap.size() > 1) {
				int t = getMax();
				insertSmall(t);
			}
		}
	}

	// Returns the median of current data stream
	public double findMedian() {
		if (bigHeap.size() == smallHeap.size())
			return (double) (bigHeap.get(0) + smallHeap.get(0)) / (double) 2;
		return bigHeap.size() > smallHeap.size() ? bigHeap.get(0) : smallHeap
				.get(0);
	}

	public static void main(String[] args) {
		long l = Calendar.getInstance().getTimeInMillis();
		System.out.println(l);
		MedianFinder mf = new MedianFinder();
		mf.addNum(6);
		System.out.println(mf.findMedian());
		mf.addNum(10);
		System.out.println(mf.findMedian());
		mf.addNum(2);
		System.out.println(mf.findMedian());
		mf.addNum(6);
		System.out.println(mf.findMedian());
		mf.addNum(5);
		System.out.println(mf.findMedian());
		mf.addNum(0);
		System.out.println(mf.findMedian());
		mf.addNum(6);
		System.out.println(mf.findMedian());
		mf.addNum(3);
		System.out.println(mf.findMedian());
		mf.addNum(1);
		System.out.println(mf.findMedian());
		mf.addNum(0);
		System.out.println(mf.findMedian());
		mf.addNum(0);
		System.out.println(mf.findMedian());
		long n = Calendar.getInstance().getTimeInMillis();
		System.out.println(n);
		System.out.println(n - l);
	}
}

// Your MedianFinder object will be instantiated and called as such:
// MedianFinder mf = new MedianFinder();
// mf.addNum(1);
// mf.findMedian();