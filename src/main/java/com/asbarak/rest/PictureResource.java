package com.asbarak.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asbarak.domain.Picture;
import com.asbarak.exception.ResourceAccessException;
import com.asbarak.module.PictureContentProvider;
import com.asbarak.module.PicturePublisher;
import com.sun.jersey.impl.ResponseBuilderImpl;
import com.sun.jersey.spi.inject.Inject;

@Path("/picture/{id}")
@Component("pictureRestResource")
@Scope("singleton")
public class PictureResource {

	private static final Logger log = Logger.getLogger(PictureResource.class);
	
	@Inject
	private PictureContentProvider pictureContentProvider;
	
	@Inject
	private PicturePublisher picturePublisher;
	
	@GET
	@Produces("image/jpg")
	public Response doGet(@PathParam("id") String id) {
		try {
			return Response.temporaryRedirect(new URI("/displayPicture?id=" + id)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
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
