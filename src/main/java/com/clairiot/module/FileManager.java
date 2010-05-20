package com.clairiot.module;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

import com.clairiot.exception.ResourceAccessException;

public class FileManager {
	
	private static final Logger log = Logger.getLogger(FileManager.class);
	
	private String parentPath;
	
	public FileManager() { }
	
	public boolean copy(InputStream input, String path) throws ResourceAccessException {
		if (input == null)
			throw new IllegalArgumentException("Cannot copy anyting : inputstream is null");
		
		File dest = getFullPath(path, false);
		
		if (log.isDebugEnabled()) log.debug("Copying content to file " + dest.getAbsolutePath());
		
		FileWriter writer = null;
		InputStreamReader reader = null;
		try {
			if (! dest.createNewFile())
				return false; 
			
			writer = new FileWriter(dest);
			reader = new InputStreamReader(input);
			
			int len = 0;
			char[] buffer = new char[512];
			while ((len = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, len);
			}
			
			writer.flush();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			
			return false;
		} finally {
			try {
				if (writer != null) writer.close();
				if (reader != null) reader.close();
				
			} catch (IOException e) {
				// not such a big deal ... anyway, what can I do ?
			}
		}
	}

	public String readStringFromFile(String path) throws FileNotFoundException, ResourceAccessException {
		File file = getFullPath(path, true);
			
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
		
		throw new ResourceAccessException("File cannot be found or is not readable");
		
	}
	
	public byte[] readContentFromBinaryFile(String path) throws ResourceAccessException {
		File completePath = getFullPath(path, true);

		try {
			ImageInputStream imageIn = ImageIO.createImageInputStream(completePath);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			int character = 0;
			while ((character = imageIn.read()) != -1) {
				output.write(character);
			}
			
			return output.toByteArray();
			
		} catch (IOException e) {
			if (log.isDebugEnabled())
				log.debug("IOException occured : " + e.getMessage());
			
			throw new ResourceAccessException("IOException : " + e.getMessage());
		}
	}
	

	private File getFullPath(String path, boolean fileExists) throws ResourceAccessException {
		if (path != null && path.length() > 0) {
			String pathFromRoot = this.parentPath + File.separatorChar + path;
			File dest = new File(pathFromRoot);
			
			if (fileExists && dest.exists() || (! fileExists && ! dest.exists()))
				return dest;
		}
		
		throw new ResourceAccessException("File does not exist or is not readable");
	}


	// injection
	
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

}
