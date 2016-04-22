package ist.alignerzilla.utils;

import ist.alignerzilla.domain.Sentence;

public class SentenceComparator {
	private final double LAMBDA = 0.6;
	private boolean useDictionary;
	private String dictionaryFile;
	
	public SentenceComparator(boolean useDictionary, String dictionaryFile){
		this.useDictionary = useDictionary;
		this.dictionaryFile = dictionaryFile;		
	}
	
	public double calculateDistance(Sentence s, Sentence t){
		if(s==null || t==null)
			return 1;
		String translation = s.toString();
		if(useDictionary){
			Translator translator = new Translator(dictionaryFile);
			translation = translator.translateSentence(s.toString());
		}
		
		String[] sentence1 =  translation.replaceAll("[\\.?!,-]", "").split("\\s");
		String[] sentence2 = t.toString().replaceAll("[\\.?!,-]", "").split("\\s");
		
		int wordCount = Math.max(sentence1.length, sentence2.length);
		int wordMatches = 0;
		
		for(String word1 : sentence1)
			for(String word2 : sentence2)
				if(wordsMatch(word1, word2)){
					wordMatches++;
					break;
				}
		
		//Relative Word Match Distance - less is better
		double wordMatchDistance = 1-Math.min(wordMatches/(float)wordCount, 1);
		
		//Relative Word Count Distance - less is better
		double wordCountDistance = 1-Math.min(sentence1.length, sentence2.length) / (double)Math.max(sentence1.length, sentence2.length);
		
		//Weighted average between last two metrics - less is better
		double distance = LAMBDA*Math.pow(wordMatchDistance,2)+(1-LAMBDA)*wordCountDistance;
		
		return distance;
	}
	
	private boolean wordsMatch(String w1, String w2){
		final double HALF_WORD_LENGHT = (w1.length()+1)/2;

		if(w1.equals(w2))
			return true;
		else
			return levenshteinDistance(w1, w2) < HALF_WORD_LENGHT; //at least half of the word is equal (lemma)
	}
	
	private int levenshteinDistance(String s1, String s2) {
		// degenerate cases
		if (s1.equals(s2))
			return 0;
		if (s1.length() == 0)
			return s2.length();
		if (s2.length() == 0)
			return s1.length();

		// create two work vectors of integer distances
		int[] v0 = new int[s2.length() + 1];
		int[] v1 = new int[s2.length() + 1];

		// initialize v0 (the previous row of distances)
		// this row is A[0][i]: edit distance for an empty s
		// the distance is just the number of characters to delete from t
		for (int i = 0; i < v0.length; i++)
			v0[i] = i;

		for (int i = 0; i < s1.length(); i++) {
			// calculate v1 (current row distances) from the previous row v0

			// first element of v1 is A[i+1][0]
			// edit distance is delete (i+1) chars from s to match empty t
			v1[0] = i + 1;

			// use formula to fill in the rest of the row
			for (int j = 0; j < s2.length(); j++) {
				int cost = (s1.charAt(i) == s2.charAt(j)) ? 0 : 1;
				v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j]
						+ cost);
			}

			// copy v1 (current row) to v0 (previous row) for next iteration
			for (int j = 0; j < v0.length; j++)
				v0[j] = v1[j];
		}

		return v1[s2.length()];
	}
}
