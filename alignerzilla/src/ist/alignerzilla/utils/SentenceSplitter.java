package ist.alignerzilla.utils;

import java.util.ArrayList;

/**
 * Class SentenceSplitter - A utility class used to split
 * an input text in sentences
 * */
public class SentenceSplitter {
	private String specialCases;
	
	/**
	 * Class constructor
	 * @param specialPunctuationCases	list of cases special cases to ignore
	 * 									when splitting the text.	
	 * 									Expected format: "case1|case2|case3|case4".
	 * 									Example: "Mr|mr|Mrs|Dr|Av"
	 * */
	public SentenceSplitter(String specialPunctuationCases){
		this.specialCases = specialPunctuationCases;
	}
	
	/**
	 * Receives a text and splits it in different sentences.
	 * @param	text	the text to split in sentences.
	 * @return	an ArrayList<String> containing the different sentences found in <b>text</b>
	 * */
	public ArrayList<String> split(String text){
		ArrayList<String> sentences = new ArrayList<String>();
		String substring = new String();
		// Regex that matches with something followed by punctuation
		String punctuationRegex = ".*[\\.!?]+ ";
		// Regex that matches with special punctuation cases
		String exceptionsRegex = ".*("+specialCases+"|\\.[A-Z]). ";
		// Variable to store where each sentence begins
		int beginIndex = 0;
		
		for(int i=0; i<text.length(); i++){
			substring += text.charAt(i);
			if(substring.matches(punctuationRegex) && !substring.matches(exceptionsRegex)){
				for(i++; i<text.length() && text.charAt(i) == '.'; i++){
					// Read trough ellipsis (...)
				}	
				sentences.add(text.substring(beginIndex, i));
				beginIndex = i;
			}
		}
		sentences.add(text.substring(beginIndex, text.length()));
		
		return sentences;
	}	
}
