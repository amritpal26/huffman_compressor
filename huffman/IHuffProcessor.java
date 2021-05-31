package huffman;

/*
 * Stores Constants for the Compression and decompression.
 */
public interface IHuffProcessor {

    public static final int BITS_PER_WORD = 8;

    public static final int BITS_PER_INT = 32;
    
    public static final int PSEUDO_EOF_SYMBOL = (1 << BITS_PER_WORD);
    
    public static final int SYMBOLS_LIMIT = PSEUDO_EOF_SYMBOL + 1;
    
    // Header related data
    public static final int MAGIC_NUMBER = 0xface8200;
    
    public static final int BITS_PER_HEADER_WORD = 9;

    public static final int HEADER_TREE_LEFT_CHILD = 0;

    public static final int HEADER_TREE_RIGHT_CHILD = 1;
}
