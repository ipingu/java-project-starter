package com.asbarak.rest;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asbarak.domain.Picture;
import com.asbarak.module.PictureContentProvider;
import com.asbarak.module.PicturePublisher;
import com.sun.jersey.spi.inject.Inject;

@Path("/picture")
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
	@Path("/{id}")
	public Response doGet(@PathParam("id") String id) {
		try {
			return Response.temporaryRedirect(new URI("./../displayPicture?id=" + id)).build();
		} catch (URISyntaxException e) {
			return Response.serverError().build();
		}
	}
	
	@POST
	@Produces("text/plain")
	public String uploadPicture(@Context HttpServletRequest request) {
		log.debug("erf");
		if (! ServletFileUpload.isMultipartContent(request))
			return "no multi";
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload uploader = new ServletFileUpload(factory);
		
		Picture picture = null;
		try {
			List<FileItem> items = uploader.parseRequest(request);
			
			for (FileItem item : items) {
				if (item.isFormField()) continue;
				
				log.debug("item = " + item.getName());
				try {
					item.write(new File("/home/erik/essai.jpg"));
					picture = picturePublisher.insertPicture("/home/erik/essai.jpg");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
		return picture != null ? "" + picture.getId() : "no upload!";
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