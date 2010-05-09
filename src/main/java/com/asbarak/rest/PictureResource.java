package com.asbarak.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asbarak.module.PictureContentProvider;
import com.asbarak.module.PicturePublisher;

@Path("/picture/{id}")
@Component
@Scope("request")
public class PictureResource {

	private static final Logger log = Logger.getLogger(PictureResource.class);
	
	private PictureContentProvider pictureContentProvider;
	
	private PicturePublisher picturePublisher;
	
	@GET
	@Produces("text/plain")
	public String doGet(@PathParam("id") String id) {
		log.debug("hello publisher " + picturePublisher);
		return id;
	}
	
	// injection

	public void setPictureContentProvider(
			PictureContentProvider pictureContentProvider) {
		this.pictureContentProvider = pictureContentProvider;
	}

	public void setPicturePublisher(PicturePublisher picturePublisher) {
		this.picturePublisher = picturePublisher;
	}
	
	
	
	
}
