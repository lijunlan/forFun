public class NumArray {

	public class Node {
		public int start;
		public int end;
		public int sum;
		public Node leftNode;
		public Node rightNode;

	}

	private Node rootNode;

	private Node buildTree(int[] nums, int start, int end) {
		Node node = new Node();
		node.start = start;
		node.end = end;
		if (start == end) {
			node.leftNode = null;
			node.rightNode = null;
			node.sum = nums[start];
		} else {
			node.leftNode = buildTree(nums, start, (start + end) / 2);
			node.rightNode = buildTree(nums, (start + end) / 2 + 1, end);
			node.sum = node.leftNode.sum + node.rightNode.sum;
		}
		return node;
	}

	private int getSum(Node node, int start, int end) {
		if (start == node.start && end == node.end)
			return node.sum;
		if (start > (node.start + node.end) / 2) {
			return getSum(node.rightNode, start, end);
		}
		if (end <= (node.start + node.end) / 2) {
			return getSum(node.leftNode, start, end);
		}

		return getSum(node.leftNode, start, (node.start + node.end) / 2)
				+ getSum(node.rightNode, (node.start + node.end) / 2 + 1, end);
	}

	private void changeTree(Node node, int index, int val) {
		if (node.start == node.end && node.start == index) {
			node.sum = val;
			return;
		}
		if (index > (node.start + node.end) / 2) {
			changeTree(node.rightNode, index, val);
		} else {
			changeTree(node.leftNode, index, val);
		}
		node.sum = node.rightNode.sum + node.leftNode.sum;
	}

	public NumArray(int[] nums) {
		if (nums.length == 0)
			return;
		rootNode = buildTree(nums, 0, nums.length - 1);
	}

	void update(int i, int val) {
		changeTree(rootNode, i, val);
	}

	public int sumRange(int i, int j) {
		return getSum(rootNode, i, j);
	}
}
