import java.util.PriorityQueue;

/*
 * Huffman code tree used for compression and decompression.
 */
public class TreeNode implements Comparable<TreeNode> {

    public int symbol;
    public int weight;              // Number of times the character occcurs
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int symbol, int weight, TreeNode leftChild, TreeNode rightChild) {
        this.symbol = symbol;
        this.weight = weight;
        this.left = leftChild;
        this.right = rightChild;
    }

    public TreeNode(int symbol, int weight) {
        this.symbol = symbol;
        this.weight = weight;
        this.left = null;
        this.right = null;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public boolean isInternal() {
        return !this.isLeaf();
    }

    @Override
    public int compareTo(TreeNode node) {
        return weight - node.weight;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString("", this, builder);
        return builder.toString();
    }

    private void toString(String prefix, TreeNode node, StringBuilder builder) {
        if (node.isInternal()) {
            toString(prefix + "0", node.left , builder);
			toString(prefix + "1", node.right, builder);
        } else {
            builder.append(String.format("%c -> %s\n", node.symbol, prefix));
        }
    }

    public static TreeNode fromFrequencyTable(FrequencyTable table) {
        if (table.isEmpty()) {
            throw new RuntimeException("Cannot create table code tree from empty table.");
        }

        PriorityQueue<TreeNode> queue = new PriorityQueue<TreeNode>();
        for(int i = 0; i < table.getLimit(); i++) {
            if (table.getFrequency(i) > 0) {
                queue.add(new TreeNode(i, table.getFrequency(i)));
            }
        }

        while (queue.size() > 1) {
            TreeNode left = queue.remove();
            TreeNode right = queue.remove();
            TreeNode newNode = new TreeNode(-1, left.weight+right.weight, left, right);
            queue.add(newNode);
        }

        TreeNode root = queue.remove();
        return root;
    }
}
