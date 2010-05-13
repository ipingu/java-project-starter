package com.clairiot.module;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.clairiot.domain.Picture;
import com.clairiot.exception.ResourceAccessException;

public class PictureContentProvider {

	private static final Logger log = Logger.getLogger(PictureContentProvider.class);
	
	private FileManager fileManager;
	
	public byte[] getContent(Picture picture) throws ResourceAccessException {
		if (picture == null)
			throw new IllegalArgumentException("No picture provided");
		
		String pathToFile = picture.getPath();
		
		byte[] content = fileManager.readContentFromBinaryFile(pathToFile);
		
		return content;
	}
	
	public boolean saveContent(InputStream input, String name) {
		try {
			return fileManager.copy(input, name);
		} catch (ResourceAccessException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	// injection
	
	public FileManager getFileManager() {
		return fileManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}
	
}
