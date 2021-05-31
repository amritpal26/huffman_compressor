package huffman;

import java.io.*;

public class Diff {

    public static void areSame(String filename1, String filename2) throws IOException {
        File file1 = new File(filename1);
        File file2 = new File(filename2);

        BitInputStream stream1 = new BitInputStream(file1);
        BitInputStream stream2 = new BitInputStream(file2);

        boolean result = false;
        while (true) {
            int x = stream1.readBits(8);
            int y = stream2.readBits(8);
            if (x == -1){
                result = (y == -1);
                break;
            } else if (y == -1) {
                result = false;
                break;
            } else if (x != y) {
                result = false;
                break;
            }
        }

        stream1.close();
        stream2.close();
        
        System.out.println("\n-----------------------------\n");
        System.out.println(result ? "Files are same." : "Files are not same.");
        System.out.println("\n-----------------------------\n");
    }

    public static void main(String[] args){
        if (args.length < 2) {
            System.err.println("Need two files to test.");
            return ;
        }

        try {
            areSame(args[0], args[1]);
        } catch (IOException ex) {
            System.err.println("Error reading file: " + ex.getMessage());
        }
        System.exit(0);
    }
}