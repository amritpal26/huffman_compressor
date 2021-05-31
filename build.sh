# build jar file.
javac -cp huffman/ huffman/*.java -d bin/
jar -cvfm Huffman.jar manifest.txt -C bin/ . huffman/