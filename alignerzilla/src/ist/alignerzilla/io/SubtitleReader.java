package ist.alignerzilla.io;

import ist.alignerzilla.domain.Subtitle;
import ist.alignerzilla.domain.SubtitlePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class SubtitleReader {
	private final String listFile = "list.txt";
	private ArrayList<SubtitlePair> pairsList = new ArrayList<SubtitlePair>(); 
	private int index = 0;
	private String specialPunctuationCases;

	/**
	 * Instantiates a Subtitle Reader<br>
	 * Reads "list.txt" file, that contains the names of the subtitles to align.
	 * */
	public SubtitleReader(String spc){
		this.specialPunctuationCases = spc;
		final String encoding = StandardCharsets.UTF_8.name();
		try {
			Scanner scanner = new Scanner(new File(listFile), encoding);
			String lastRead = null;

			while (scanner.hasNextLine()) {
				String subtitleFile = scanner.nextLine();

				if(isSubtitlePair(lastRead, subtitleFile)){
					pairsList.add(new SubtitlePair(lastRead, subtitleFile));
					lastRead = null;	//no need to verify if this subtitle has pair
				}else
					lastRead = subtitleFile;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not read file \""+ listFile +"\".");
		}
	}

	public boolean hasMorePairs(){
		return pairsList.size() > index;
	}

	public SubtitlePair readPair(){
		SubtitlePair pair = pairsList.get(index++);
		try{
			pair.setSubtitles(readSubtitle(pair.getSourceName()), readSubtitle(pair.getTargetName()));
		}catch(Exception e){
			System.err.println("ERROR: Impossible to read subtitle pair \"" + pair.getName() + "\"\n       "+e.getMessage());
		}
		return pair;
	}

	private Subtitle readSubtitle(String filename){
		final String encoding = StandardCharsets.UTF_8.name();
		Subtitle subtitle = new Subtitle(specialPunctuationCases);
		filename = "data/" + filename;

		try {
			Scanner scanner = new Scanner(new File(filename), encoding);
			int screenId=1;
			String line = new String();
			String text = new String();
			String timestampLine = new String();
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();

				if(line.matches("^[0-9]+$")){					// id line - new screen start
					screenId = Integer.parseInt(line);
					text = new String();
				}else if(line.matches(".+[0-9] --> [0-9].+"))	// timestamp line
					timestampLine = line;
				else if(line.matches("^$"))
					try{
						subtitle.addText(text, getAbsoluteStartTime(timestampLine), getAbsoluteEndTime(timestampLine));
					}catch(Exception e){
						scanner.close();
						throw new RuntimeException("Incorrect format at subtitle screen " + screenId);
					}
				else
					text += line;	
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could not read file \""+ filename +"\".");
		}
		return subtitle;
	}

	private double getAbsoluteStartTime(String line){
		final int T1_START = 0;
		final int T1_END = 12;
		return timestampToAbsolute(line.substring(T1_START, T1_END));
	}

	private double getAbsoluteEndTime(String line){
		final int T2_START= 17;
		final int T2_END = 29;
		return timestampToAbsolute(line.substring(T2_START, T2_END));
	}


	//e.g. converts 00:01:10,270 to 70.27
	private double timestampToAbsolute(String ts){
		String token[] = ts.replace(',', '.').split(":");
		return Integer.parseInt(token[0])*3600 + Integer.parseInt(token[1])*60 + Double.parseDouble(token[2]);
	}

	/**
	 * Checks if subtitle names make a pair:<br>
	 * Example:<br>
	 * 		"abeautifulMind-EN.srt", "abeautifulMind-PT.srt" = TRUE<br>
	 * 		"abeautifulMind-EN.srt", "americanBeauty-PT.srt" = FALSE
	 * @param	sub1 	the first subtitle filename
	 * @param	sub2 	the second subtitle filename
	 * @return	true if <b>sub1</b> movie name equals <b>sub2</b> movie name.
	 * 			false otherwise.
	 */
	private boolean isSubtitlePair(String sub1, String sub2){		
		if(sub1 == null || sub2 == null)
			return false;

		// must be equal until the "-" that separates movie name from language
		for(int i=0; i<sub1.length() && i<sub2.length() &&  (sub1.charAt(i)!='-' || sub2.charAt(i)!='-'); i++)
			if(sub1.charAt(i) != sub2.charAt(i))
				return false;

		return true;
	}

}
