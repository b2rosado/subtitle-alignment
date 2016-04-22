package ist.alignerzilla.domain;

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
