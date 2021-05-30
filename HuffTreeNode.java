import java.util.PriorityQueue;

public class HuffTreeNode implements Comparable<HuffTreeNode> {

    public int symbol;
    public int weight;              // Number of times the character occcurs
    public HuffTreeNode left;
    public HuffTreeNode right;

    public HuffTreeNode(int symbol, int weight, HuffTreeNode leftChild, HuffTreeNode rightChild) {
        this.symbol = symbol;
        this.weight = weight;
        this.left = leftChild;
        this.right = rightChild;
    }

    public HuffTreeNode(int symbol, int weight) {
        this.symbol = symbol;
        this.weight = weight;
        this.left = null;
        this.right = null;
    }

    @Override
    public int compareTo(HuffTreeNode node) {
        return weight - node.weight;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString("", this, builder);
        return builder.toString();
    }

    private void toString(String prefix, HuffTreeNode node, StringBuilder builder) {
        if (node.left != null && node.right != null) {
            toString(prefix + "0", node.left , builder);
			toString(prefix + "1", node.right, builder);
        } else if (node.left != null) {
            toString(prefix+"0", node.left , builder);
        } else if (node.right != null) {
            toString(prefix+"1", node.right, builder);
        } else {
            builder.append(String.format("%c -> %s\n", node.symbol, prefix));
        }
    }

    public static HuffTreeNode fromFrequencyTable(FrequencyTable table) {
        if (table.isEmpty()) {
            throw new RuntimeException("Cannot create table code tree from empty table.");
        }

        PriorityQueue<HuffTreeNode> queue = new PriorityQueue<HuffTreeNode>();
        for(int i = 0; i < table.getLimit(); i++) {
            if (table.getFrequency(i) > 0) {
                queue.add(new HuffTreeNode(i, table.getFrequency(i)));
            }
        }

        while (queue.size() > 1) {
            HuffTreeNode left = queue.remove();
            HuffTreeNode right = queue.remove();
            HuffTreeNode newNode = new HuffTreeNode(-1, left.weight+right.weight, left, right);
            queue.add(newNode);
        }

        HuffTreeNode root = queue.remove();
        return root;
    }
}
