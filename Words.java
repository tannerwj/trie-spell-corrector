package spell;

import java.util.*;

public class Words implements Trie {
	
	private int wordCount = 0;
	private int nodeCount = 1; // starts with 1 node - root
	private final static int alph = 26; // alphabet length
	private final int charDiff = 97; // difference between a char and it's value (0-25)

	private Node root = new Node();
	
	private Queue<String> words = new LinkedList<String>();
	private Queue<Number> wordFrequency =  new LinkedList<Number>();
	
	@Override
	public void add(String word) {
		word = word.toLowerCase();		
		Node current = root;				
		for(int i = 0; i < word.length(); i++){
			if(current.children[word.charAt(i) - charDiff] == null){
				current.children[word.charAt(i) - charDiff] = new Node();
				nodeCount++;
			}
			current = current.children[word.charAt(i) - charDiff];
		}		
		if(current.frequency++ == 0)
			wordCount++;		
	}

	@Override
	public Node find(String word) {
		word = word.toLowerCase();		
		Node current = root;		
		for(int i = 0; i < word.length(); i++){
			if(current.children[word.charAt(i) - charDiff] == null){
				return null;
			}
			current = current.children[word.charAt(i) - charDiff];
		}		
		if(current.getValue() > 0)
			return current;
		else 
			return null;
	}

	@Override
	public int getWordCount() {
		return  wordCount;
	}

	@Override
	public int getNodeCount() {
		return nodeCount;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		words.clear();
		wordFrequency.clear();
		iterate(root,"");
		while(!words.isEmpty()){
			sb.append(words.remove() + " " + wordFrequency.remove() + "\n");
		}
		return sb.toString();		
	}
	
	public void iterate(Node n, String s){		
		if(n.getValue() > 0){
			words.add(s);
			wordFrequency.add(n.getValue());			
		}
		for(int i = 0; i < alph; i++)
			if(n.children[i] != null)
				iterate(n.children[i], s + (char)(i + charDiff));
	}
	
	@Override
	public int hashCode(){
		return wordCount ^ nodeCount;		
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this)
			return true;
		
		if(o == null)
			return false;
		
		if (getClass() != o.getClass())
			return false;
		
		Words w = (Words)o;
		
		if(wordCount != w.wordCount)
			return false;
		
		if(nodeCount != w.nodeCount)
			return false;
		
		if(!(this.toString().equals(w.toString())))
			return false;

		return true;
	}
	
	public static class Node implements Trie.Node{
		
		public int frequency = 0;
		public Node[] children = new Node[alph];

		@Override
		public int getValue() {
			return frequency;
		}
		
	}

}
