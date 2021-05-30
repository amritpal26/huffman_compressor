public interface IHuffProcessor {

    public static final int BITS_PER_WORD = 8;

    public static final int BITS_PER_HEADER_WORD = 9;

    public static final int ALPHA_SIZE = (1 << BITS_PER_WORD);

    public static final int SYMBOLS_LIMIT = ALPHA_SIZE + 1;
    
    public static final int PSEUDO_EOF_SYMBOL = ALPHA_SIZE;
    
    public static final int BITS_PER_INT = 32;
}
