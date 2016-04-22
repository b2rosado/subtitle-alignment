package ist.alignerzilla;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import ist.alignerzilla.domain.SubtitlePair;
import ist.alignerzilla.io.ConfigLoader;
import ist.alignerzilla.io.SubtitleReader;

/**
 * Alternative main class for Alignerzilla.<br>
 * Running this class will print all the sentences from the subtitles present in <b>list.txt</b> file.<br>
 * Will store them in <b>sentences/</b> directory.
 * */
public class SubtitlePrinter {
	public static void main(String[] args){
		ConfigLoader config = new ConfigLoader();
		SubtitleReader reader = new SubtitleReader(config.getSpecialPunctiationCases());
		
		while(reader.hasMorePairs()){
			SubtitlePair pair = reader.readPair();
			
			PrintWriter writer;
			try {
				writer = new PrintWriter("sentences/"+pair.getSourceName()+".txt", "UTF-8");
				writer.println(pair.getSource());
				writer.close();
				
				writer = new PrintWriter("sentences/"+pair.getTargetName()+".txt", "UTF-8");
				writer.println(pair.getTarget());
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				System.err.println("ERROR: Couldn't save output of "+pair.getName());
			}	
		}
	}
}
