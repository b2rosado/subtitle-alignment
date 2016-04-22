package ist.alignerzilla.domain;

import java.util.ArrayList;

public class SentenceGroup extends ArrayList<Sentence>{
	private static final long serialVersionUID = 921156608290771803L;

	public SentenceGroup(){
		super();
	}
	
	public SentenceGroup(Sentence sentence) {
		super();
		add(sentence);
	}

	public Sentence getFirst() {
		return isEmpty() ? null : get(0);
	}

	public Sentence getLast() {
		return isEmpty() ? null : get(size()-1);
	}
	
	public double getStartTime(){
		return isEmpty()? 0 : getFirst().getStartTime();
	}
	
	public double getEndTime(){
		return isEmpty()? 0 : getLast().getEndTime();
	}
	
	public double getDuration(){
		return getEndTime() - getStartTime();
	}
	
	@Override
	public String toString() {
		String str = new String();
		for(Sentence s: this)
			str += s+" ";
		return str.replaceAll("\\s+", " ").replaceAll("\\s+$", "");
	}

}
