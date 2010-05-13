package com.clairiot.module;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.AssertThrows;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clairiot.domain.Picture;
import com.clairiot.exception.ResourceAccessException;
import com.clairiot.module.PictureContentProvider;

@ContextConfiguration(locations={"classpath:applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PictureContentProviderTest {

	private PictureContentProvider provider;

	@Before
	public void setUp() {
		this.provider = new PictureContentProvider();
	}
	
	@Test
	public void getContentFromNullPicture() {
		new AssertThrows(IllegalArgumentException.class) {
			
			@Override
			public void test() throws Exception {
				provider.getContent(null);
				
			}
		};
	}
	
	@Test
	public void getContentFromPictureWithNoFile() {
		new AssertThrows(ResourceAccessException.class) {
			
			@Override
			public void test() throws Exception {
				provider.getContent(new Picture());
			}
		};
	}
	
	@Test
	public void getContentFromPictureWithExistingFile() {
		Picture picture = new Picture();
		picture.setPath("src/test/resources/files/picture.jpg");
		
		byte[] content;
		try {
			content = provider.getContent(picture);
			assertNotSame(0, content.length);
		} catch (ResourceAccessException e) {
			fail();
		}
	}
	
	@Test
	public void getContentFromPictureWithNonExistingFile() {
		new AssertThrows(ResourceAccessException.class) {
			
			@Override
			public void test() throws Exception {
				Picture picture = new Picture("src/test/resources/files/no-file-like-that.jpg");
				provider.getContent(picture);
				
			}
		};
	}
	
}
