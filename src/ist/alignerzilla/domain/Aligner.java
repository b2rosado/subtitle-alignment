package ist.alignerzilla.domain;

import ist.alignerzilla.io.ConfigLoader;
import ist.alignerzilla.utils.SentenceComparator;

import java.util.ArrayList;

public class Aligner {
	private boolean TIME_ONLY;
	private double TIME_THRESHOLD;						//maximum time diff accepted
	private double DISTANCE_THRESHOLD;					//maximum distance accepted
	private boolean PRINT_CONFIDENCE;
	private boolean USE_DICTIONARY;
	private String DICTIONARY_FILE;
	private final double MIN_SENTENCE_DURATION = 0.7;	//minimum time for a sentence to be used in N:N alignments
	private final double CONFIDENCE_DISTANCE = 0.35;	//distance that gives a good confidence
	
	private int indexOffset = 0;
	
	private Subtitle source;
	private Subtitle target;
	private ArrayList<Match> alignments = new ArrayList<Match>();
	
	public Aligner(SubtitlePair pair, ConfigLoader config) {
		this.source = pair.getSource();
		this.target = pair.getTarget();
		this.TIME_ONLY = config.useTimeOnly();
		this.TIME_THRESHOLD = config.getTimeThreshold();
		this.DISTANCE_THRESHOLD = config.getDistanceThreshold();
		this.USE_DICTIONARY = config.useDictionary();
		this.DICTIONARY_FILE = config.getDictionaryFile();
		this.PRINT_CONFIDENCE = config.printConfidence();
	}

	public void align() {
		for(int i=0; i<source.getNumSentences(); i++){
			Sentence s = source.getSentenceByIndex(i);
			Match m = new Match(s); 
			m = findMatchUsingTime(m);
			if(!TIME_ONLY && !m.isValid(TIME_THRESHOLD/2.0))
				m = findMatchUsingSimilarity(m);
			else
				indexOffset =  m.getTargetIndex() - m.getSourceIndex();			
			i = m.getSourceSentences().getLast().getIndex();			
			alignments.add(m);
		}
	}

	private Match findMatchUsingTime(Match m) {
		final String DEBUG_STRING = "%$$&";
		m.addTargetSentence(target.getSentenceByTime(m.getSourceStart()));
		if(m.getSourceSentences().getFirst().getText().contains(DEBUG_STRING)){
			System.out.println("\n!!!!!\t!!!!!\t!!!!!");
			System.out.printf("\"%s\" (%.02f) -- \"%s\" (%.02f)\n", m.getSourceSentences(), m.getSourceDuration(), m.getTargetSentences(), m.getTargetDuration());
		}
		
		if(sourceTooShort(m)){			//N:1 alignment
			Sentence otherSourceSentence = source.getSentenceByIndex(m.getSourceSentences().getLast().getIndex()+1);
			//System.out.printf("Source too short +++ \"%s\" (%.02f)\n",otherSourceSentence.toString(), otherSourceSentence.getDuration());
			if(otherSourceSentence != null && otherSourceSentence.getDuration() > MIN_SENTENCE_DURATION)
				m.addSourceSentence(otherSourceSentence);
		}else if(targetTooShort(m)){	//1:N alignment
			Sentence otherTargetSentence = target.getSentenceByIndex(m.getTargetSentences().getLast().getIndex()+1);
			//System.out.printf("Target too short +++ \"%s\" (%.02f)\n",otherTargetSentence.toString(), otherTargetSentence.getDuration());
			if(otherTargetSentence != null && otherTargetSentence.getDuration() > MIN_SENTENCE_DURATION /*&& Math.abs(m.getSourceSentences().getDuration() - m.getTargetSentences().getDuration() - otherTargetSentence.getDuration()) < TIME_THRESHOLD*/)
				m.addTargetSentence(otherTargetSentence);
		}
		if(!m.isValid(TIME_THRESHOLD)){
			if(m.getSourceSentences().getFirst().getText().contains(DEBUG_STRING))
				System.out.printf("WAS RESET : %.02f\n",Math.abs(m.getSourceDuration()-m.getTargetDuration()));
			m.reset();
		}else{
			m.setConfidence((int)(100-(50/TIME_THRESHOLD)*Math.abs(m.getTargetDuration() - m.getSourceDuration())));
		}
		//System.out.println("< "+m);
		if(m.getSourceSentences().getFirst().getText().contains(DEBUG_STRING))
			System.out.println("!!!!!\t!!!!!\t!!!!!\n");
		return m;
	}
	
	private boolean sourceTooShort(Match m){
		return m.getTargetDuration() - m.getSourceDuration() > TIME_THRESHOLD;
	}
	
	private boolean targetTooShort(Match m){
		return m.getSourceDuration() - m.getTargetDuration() > TIME_THRESHOLD;
	}
	
	private Match findMatchUsingSimilarity(Match m) {		
		SentenceComparator sc = new SentenceComparator(USE_DICTIONARY, DICTIONARY_FILE);
		
		double currentDistance = sc.calculateDistance(m.getSourceSentences().getFirst() , m.getTargetSentences().getFirst());
		//System.out.printf("? %s --- %d\n",m, m.getConfidence());
		if(currentDistance < CONFIDENCE_DISTANCE){
			//intentionally left blank; nothing to do here
		}else{
			final int WINDOW = 4;
			final int CURRENT_INDEX = m.getSourceIndex() + indexOffset; 
			final int MIN = CURRENT_INDEX-WINDOW;
			final int MAX = CURRENT_INDEX+WINDOW;
			
			int matchIndex = -1;
			double bestDistance = 1.0f; //value between 0 - 1
			
			for(int i=Math.max(0, MIN); i<Math.min(target.getNumSentences(), MAX); i++){
				double distance = sc.calculateDistance(m.getSourceSentences().getFirst(), target.getSentenceByIndex(i));
				if(distance < bestDistance){
					bestDistance = distance;
					matchIndex = i;
				}
			}
			if(bestDistance > DISTANCE_THRESHOLD){	// if distance > 0.55, DISCARD
				m.clear();
				m.setConfidence((int)(bestDistance*100));
			}else if(Math.abs(currentDistance - bestDistance) > CONFIDENCE_DISTANCE/2.0f){	// if best distance is significantly better than the aligned found by time, USE IT
				m.clear();
				m.addTargetSentence(target.getSentenceByIndex(matchIndex));
				m.setConfidence((int)(100-bestDistance*100));
			}
		}
		//System.out.printf("> %s --- %d\n",m, m.getConfidence());
		return m;
	}
	
	public ArrayList<Match> getAlignments() {
		return alignments;
	}
	
	@Override
	public String toString() {
		String str = new String();
		for(Match a : alignments)
			str += a.toString() + (PRINT_CONFIDENCE ? " --- "+a.getConfidence()+"%": "") + "\n";
		//str += String.format("\n\nBy Similarity: %d out of %d : %.02f%%\n", bySimilarityCount, alignments.size(), 100*bySimilarityCount/(double)alignments.size());
		return str;
	}
}
