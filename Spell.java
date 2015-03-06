package spell;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import spell.Trie.Node;

public class Spell implements SpellCorrector{

	private final static int alph = 26; // alphabet length
	private final int charDiff = 97; // difference between a char and it's value (0-25)
	private test words;

	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		words = new test();
		File file = new File(dictionaryFileName);
		
		Scanner scan = new Scanner(file);
		
		while(scan.hasNext())
			words.add(scan.next());
		
		scan.close();
	}

	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
		if(words.find(inputWord)!= null)
			return inputWord.toLowerCase();
		else{
			ArrayList<String> possibles = new ArrayList<String>();
			
			possibles.addAll(deletion(inputWord));
			possibles.addAll(transposition(inputWord));
			possibles.addAll(alteration(inputWord));
			possibles.addAll(insertion(inputWord));
			
			String similar = new String();
			int high = 0;
			
			for(int i = 0; i < possibles.size(); i++){
				Node n = words.find(possibles.get(i));
				if(n != null){
					if(n.getValue() > high){
						similar = possibles.get(i);
						high = n.getValue(); 
					}else if(n.getValue() == high){
						if(similar.compareTo(possibles.get(i)) > 0){
							similar = possibles.get(i);
							high = n.getValue(); 
						}
					}
				}
			}
			
			if(high == 0){
				ArrayList<String> possibles2 = new ArrayList<String>();
				
				for(int i = 0; i < possibles.size(); i++){
					possibles2.addAll(deletion(possibles.get(i)));
					possibles2.addAll(transposition(possibles.get(i)));
					possibles2.addAll(alteration(possibles.get(i)));
					possibles2.addAll(insertion(possibles.get(i)));				
				}		
			
				for(int i = 0; i < possibles2.size(); i++){
					Node n = words.find(possibles2.get(i));
					if(n != null){
						if(n.getValue() > high){
							similar = possibles2.get(i);
							high = n.getValue(); 
						}else if(n.getValue() == high){
							if(similar.compareTo(possibles2.get(i)) > 0){
								similar = possibles2.get(i);
								high = n.getValue(); 
							}
						}
					}
				}
			
			}
			if(high == 0)
				throw new NoSimilarWordFoundException(); 			
			
			return similar;
		}
	}
	
	private ArrayList<String> deletion(String s){
		ArrayList<String> possibles = new ArrayList<String>();
		for(int i = 0; i < s.length(); i++)
		    possibles.add(s.substring(0, i) + s.substring(i+1));			
		return possibles;
	}
	
	private ArrayList<String> transposition(String s){
		ArrayList<String> possibles = new ArrayList<String>();
		StringBuilder str;
		char temp;
		for(int i = 0; i < s.length() - 1; i++){
			str = new StringBuilder(s);
			temp = str.charAt(i);
			str.setCharAt(i, str.charAt(i+1));
			str.setCharAt(i+1,temp);
		    possibles.add(str.toString());
		}
		return possibles;
	}
	
	private ArrayList<String> alteration(String s){
		ArrayList<String> possibles = new ArrayList<String>();
		StringBuilder str;
		for(int i = 0; i < s.length(); i++){
			for(int j = 0; j < alph; j++){
				str = new StringBuilder(s);
				if(str.charAt(i) != (char)(j+charDiff)){
					str.setCharAt(i, (char)(j+charDiff));
				    possibles.add(str.toString());
				}
			}
		}
		return possibles;
	}
	
	private ArrayList<String> insertion(String s){
		ArrayList<String> possibles = new ArrayList<String>();
		StringBuilder str;
		for(int i = 0; i <= s.length(); i++){
			for(int j = 0; j < alph; j++){
				str = new StringBuilder(s);
				if(i != s.length())
					str.insert(i,(char)(j+charDiff));
				else
					str.append((char)(j+charDiff));
			    possibles.add(str.toString());
			}
		}
		return possibles;
	}

	public static void main(String args[]) throws IOException, NoSimilarWordFoundException{
		Spell spell = new Spell();
		spell.useDictionary("words.txt");
		System.out.println(spell.words.toString());
	}
}
