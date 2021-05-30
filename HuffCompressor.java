import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;


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
        }
        return table;
    }

    public static void compressFile(String inputFileName, String outFileName) throws IOException{
        File inputFile = getFileHandle(inputFileName);
        File outFile = getFileHandle(outFileName);
        
        FrequencyTable table = createFrequencyTable(inputFile);
        table.incrementFrequency(EOF_SYMBOL);
        HuffEncodings encodings = new HuffEncodings(table);
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