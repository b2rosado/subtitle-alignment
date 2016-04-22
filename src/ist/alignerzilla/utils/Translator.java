package ist.alignerzilla.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class Translator {
	private HashMap<String, String> map = new HashMap<String, String>();
	
	public Translator(String dictionaryFile){
		final String translatorFile = dictionaryFile;
		final String encoding = StandardCharsets.UTF_8.name();
		final int ORIGINAL = 0;
		final int TRANSLATION = 1;
		
		try {
			Scanner scanner = new Scanner(new File(translatorFile), encoding);
			while (scanner.hasNextLine()) {
				String[] word = scanner.nextLine().split("\t");
				map.put(word[ORIGINAL].replaceAll("[^\\w\\s]", ""), word[TRANSLATION]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not read file \""+ translatorFile +"\".");
		}
	}
	
	public String translateWord(String word){
		return map.get(word.toLowerCase());
	}
	
	public String translateSentence(String sentence){
		sentence = sentence.replaceAll("(\\w+)-(\\w+)","$1 $2").replaceAll("[^\\w\\s]", "");
		String translation = new String();
		String translatedWord = new String();
		for(String word : sentence.split("\\s")){
			translatedWord = translateWord(word);
			translation += translatedWord == null? word+" " : translatedWord+" ";
		}
		return translation;
	}
	
}
