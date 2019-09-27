package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FileIn {
	
	public static String[][] getData(String file) {
		try {
			String row;
			ArrayList<String[]> data = new ArrayList<String[]>();
			BufferedReader csvReader = new BufferedReader(new FileReader(file));
			while ((row = csvReader.readLine()) != null) {
				data.add(row.split(","));
			}
			csvReader.close();
			return data.toArray(new String[data.size()][]);
		} catch(Exception e) {
			System.out.println("oops. Something happened.\n" + e.getMessage());
		}
		return null;
	}

}
