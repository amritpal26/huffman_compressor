package huffman;

import java.io.IOException;

public class Huffman {

    private static final String usageMessage = "usage: [compress|decompress|diff] target destination";
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Wrong input ");
            System.err.println(usageMessage);
            System.exit(0);
        }

        try {
            String filename1 = args[1];
            String filename2 = args[2];

            if (args[0].equals("compress")) {
                HuffCompressor.compressFile(filename1, filename2);
            } else if (args[0].equals("decompress")) {
                HuffDecompressor.decompressFile(filename1, filename2);
            } else if (args[0].equals("diff")) {
                Diff.areSame(filename1, filename2);
            } else {
                System.err.println("Wrong input.");
                System.err.println(usageMessage);
            }
        } catch (IOException ex) {
            System.err.println("Error reading file: " + ex.toString());
        }
    }
}
