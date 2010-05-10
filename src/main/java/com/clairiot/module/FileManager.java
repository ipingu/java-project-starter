package com.clairiot.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileManager {

	public boolean copy(InputStream input, String path) {
		if (input == null)
			throw new IllegalArgumentException("Inputstream to copy is null");
		
		File dest = (path != null && path.length() > 0) ? new File(path) : null;
		
		if (dest == null || dest.exists())
			return false;
		
		
		try {
			if (! dest.createNewFile())
				return false; 
			
			FileWriter writer = new FileWriter(dest);
			InputStreamReader reader = new InputStreamReader(input);
			
			char[] buffer = new char[512];
			while (reader.read(buffer) != -1) {
				writer.write(buffer);
			}
			
			writer.flush();
			writer.close();
			reader.close();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			
			return false;
		}
	}

	public Object readStringFromFile(String path) throws FileNotFoundException {
		File file = null;
		
		if (path != null && path.length() > 0) {
			file = new File(path);
			
			if (file.exists() && file.canRead()) {
				StringBuilder builder = new StringBuilder();
				
				FileReader reader = new FileReader(file);
				BufferedReader buffer = new BufferedReader(reader);
				
				String line = null;
				try {
					while ((line = buffer.readLine()) != null) {
						builder.append(line);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return builder.toString();
			}
		}
		
		throw new FileNotFoundException("No file identified by " + path);
	}

}
