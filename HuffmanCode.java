package greedy;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Given a list of characters, each has a frequency of appearances in a file.
 * Need to build a mapping of prefix code for the characters, so that the size of compressed file is minimized.
 *
 */
public class HuffmanCode {
	
	/**Encapsulates the Map.Entry data into a List of Huffman entries.
	Each Huffman entry contains a Character and a Integer variable that represents the frequency of the character in a file.*/
	private List<HuffmanEntry> createMapsOfHoffmanEntries(Map<Character, Integer> mapOfCharacters) {
		Set<Map.Entry<Character, Integer>> setOfCharcters=mapOfCharacters.entrySet();
		List<HuffmanEntry> listOfEntries = new ArrayList<HuffmanEntry>();
		HuffmanEntry huffmanEntry;
		
		for(Map.Entry<Character, Integer> entry:setOfCharcters) {
			huffmanEntry = new HuffmanEntry(entry.getKey(), entry.getValue());
			listOfEntries.add(huffmanEntry);
		}
		
		return listOfEntries;
		
	}
	
	/** Puts all the given characters and their frequencies (that are stored in a List of Huffman entries) into a priority queue.
	 * A priority queue acts as a min-heap structure, where the root contains the character with the smallest frequency.
	 * To compare the Huffman entries, the compareTo() method of the HuffmanEntry class is used.
	 */
	private PriorityQueue<HuffmanEntry> createPriorityQueu(List<HuffmanEntry> listOfEntries) {
		PriorityQueue<HuffmanEntry> priorityQueu= new PriorityQueue<HuffmanEntry>();
		
		for(HuffmanEntry entry:listOfEntries) {
			priorityQueu.add(entry);
		}
		
		return priorityQueu;
	}
	
	/**
	 * A function that creates a Huffman tree, where each leaf of the tree is one of the characters found on the file
	 * @param mapOfCharacters
	 */
	public HuffmanEntry createHuffmanCode(Map<Character, Integer> mapOfCharacters) {
		List<HuffmanEntry> listOfEntries = createMapsOfHoffmanEntries(mapOfCharacters);
		
		/**
		 * Calls the createPriorityQueue method that inserts the map of Characters and their frequencies 
		 * into a priority queue(min-heap) structure.
		 */
		PriorityQueue<HuffmanEntry> priorityQueue = createPriorityQueu(listOfEntries);
		
		HuffmanEntry entry1, entry2;
		
		
		
		/**
		 *  Loop that executes the algorithm for the creation of the Huffman tree
		 *  First removes from the min-heap the two nodes with the smallest frequencies, then creates a new node 
		 *  which frequency is the sum of the two extracted nodes, which are also its left and right child.
		 *  At the end the new node is inserted in the min-heap.
		 */
		while(priorityQueue.size()>1){ // since a Huffman tree and has n-1 inner node, this is equivalent to: "for(int i=0;i<mapOfCharacters.size()-1;i++)"
			HuffmanEntry entry3= new HuffmanEntry(); // creates a new node
			entry1=priorityQueue.poll(); //extracts the smallest node in the priority queue
			entry2=priorityQueue.poll(); //extracts the smallest node in the priority queue
			entry3.setCharacter(null); // the new node doesn't contain a character
			entry3.setFrequency(entry1.getFrequency()+entry2.getFrequency()); // the frequency of the new node is the sum of the two extracted nodes
			
			entry3.setLeftChild(entry1); // the left child of the new node is the first node to be extracted
			entry3.setRightChild(entry2);// the left child of the new node is the second node to be extracted
			
			priorityQueue.offer(entry3); // the new node is inserted in the min-heap

		}
		
		createDecodingTable(priorityQueue.peek());
		
		return priorityQueue.peek();
		
	}
	
	/**
	 * A function that returns a HashMap with the characters and the their codeword.
	 * It calls the navigateMap function which up and down the tree in order to retrieve the codeword
	 * @param root
	 * @return HashMap containing the character and its codeword
	 */
	public Map<Character, StringBuffer> createDecodingTable(HuffmanEntry root) {
		Map<Character, StringBuffer> decodingMap= new HashMap<Character, StringBuffer>();
		
		StringBuffer characterCode= new StringBuffer();
		
		 decodingMap=navigateMap(decodingMap,root,characterCode);
		 
		 return  decodingMap;
	}
	
