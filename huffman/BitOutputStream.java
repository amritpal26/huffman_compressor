package huffman;

import java.io.*;

// Inspiration from https://courses.cs.duke.edu/cps100e/spring11/assign/huff/src/BitOutputStream.java
public class BitOutputStream extends OutputStream {

    private static final int BITS_PER_BYTE = 8;
    private static final int mask[] = {
        0x00000000, 
        0x00000001, 0x00000003, 0x00000007, 0x0000000f, 0x0000001f, 0x0000003f, 0x0000007f, 0x000000ff,
        0x000001ff, 0x000003ff, 0x000007ff, 0x00000fff, 0x00001fff, 0x00003fff, 0x00007fff, 0x0000ffff,
        0x0001ffff, 0x0003ffff, 0x0007ffff, 0x000fffff, 0x001fffff, 0x003fffff, 0x007fffff, 0x00ffffff,
        0x01ffffff, 0x03ffffff, 0x07ffffff, 0x0fffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, 0xffffffff,
    };

    private OutputStream stream;
    private int buffer = 0;
    private int bitsToByte = 0;

    public BitOutputStream(File file) throws FileNotFoundException {
        stream = new BufferedOutputStream(new FileOutputStream(file));
        buffer = 0;
        bitsToByte = BITS_PER_BYTE;
    }

    public void flush() {
        try {
            if (bitsToByte == BITS_PER_BYTE) {
                stream.flush();
            } else {
                stream.write((buffer << bitsToByte));
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error flushing buffer: " + ex);
        }
        buffer = 0;
        bitsToByte = BITS_PER_BYTE;
    }

    public void writeBits(int numBits, int value) {
        value &= mask[numBits];

        while (numBits >= bitsToByte) {
            buffer = (buffer << bitsToByte) | (value >>> (numBits - bitsToByte));

            try {
                stream.write(buffer);
            } catch (IOException ex) {
                throw new RuntimeException("Error writing bits: " + ex);
            }

            value &= mask[numBits - bitsToByte];
            numBits -= bitsToByte;
            bitsToByte = BITS_PER_BYTE;
            buffer = 0;
        }

        if (numBits > 0) {
            buffer = (buffer << numBits) | value;
            bitsToByte -= numBits;
        }
    }

    public void close() {
        try{
            flush();
            stream.close();
        }
        catch (IOException ex){
            throw new RuntimeException("Error closing stream" + ex);
        }
    }

    @Override
    public void write(int b) throws IOException {
        writeBits(b, 8);   
    }
}
