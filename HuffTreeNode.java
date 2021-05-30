import java.util.PriorityQueue;
public class HuffTreeNode implements Comparable<HuffTreeNode> {

    public int symbol;
    public int weight;         // Number of times the character occcurs
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

    public static HuffTreeNode rootFromFrequencyTable(FrequencyTable table) throws RuntimeException {
        if (table.isEmpty()) {
            throw new RuntimeException("Cannot create table code tree from empty table.");
        }

        PriorityQueue<HuffTreeNode> queue = new PriorityQueue<HuffTreeNode>();
        for(int i = 0; i < table.getSize(); i++) {
            if (table.getFrequency(i) > 0) {
                queue.add(new HuffTreeNode(i, table.getFrequency(i)));
            }
        }

        System.out.println(queue.size());
        while (queue.size() > 1) {
            HuffTreeNode left = queue.remove();
            HuffTreeNode right = queue.remove();
            HuffTreeNode newNode = new HuffTreeNode(-1, left.weight+right.weight, left, right);
            queue.add(newNode);
        }

        HuffTreeNode root = queue.remove();
        System.out.println("weight of root: " + root.weight);
        return root;
    }

}
