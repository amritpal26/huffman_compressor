package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;


public class HuffCompressor implements IHuffProcessor {

    private static final String usageMessage = "usage: Compressor target destination";

    // Compress inputFileName and write encoded data to outFileName.
    public static void compressFile(String inputFileName, String outFileName) throws IOException {
        File inputFile = new File(inputFileName);
        File outFile = new File(outFileName);
        
        FrequencyTable table = createFrequencyTable(inputFile);
        table.incrementFrequency(PSEUDO_EOF_SYMBOL);
        TreeNode codeTree = TreeNode.fromFrequencyTable(table);
        CodesTable encodings = new CodesTable(codeTree, SYMBOLS_LIMIT);

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (BitOutputStream outStream = new BitOutputStream(outFile)) {
                writeMagicNumber(outStream);
                writeHeaderData(codeTree, outStream);
                writeEncodedData(encodings, inputStream, outStream);
                outStream.close();
            }
            inputStream.close();
        }

        printStats(inputFile.length(), outFile.length());
    }

    // Create frequency table of each symbol in the text file.
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

    // Step 1: Write magic number at the beginning of the file to identify the file.
    private static void writeMagicNumber(BitOutputStream outStream) {
        outStream.writeBits(BITS_PER_INT, MAGIC_NUMBER);
    }

    // Step 2: Write the header with information to recreate code tree while decompressing.
    private static void writeHeaderData(TreeNode node, BitOutputStream outStream) {
        if (node.isInternal()) {
            outStream.writeBits(1, HEADER_TREE_LEFT_CHILD);
            writeHeaderData(node.left, outStream);
            writeHeaderData(node.right, outStream);
        } else {
            if (node.symbol > PSEUDO_EOF_SYMBOL) {
                throw new IllegalArgumentException("Symbol exceeds the limit.");
            } else if (node.symbol < 0) {
                throw new IllegalArgumentException("Invalid symbol.");
            }

            outStream.writeBits(1, HEADER_TREE_RIGHT_CHILD);
            outStream.writeBits(BITS_PER_HEADER_WORD, node.symbol);
        }
    }

    // Step 3: Write the coded text to the file.
    private static void writeEncodedData(CodesTable codes, BufferedInputStream inputStream, BitOutputStream outStream) throws IOException {
        // Write encoded text from inputStream.
        while (inputStream.available() > 0) {
            int symbol = inputStream.read();
            List<Integer> bits = codes.getBitsForSymbol(symbol);
            for(int bit : bits) {
                outStream.writeBits(1, bit);
            }
        }

        // Write pseudo end of file character at the end.
        List<Integer> bits = codes.getBitsForSymbol(PSEUDO_EOF_SYMBOL);
        for(int bit : bits) {
            outStream.writeBits(1, bit);
        }
    }

    private static void printStats(long sizeBefore, long sizeAfter) {
        double compressionRatio = (double) sizeBefore /  sizeAfter;
        System.out.println("\n-----------------------------\n");
        System.out.println("File compressed");
        System.out.println(String.format("Size before compression: %d bytes", sizeBefore));
        System.out.println(String.format("Size after compression: %d bytes", sizeAfter));
        System.out.println(String.format("Compression ratio: %f", compressionRatio));
        System.out.println("\n-----------------------------\n");
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Wrong arguments.");
            System.err.println(usageMessage);
            return ;
        }

        try {
            compressFile(args[0], args[1]);
        } catch (IOException ex) {
            System.out.println("\n");
            System.err.println("Error reading file: " + ex.getMessage());
            System.out.println("\n");
        }
    }
}