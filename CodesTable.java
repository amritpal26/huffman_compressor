import java.util.List;
import java.util.ArrayList;

public class CodesTable {
    
    private List<List<Integer>> codes;

    public CodesTable(TreeNode codeTree, int sizeLimit) {
        codes = new ArrayList<>(sizeLimit);
        for (int i = 0; i < sizeLimit; i++) {
            codes.add(null);
        }

        buildCodeList(codeTree, new ArrayList<Integer>());
    }

    private void buildCodeList(TreeNode node, List<Integer> prefix) {
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

    public int getSize() {
        return codes.size();
    }
}