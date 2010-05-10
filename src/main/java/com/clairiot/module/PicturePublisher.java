package com.clairiot.module;

import org.apache.log4j.Logger;

import com.clairiot.domain.Picture;
import com.clairiot.persistence.PictureDAO;

public class PicturePublisher {
	
	private static final Logger log = Logger.getLogger(PicturePublisher.class);
	
	private PictureDAO dao;

	public PicturePublisher() { }
	
	public PicturePublisher(PictureDAO dao) {
		this.dao = dao;
	}

	public Picture getPictureById(int id) {
		Picture picture = dao.find(new Long(id));
		
		if (log.isDebugEnabled())
			log.debug("picture " + id + (picture == null ? " not" : "") + " found");
		
		return picture;
	}

	public int count() {
		return this.dao.findAll().size();
	}

	public Picture insertPicture(String path) {
		Picture picture = new Picture(path);
		return this.dao.save(picture);
	}

	// injection
	
	public void setDao(PictureDAO dao) {
		this.dao = dao;
	}
	
}
