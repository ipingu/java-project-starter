package com.clairiot.module;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.clairiot.domain.Picture;
import com.clairiot.model.PicturesList;
import com.clairiot.module.PicturePublisher;
import com.clairiot.persistence.PictureDAO;

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
	
	@Test
	public void getListOfPictures() {
		final Picture pic1 = new Picture("pix1");
		final Picture pic2 = new Picture("pix2");
		pic1.setId(new Long(1));
		pic2.setId(new Long(2));
		
		when(mockDAO.findAll()).thenReturn(new ArrayList(){{
			add(pic1);
			add(pic2);
		}});
		
		PicturesList list = publisher.getListOfPictures();
		assertEquals(2, list.pictures.size());
		assertEquals("1", list.pictures.get(0));
		assertEquals("2", list.pictures.get(1));
	}

}