	/**
	 * A function that navigates the tree recursively and creates the codeword for each character.
	 * @param decodingMap HashMap of characters and their codeword
	 * @param node Current Node on the tree
	 * @param characterCode StringBuffer containing the codeword
	 * @return
	 */
	private Map<Character, StringBuffer> navigateMap(Map<Character, StringBuffer> decodingMap, HuffmanEntry node,StringBuffer characterCode) {
		
		if(node.getLeftChild()!=null) { //checks if the left child is empty
			// if not empty, it navigates on that node, '0' is added to the end of the codeword
			navigateMap(decodingMap, node.getLeftChild(), new StringBuffer(characterCode).append('0')); 
		}
		// since the Huffman tree is a full tree, when there is no left child then there is also no right child
		// the character and its codeword is added to the HashMap
		else {
			decodingMap.put(node.getCharacter(), characterCode);
		}
		
		if(node.getRightChild()!=null) { //checks if the left child is empty
			// if not empty, it navigates on that node, '1' is added to the end of the codeword
			navigateMap(decodingMap, node.getRightChild(), new StringBuffer(characterCode).append('1'));
		}
		
		return decodingMap; // it returns the HashMap
	}
	
	/**
	 * Method that takes the name of a file and return a map of the characters contained in that file and their frequencies.
	 * @param fileName The name of the file
	 * @return A HashMap of the Characters in the file and their frequency
	 * @throws IOException
	 */
	public  Map<Character, Integer> createMapOfCharatcers(String fileName) throws IOException {
		Map<Character, Integer> mapOfCharacters = new HashMap<Character, Integer>();
		String line=null;		
		
		FileReader fileReader= new FileReader(fileName);
		BufferedReader bReader= new BufferedReader(fileReader);
		
		int intCharacter;
		char character;
		
		while((intCharacter=bReader.read())!=-1) { // reads each character of the text file. When it reaches the end of the file -1 is returned
			character=(char) intCharacter; // the read() method returns an integer, which is casted into a character
			if(mapOfCharacters.get(character)!=null) { // if the charcter already exists in the map, it's frequency is increased by 1
				Integer frequency = mapOfCharacters.get(character)+1;
				mapOfCharacters.put(character, frequency);
			}
			else { // or else a new entry is added to the map with frequency 1
				mapOfCharacters.put(character, 1);
			}
		}
		
		bReader.close();
			
		return mapOfCharacters;
	}
	
	
	/**
	 * Function that creates a binary representation of the text file by converting any character of it into its binary form,
	 * as determined by the Huffman algorithm
	 * @param fileName Name of the text file that will be compressed using the Huffman algorithm
	 * @param decodingMap Map that contains the Characters and their binary representation as determined by Huffman algorithm 
	 * @return A BufferedString that holds the binary representation of the text file
	 * @throws IOException
	 */
	public StringBuffer createBinaryRepresentation(String fileName,Map<Character, StringBuffer> decodingMap) throws IOException {
		StringBuffer binaryString=new StringBuffer();
		
		FileReader fReader= new FileReader(fileName);
		BufferedReader bReader= new BufferedReader(fReader);
		String line;
		
		while((line=bReader.readLine())!=null) {
			for(int i=0;i<line.length();i++) {
				binaryString.append(decodingMap.get(line.charAt(i)));
			}
			
			if(decodingMap.get('\n')!=null) // inserts the newline character into the binary string, as it gets ignored by the readLine() method
				binaryString.append(decodingMap.get('\n'));
		}
		
		
		bReader.close();
				
		return binaryString;
	}
	
