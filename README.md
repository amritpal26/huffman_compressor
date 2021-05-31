# Huffman Compression Algorithm

This is a java program to compress, decompress files encoded using ASCII characters. It also compares difference between compressed and decompressed files. This program uses ASCII characters which are encoded with 8 bits for each character and there are potentially 256 characters. 

For compression the program creates a Huffman tree which is simply a binary tree with each leaf node representing a character and the path to the leaf represents the coding used for that character. This tree is saved in the header of the file which is then used by the decompression algorithm to recreate the ASCII encoded text file.

## Usage 

1. Compress a file :
	```
	java Huffman.jar <input_text_file_path> <output_text_file_path>
	```
2. Decompress a compressed file. 
	```
	java Huffman.jar <compressed_file_path> <output_text_file_path>
	```
	Note: The file should be compressed with the same program and the program shows relevant error if the file is not compressed using this program.
	
3. Check difference in the file paths:
	```
	java Huffman.jar <file1_path> <file2_path>
	```
