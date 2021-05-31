/*
 * Store frequency of each character in the input file.
 */

public class FrequencyTable {

    private int[] frequencies;
    private boolean isEmpty = true;

    public FrequencyTable(int length) {
        if (length < 2) {
            throw new IllegalArgumentException("Length of Frequency table should be greater than 2");
        }
        frequencies = new int[length];
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public int getLimit() {
        return frequencies.length;
    }

    public int getFrequency(int symbol) {
        checkRange(symbol);
		return frequencies[symbol];
	}

    public int incrementFrequency(int symbol) {
        checkRange(symbol);
        isEmpty = false;
		return frequencies[symbol]++;
	}

    public String toString() {
        StringBuilder builder = new StringBuilder();
        int symbolCount = 0;
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                symbolCount++;
                builder.append(String.format("%c --> %d\n", i, frequencies[i]));
            }
        }
        builder.append(String.format("Number of symbols: %d", symbolCount));
        return builder.toString();
	} 

    private void checkRange(int symbol) {
        if (symbol < 0 || symbol > frequencies.length-1) {
            throw new IllegalArgumentException("Symbol out of range");
        }
    }
}