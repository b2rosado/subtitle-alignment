package ist.alignerzilla.domain;

import ist.alignerzilla.utils.SentenceSplitter;

import java.util.ArrayList;
import java.util.TreeMap;

public class Subtitle {
	private TreeMap<Double, Sentence> sentences = new TreeMap<Double, Sentence>(); // KEY = startTime with 0.25 precision
	private SentenceSplitter spliter;
	private String specialPunctiationCases;
	
	public Subtitle(String spc){
		this.specialPunctiationCases = spc;
		this.spliter = new SentenceSplitter(specialPunctiationCases);
	}

	public void addText(String text, double startTime, double endTime) {
		double tBefore = startTime;
		double tAfter = endTime;
		int cBefore = 0;
		
		for(String str : spliter.split(text)){
			int cAfter = text.length() - cBefore;
			double tStart = calculateNewTime(tBefore, tAfter, cBefore, cAfter);
			cBefore += str.length();
			cAfter -= str.length();
			double tEnd = calculateNewTime(tBefore, tAfter, cBefore, cAfter);

			Sentence s = new Sentence(str.replaceAll("\\s+$", ""), tStart, tEnd);
			addSentence(s);
		}
	}

	private double calculateNewTime(double tBefore, double tAfter, int cBefore, int cAfter){
		//Tiedemann approach: tnew = tbefore + cbefore * (tafter-tbefore)/(cbefore+cafter)
		return tBefore + (double)(cBefore * (double)((double)(tAfter-tBefore)/(double)(cBefore+cAfter)));
	}
	
	private void addSentence(Sentence s){
		Sentence lastSentence = getLastSentence();
		
		if(lastSentence != null && lastSentence.isIncomplete() && s.continuesSentence())
			lastSentence.concatenate(s);
		else{
			s.setIndex(sentences.size());
			sentences.put(s.getTimeKey(), s);
		}
	}
	
	public ArrayList<Sentence> getSentences() {
		return new ArrayList<Sentence>(sentences.values());
	}
	
	public int getNumSentences(){
		return sentences.size();
	}
	
	public Sentence getSentenceByTime(double time){
		Double lower = sentences.floorKey(time);
		Double upper = sentences.ceilingKey(time);
		
		if(lower == null)
			return sentences.get(upper);
		else if(upper == null)
			return sentences.get(lower);
		else if(Math.abs(time - lower) <= Math.abs(time - upper))
			return sentences.get(lower);
		else
			return sentences.get(upper);
	}
	
	public Sentence getSentenceByIndex(int index){
		if(index >= 0 && index < sentences.size())
			return getSentences().get(index);
		else
			return null;
	}
	
	private Sentence getLastSentence(){
		return sentences.isEmpty() ? null : sentences.get(sentences.lastKey());
	}
	
	@Override
	public String toString() {
		String str = new String();
		for(Sentence s : sentences.values()){
			str += s+"\n";
		}
		return str;
	}
}
