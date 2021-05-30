import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class HuffEncodings {
    
    private HuffTreeNode codeTree;
    private List<List<Integer>> codes;

    public HuffEncodings(FrequencyTable table) {
        codeTree = createCodeTree(table);
        
        codes = new ArrayList<>(table.getLimit());
        for (int i = 0; i < table.getLimit(); i++) {
            codes.add(null);
        }

        buildCodeList(codeTree, new ArrayList<Integer>());
    }

    private HuffTreeNode createCodeTree(FrequencyTable table) {
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

    private void buildCodeList(HuffTreeNode node, List<Integer> prefix) {
        if (node.left != null && node.right != null) {
            prefix.add(0);
            buildCodeList(node.left, prefix);
            prefix.remove(prefix.size()-1);

            prefix.add(1);
            buildCodeList(node.right, prefix);
            prefix.remove(prefix.size()-1);
        } else if (node.left != null) {
            prefix.add(0);
            buildCodeList(node.left, prefix);
            prefix.remove(prefix.size()-1);
        } else if (node.right != null) {
            prefix.add(1);
            buildCodeList(node.right, prefix);
            prefix.remove(prefix.size()-1);
        } else {
            if (node.symbol >= codes.size()) {
                throw new IllegalArgumentException("Symbol exceeds the limit.");
            } else if (codes.get(node.symbol) != null) {
                throw new IllegalArgumentException("symbol occurs more than once.");
            }
            codes.set(node.symbol, new ArrayList<>(prefix));
        }
    }

    public List<Integer> getBitsForSymbol(int symbol) {
        if (symbol <= 0) {
            throw new IllegalArgumentException("Invalid symbol");
        } else if (symbol > codes.size()) {
            throw new IllegalArgumentException("Symbol out of range.");
        }

        return codes.get(symbol);
    }
}