package ist.alignerzilla.domain;

public class Sentence {
	private double startTime;
	private double endTime;
	private String text;
	private int index;
	
	public Sentence(String text, double startTime, double endTime){
		this.startTime = startTime;
		this.endTime = endTime;
		this.text = text;
	}
	
	public void setIndex(final int index){
		this.index = index;
	}
	public int getIndex(){
		return index;
	}
	
	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}
	
	public String getText(){
		return text;
	}
	
	public Double getTimeKey(){
		//rounds number to .5 precision (e.g. 3.260 = 3.0 & 3.680 = 3.5)
		return (double) Math.round(startTime * 4) / 4.0;
	}

	public void concatenate(Sentence s) {
		this.endTime = s.endTime;
		this.text = this.text.replaceAll(">>", " ") + s.text;
		this.text = this.text.replaceAll("(\\.+ )\\.+", "$1");

	}

	public boolean continuesSentence(){
		//must start with lower case OR "I" OR be an acronym
		return text.matches("(^[a-z\\.\" ]+|^I|^[A-Z]{3,}).+");
	}
	
	public boolean isIncomplete() {
		return text.contains(">>");
	}
	
	public double getDuration() {
		return getEndTime() - getStartTime();
	}
	
	@Override
	public String toString() {
		//return String.format("%.2f --> %.2f | %s\n", startTime, endTime, text.replaceAll(">>\\s*\\.*", " "));
		return text.replaceAll(">>\\s*\\.*", " ");
	}
}
