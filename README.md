# Explanation of Huffman Code algorithm

This program creates a compressed binary file of a given text file using the Huffman algorithm of optimal prefix code.

It starts by reading and creating a HashMap of the frequency of appearances of each character in the text file.
Next, based on this frequency HashMap the program creates a variable-length prefix code for each character by using Huffman code algorithm (greedy algorithm).
The main principle of the Huffman code algorithm is to assign the shortest prefix code to the most frequent character, in order to minimize the size of the compressed file.
In order to achieve this principle, we put all the characters and their frequencies into a PriorityQueue<Node> (min-heap - see my previous program for min/max heap -- https://github.com/phsimo/MaxHeap.git).
Each node represents a character and its frequency.
From the PriorityQueue, (i) we poll the two least frequent characters, (ii) add their frequencies, (iii) create a new node with this combined frequency, and (iv) add this node back to the PriorityQueue. Note that the new node does not contain any character.
We repeat this process until the PriorityQueue is left with only one single node, which is the root of the prefix-code tree.

In the next step, we obtain the binary representation of the text file by converting each character in the file to its Huffman code. (Note, that the Huffman code is a binary representation).
Next, we need to store this binary representation into a binary file. However, we cannot write this String of 0 and 1 bits directly to a binary file (We could have written it to a text file as 0 and 1 characters, but that would just increase the size of the compressed file).
Therefore, we apply a similar algorithm to the Base64 encoding by grouping every 32 consecutive bits and converting them to an integer (int in Java occupies 4 bytes/32 bits), which are then written into the binary file.
At the end of the grouping process, there maybe less than 32 bits remaining, therefore we need to pad 0 bits to the end to make it 32 bits long.
The number of padded bits is the first integer written in the compressed binary file; the decompressing process has to take this account.

During the decompressing process we first read the number of padded zeros and read the remaining file as usual. We remove the padded zeros from the last group of 32 bits in the file.
The final step is to travel through the binary representation and using the Huffman code tree to recover each character, so that we can obtain the original text file.
