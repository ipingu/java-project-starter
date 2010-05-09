package com.asbarak.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.HttpRequestHandler;

import com.asbarak.domain.Picture;
import com.asbarak.exception.ResourceAccessException;
import com.asbarak.module.PictureContentProvider;
import com.asbarak.module.PicturePublisher;

public class PictureDisplayerServlet implements HttpRequestHandler {

	private static final Logger log = Logger.getLogger(PictureDisplayerServlet.class);
	
	private PictureContentProvider pictureContentProvider;
	
	private PicturePublisher picturePublisher;

	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// get picture by id
		String pictureId = request.getParameter("id");
		Picture picture = null;
		
		if (pictureId != null)
			picture = picturePublisher.getPictureById(Integer.parseInt(pictureId));
		
		// 404 error if the picture was not found
		if (picture == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		try {
			byte[] pictureAsBytes = pictureContentProvider.getContent(picture);
			response.getOutputStream().write(pictureAsBytes);
			response.getOutputStream().flush();
			
			response.setContentType("image/jpg");
			response.setContentLength((int) picture.length());
			response.setHeader("Content-disposition", "inline; filename=avatar_from_web.jpeg");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ResourceAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
