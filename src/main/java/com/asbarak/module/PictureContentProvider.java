package com.asbarak.module;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

import com.asbarak.domain.Picture;
import com.asbarak.exception.ResourceAccessException;

public class PictureContentProvider {

	private static final Logger log = Logger.getLogger(PictureContentProvider.class);
	
	public byte[] getContent(Picture picture) throws ResourceAccessException {
		if (picture == null)
			throw new IllegalArgumentException("No picture provided");
		
		File file = picture.getFile();
		
		if (isNotValidFile(file)) {
			throw new ResourceAccessException("File does not exist or is not readable");
		}

		try {
			ImageInputStream imageIn = ImageIO.createImageInputStream(file);
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

	private boolean isNotValidFile(File file) {
		return file == null 
			|| ! file.exists() 
			|| file.isDirectory() 
			|| ! file.canRead();
	}
	
}
