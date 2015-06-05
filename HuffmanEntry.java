package greedy;

/**
 * Class that integrates a Huffman entry. It stores a character and the frequency it appears on a given text file.
 * Each entry has also a left and right child, which are used in constructing the Huffman tree.
 * Setters and Getters are implemented for each member variable
 *
 */
public class HuffmanEntry implements Comparable<HuffmanEntry>{
	
	public Character character;
	public Integer frequency;
	HuffmanEntry leftChild;
	HuffmanEntry rightChild;
	
	public HuffmanEntry() {
		// TODO Auto-generated constructor stub
	}
	
	public HuffmanEntry(Character character,Integer frequency) {
		// TODO Auto-generated constructor stub
		this.character=character;
		this.frequency=frequency;
	}
	
	public void setCharacter(Character character) {
		this.character=character;
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public void setFrequency(Integer frequency) {
		this.frequency=frequency;
	}
	
	public Integer getFrequency() {
		return frequency;
	}
	
	public void setLeftChild(HuffmanEntry leftChild) {
		this.leftChild=leftChild;
	}
	
	public HuffmanEntry getLeftChild() {
		return leftChild;
	}
	
	public void setRightChild(HuffmanEntry rightChild) {
		this.rightChild=rightChild;
	}
	
	public HuffmanEntry getRightChild() {
		return rightChild;
	}
	
	@Override
	public int compareTo(HuffmanEntry o) {
		// TODO Auto-generated method stub
		return this.frequency.compareTo(o.frequency);
	}
	
}
