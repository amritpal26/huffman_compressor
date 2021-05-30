import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HuffDecompressor implements IHuffProcessor {
    
    private static final String usageMessage = "usage: HuffDecompressor target destination";

    // Decompress inputFileName and write output to outFileName.
    public static void decompressFile(String inputFileName, String outFileName) throws IOException {
        File inputFile = new File(inputFileName);
        File outFile = new File(outFileName);
        
        TreeNode codeTree = new TreeNode(-1, 0);
        try (BitInputStream inputStream = new BitInputStream(inputFile)) {
            readHeader(inputStream, codeTree);
            try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outFile))) {
                writeOutputFile(inputStream, outputStream, codeTree);
                outputStream.close();
            }
            inputStream.close();
        }
    }


    // Step 2: Read header and recreate code tree to decode encoded text.
    private static void readHeader(BitInputStream inputStream, TreeNode node) throws IOException {
        int bit = inputStream.readBits(1);
        if (bit == 1) {
            int symbol = inputStream.readBits(BITS_PER_HEADER_WORD);
            node.symbol = symbol;
        } else {
            node.left = new TreeNode(-1, 0);
            node.right = new TreeNode(-1, 0);

            readHeader(inputStream, node.left);
            readHeader(inputStream, node.right);
        }
    }

    // Step 3: Write decoded text to output file.
    private static void writeOutputFile(BitInputStream inputStream, OutputStream outputStream, TreeNode codeTree) throws IOException {
        while (true) {
            int symbol = readNextSymbol(inputStream, codeTree);
			if (symbol == PSEUDO_EOF_SYMBOL || symbol == -1)  // EOF symbol
				break;
            outputStream.write(symbol);
		}
    }

    // Helper method to read symbols from bit input stream.
    private static int readNextSymbol(BitInputStream input, TreeNode root) throws IOException {
        TreeNode node = root;
		while (true) {
            TreeNode nextNode;
			int temp = input.readBits(1);
			if (temp == 0) {
                nextNode = node.left;
            } else if (temp == 1) {
                nextNode = node.right;
            } else {
                throw new AssertionError("Unexpected end of file.");
            }
			
			if (nextNode.isLeaf()){
				return nextNode.symbol;
            } else {
				node = nextNode;
            }
		}
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Wrong input ");
            System.err.println(usageMessage);
            return ;
        }

        try {
            decompressFile(args[0], args[1]);
            System.out.println("File decompressed");
        } catch (IOException ex) {
            System.err.println("Error reading file: " + ex.toString());
        }
    }
}
