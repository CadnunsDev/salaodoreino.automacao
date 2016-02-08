package utils;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {
	public static String[] fileNamesOnFolder(String folderPath){
		ArrayList<String> files = new ArrayList<String>();
		File[] filesOnDirectory = new File(folderPath).listFiles();
		
		for (File file : filesOnDirectory) {
			if (file.isFile()) {
				files.add(file.getName());
			}
		}
		return (String[]) files.toArray(new String[files.size()]);
	}
}
