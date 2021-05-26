// Inspiration from https://courses.cs.duke.edu/cps100e/spring11/assign/huff/src/BitInputStream.java

import java.io.*;

public class BitInputStream extends InputStream {

    private static final int BITS_PER_BYTE = 8;
    private static final int mask[] = {
        0x00, 0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff 
    };

    private InputStream stream;
    private int currentByteBitCount = 0;
    private int currentByteBuffer = 0;
    private File file;



    public BitInputStream(String filename) {
        file = new File(filename);
        
        try {
            stream = new BufferedInputStream(new FileInputStream(file));
            currentByteBuffer = currentByteBitCount = 0;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found: " + ex);
        }
    }

    public void reset() throws IOException {
        if (file == null) {
            throw new IOException("Cannot reset bit stream");
        }

        try {
            close();
            stream = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            throw new IOException("File not found: " + ex);
        }
    }
    
    public int read(int bitsToRead) throws IOException {
        if (stream == null) {
            return -1;
        }
        
        int retVal = 0;
        while (bitsToRead > currentByteBitCount) {
            retVal |= (currentByteBuffer << (bitsToRead - currentByteBitCount));
            bitsToRead -= currentByteBitCount;
            
            try {
                currentByteBuffer = stream.read();
                if (currentByteBuffer == -1) {
                    return -1;
                }
            } catch (IOException ex) {
                throw new IOException("Error reading bits: " + ex);
            }
            currentByteBitCount = BITS_PER_BYTE;
        }

        if (bitsToRead > 0) {
            retVal |= currentByteBuffer >> (currentByteBitCount - bitsToRead);
            currentByteBuffer &= mask[currentByteBitCount - bitsToRead];
            currentByteBitCount -= bitsToRead;
        }
        
        return retVal;
    }
    
    @Override
    public int read() throws IOException {
        return read(8);
    }

    public void close() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                throw new RuntimeException("Could not close stream: " + ex);
            }
        }
    }
}
