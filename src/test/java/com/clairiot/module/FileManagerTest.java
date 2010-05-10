package com.clairiot.module;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.UUID;

import org.junit.Test;
import org.springframework.test.AssertThrows;

public class FileManagerTest {
	
	private String example = "src/test/resources/files/filemanager.test";
	
	private FileManager manager = new FileManager();

	@Test
	public void saveIncorrectContent() {
		new AssertThrows(IllegalArgumentException.class) {
			@Override
			public void test() throws Exception {
				manager.copy(null, "destination.file");
			}
		};
	}
	
	@Test
	public void saveEmptyContent() {
		InputStream input = new StringBufferInputStream("");
		assertTrue(manager.copy(input, "destination.file"));
	}
	
	@Test
	public void saveContentToIncorrectDestination() {
		InputStream input = new StringBufferInputStream("");
		assertFalse(manager.copy(input, ""));
		assertFalse(manager.copy(input, null));
	}
	
	@Test
	public void readStringFromExistingFile() {
		try {
			assertEquals("Line from filemanager.test", manager.readStringFromFile(example));
		} catch (FileNotFoundException e) {
			fail();
		}
	}
	
	@Test
	public void readStringFromNonExistingFile() {
		new AssertThrows(FileNotFoundException.class) {
			@Override
			public void test() throws Exception {
				manager.readStringFromFile("woot.file");
			}
		};
	}
	
	@Test
	public void saveContentAndReadIt() throws FileNotFoundException {
		String randomText = UUID.randomUUID().toString();
		String destination = "saveContentAndReadIt.test";
		
		InputStream input = new StringBufferInputStream(randomText);
		assertTrue(manager.copy(input, destination));
		
		assertEquals(randomText, manager.readStringFromFile(destination));
	}
}
