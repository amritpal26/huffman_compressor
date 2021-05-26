public class HuffTreeNode implements Comparable<HuffTreeNode> {

    public int val;
    public int weight;         // Number of times the character occcurs
    public HuffTreeNode left;
    public HuffTreeNode right;

    public HuffTreeNode(int val, int weight, HuffTreeNode leftChild, HuffTreeNode rightChild) {
        this.val = val;
        this.weight = weight;
        this.left = leftChild;
        this.right = rightChild;
    }

    public HuffTreeNode(int val) {
        this.val = val;
        this.weight = 0;
        this.left = null;
        this.right = null;
    }

    @Override
    public int compareTo(HuffTreeNode node) {
        return weight - node.weight;
    }
}
