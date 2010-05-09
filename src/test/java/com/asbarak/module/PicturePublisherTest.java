package com.asbarak.module;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.asbarak.domain.Picture;
import com.asbarak.persistence.PictureDAO;

@RunWith(MockitoJUnitRunner.class)
public class PicturePublisherTest {
	
	@Mock private PictureDAO mockDAO;
	private PicturePublisher publisher;
	
	@Before
	public void setUp() {
		this.publisher = new PicturePublisher(mockDAO);
	}
	
	@Test
	public void getExistingPicture() {
		when(mockDAO.find(new Long(1))).thenReturn(new Picture());
		assertNotNull(publisher.getPictureById(1));
	}
	
	@Test
	public void getUnexistingPicture() {
		when(mockDAO.find(new Long(42))).thenReturn(null);
		assertNull(publisher.getPictureById(42));
	}
	
	@Test
	public void addNewPicture() {
		Picture picture = new Picture("/home/erik/avatar.jpg");
		
		when(mockDAO.save(picture)).thenReturn(picture);
		assertNotNull(publisher.insertPicture(picture.getPath()));
	}
	
	@Test
	public void addNullPicture() {
		assertNull(publisher.insertPicture(null));
		assertNull(publisher.insertPicture(""));
	}

}
