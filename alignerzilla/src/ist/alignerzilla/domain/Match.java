package ist.alignerzilla.domain;


public class Match {
	private SentenceGroup sourceSentences = new SentenceGroup();
	private SentenceGroup targetSentences = new SentenceGroup();
	private int confidence = 0;
	
	public Match(Sentence s){
		sourceSentences.add(s);
	}
	
	public void addSourceSentence(Sentence s){
		sourceSentences.add(s);
	}
	
	public void addTargetSentence(Sentence t){
		targetSentences.add(t);
	}
	
	public void setConfidence(int value){
		confidence = value;
	}
	
	public int getSourceIndex(){
		return sourceSentences.getLast().getIndex();
	}
	
	public int getTargetIndex(){
		return targetSentences.getLast().getIndex();
	}
	
	public SentenceGroup getSourceSentences(){
		return sourceSentences;
	}
	
	public SentenceGroup getTargetSentences(){
		return targetSentences;
	}

	public double getSourceDuration(){
		return sourceSentences.getDuration();
	}
	
	public double getTargetDuration(){
		return targetSentences.getDuration();
	}
	
	public double getSourceStart(){
		return sourceSentences.getStartTime();
	}
	
	public double getSourceEnd() {
		return sourceSentences.getEndTime();
	}
	
	public double getTargetStart(){
		return targetSentences.getStartTime();
	}
	
	public double getTargetEnd(){
		return targetSentences.getEndTime();
	}
	
	public int getConfidence(){
		return confidence;
	}
	
	public void reset() {
		sourceSentences = new SentenceGroup(sourceSentences.getFirst());
		targetSentences = new SentenceGroup(targetSentences.getFirst());
		confidence = 0;
	}
	
	public void clear(){
		targetSentences.clear();
		confidence = 0;
	}
	
	public boolean found() {
		return !targetSentences.isEmpty();
	}
	
	public boolean isValid(double threshold){
		return Math.abs(getSourceDuration() - getTargetDuration()) < threshold;
	}
	
	@Override
	public String toString() {
		return String.format("%-85s --- %s", sourceSentences.toString(), targetSentences.toString());
	}


}