	/**
	 * Writes the binary representation of the text file into a binary file, by converting each 32-bit long sequence into
	 * an integer and writing it as such on the binary file.
	 * @param binaryString The binary representation of the context of the text file.
	 * @return The name of the binary file
	 * @throws IOException
	 */
	public String writeBinaryFile(StringBuffer binaryString) throws IOException {
		
		// the number of extra zeros needed to be added at the end of binary string,so that it can be correctly decoded from integer to binary
		int padNumber=32-(binaryString.length()%32); 
		
		
		for(int i =0;i<padNumber;i++)
			binaryString.append('0');
		
		String outputFileName="test.dat";
		DataOutputStream dStream = new DataOutputStream(new FileOutputStream(outputFileName));
		
		//first integer in the binary file represents the number of zeros added
		dStream.writeInt(padNumber);
		int number;
		
		int startIndex=0;
		int endIndex=32;
		
		// the binary string is written into the binary file in groups of 32-characters long, which are converted to integers
		for(int i=0;i<binaryString.length()/32;i++) {
			// needs to be converted first to long, as parseInt() doesn't consider the sign bit
			number=(int)Long.parseLong(binaryString.substring(startIndex, endIndex), 2);
			dStream.writeInt(number);
			startIndex+=32;
			endIndex+=32;
		}
			
		dStream.close();
		
		return outputFileName;
		
	}
	
	/**
	 * Function that reads  the binary compressed file and converts the integers stored in it into binary sequence stored in a BufferedString 
	 * @param fileName The name of the compressed binary file that would be read
	 * @return A binary representation of the text file
	 * @throws IOException
	 */
	public String readBinaryFile(String fileName) throws IOException {
		DataInputStream iSream = new DataInputStream(new FileInputStream(fileName));
		StringBuffer codedString= new StringBuffer();
		int padNumber =iSream.readInt(); // the number of extra zeros is the first integer stored in the file. It's not part of the uncompressed file
		
		try {
			while(true) {
				String bp = Integer.toBinaryString(iSream.readInt());
				
				codedString.append(padFrontZeros(bp));
			}
		} catch (EOFException e) { //it keeps reading and converting to binary until it encounters the EOF character
			iSream.close();
		}
		
		return codedString.substring(0, codedString.length()-padNumber); // the extra zeros specified by the padNumber are removed from the Binary sequence
		
	}
	
	/**
	 * Function that adds the zeros in front of the binary representation of a number, which are removed by the toBinaryString() method because they have no meaning during the conversion.
	 * add the zeros to the front of the binary representation because by default, =java trim off all the zero in teh front as they have no meaning.
	 * @param bp String which contains the binary form of a integer stored in the binary file
	 * @return String which contains the correct numbers of zeros, so that it matches the binary representation determined by Huffman algorithm
	 */
	private String padFrontZeros(String bp){
		if(bp.length()==32)
			return bp;
		StringBuffer buf = new StringBuffer();
		int extraLength = 32-bp.length();
		for(int i=1; i<=extraLength; i++){
			buf.append('0');
		} 
		buf = buf.append(bp);
		return buf.toString();
	}
	
	/**
	 * Function that recreates the text file based on the binary representation.
	 * @param root The root of the Huffman tree
	 * @param codedString StringBuffer that contains the binary representation of the text file
	 * @param decodingMap HashMap containing the characters and their binary representation as determined by Huffman algorithm
	 * @throws IOException
	 */
	public void createDecompressedFile(HuffmanEntry root, String codedString) throws IOException {
		FileWriter writer = new FileWriter("DecompressedFile.txt");
		HuffmanEntry node= root;
				
		for(int i=0;i<codedString.length();i++) {
			// checks if we arrived at a leaf. The Huffman tree is a full-tree, so if the left child has no child of its own, so does the right
			if(node.getCharacter()==null) { 
				if(codedString.charAt(i)=='0') {
					node=node.getLeftChild();
				}
				else {
					node=node.getRightChild();
				}
			}
			
			if(node.getCharacter()!=null) {
				writer.write(node.getCharacter());
				node=root;
				}
		}
		
		writer.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		HuffmanCode code1 = new HuffmanCode();
		Map<Character, Integer> mapOfCharacters = code1.createMapOfCharatcers("test.txt");
		Map<Character, StringBuffer> decodingMap;
		HuffmanEntry root;

		root=code1.createHuffmanCode(mapOfCharacters);
		decodingMap=code1.createDecodingTable(root);
		StringBuffer b=code1.createBinaryRepresentation("test.txt", decodingMap);
		code1.writeBinaryFile(b);
		String c=code1.readBinaryFile("test.dat");
		code1.createDecompressedFile(root, c);
		

	}
	
}
