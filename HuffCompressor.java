import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;


public class HuffCompressor implements IHuffProcessor {
    private static final String usageMessage = "usage: Compressor target destination";

    private static File getFileHandle(String filename) {
        File file = new File(filename);
        return file;
    }

    private static FrequencyTable createFrequencyTable(File file) throws IOException {
        FrequencyTable table = new FrequencyTable(SYMBOLS_LIMIT);
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            while (inputStream.available() > 0) {
                int symbol = inputStream.read();
				table.incrementFrequency(symbol);
            }
            inputStream.close();
        }
        return table;
    }

    public static void compressFile(String inputFileName, String outFileName) throws IOException {
        File inputFile = getFileHandle(inputFileName);
        File outFile = getFileHandle(outFileName);
        
        FrequencyTable table = createFrequencyTable(inputFile);
        table.incrementFrequency(PSEUDO_EOF_SYMBOL);
        HuffTreeNode codeTree = HuffTreeNode.fromFrequencyTable(table);
        CodesTable encodings = new CodesTable(codeTree, SYMBOLS_LIMIT);

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (BitOutputStream outStream = new BitOutputStream(outFile)) {
                writeHeaderData(codeTree, outStream);
                writeEncodedData(encodings, inputStream, outStream);
                outStream.close();
            }
            inputStream.close();
        }
    }

    private static void writeHeaderData(HuffTreeNode node, BitOutputStream outStream) {
        if (node.left != null && node.right != null) {
            outStream.writeBits(1, '0');
            writeHeaderData(node.left, outStream);
            writeHeaderData(node.right, outStream);
        } else if (node.left != null) {
            outStream.writeBits(1, '0');
            writeHeaderData(node.left, outStream);
        } else if (node.right != null) {
            outStream.writeBits(1, '0');
            writeHeaderData(node.right, outStream);
        } else {
            if (node.symbol > PSEUDO_EOF_SYMBOL) {
                throw new IllegalArgumentException("Symbol exceeds the limit.");
            } else if (node.symbol < 0) {
                throw new IllegalArgumentException("Invalid symbol.");
            }

            outStream.writeBits(1, 1);
            outStream.writeBits(BITS_PER_HEADER_WORD, node.symbol);
        }
    }

    private static void writeEncodedData(CodesTable codes, BufferedInputStream inputStream, BitOutputStream outStream) throws IOException {
        while (inputStream.available() > 0) {
            int symbol = inputStream.read();
            List<Integer> bits = codes.getBitsForSymbol(symbol);
            for(int bit : bits) {
                outStream.writeBits(1, bit);
            }
        }
        List<Integer> bits = codes.getBitsForSymbol(PSEUDO_EOF_SYMBOL);
        for(int bit : bits) {
            outStream.writeBits(1, bit);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Wrong input ");
            System.err.println(usageMessage);
            return ;
        }

        try {
            compressFile(args[0], args[1]);
            System.out.println("File compressed");
        } catch (IOException ex) {
            System.err.println("Error reading file: " + ex.toString());
        }
    }
}