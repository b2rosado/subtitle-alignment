package ist.alignerzilla.domain;

/**
 * Represents the subtitle pair to align.<br>
 * It is composed by two subtitle files – source and target.<br>
 * To produce correct alignments it is expected that the two subtitles are from the same movie.<br>
 * In order to obtain translations as the output, the subtitles must be of different languages.<br>
 * To obtain paraphrases and synonyms, the subtitle pair must be of the same language.
 */
public class SubtitlePair{
	private String sourceName;
	private String targetName;
	private Subtitle source = null;
	private Subtitle target = null;
	
	public SubtitlePair(String sourceName, String targetName) {
		this.sourceName = sourceName;
		this.targetName = targetName;
	}
	
	public String getSourceName() 		{	return sourceName;	}
	public String getTargetName() 		{	return targetName;	}
	public String getName()				{	return sourceName.replaceAll("-.*", "");}
	public Subtitle getSource()			{	return source;		}
	public Subtitle getTarget()			{	return target;		}
	
	public void setSubtitles(Subtitle s, Subtitle t) {
		source = s;
		target = t;
	}
	
	public boolean isValid() {
		return source != null && target != null;
	}
}
