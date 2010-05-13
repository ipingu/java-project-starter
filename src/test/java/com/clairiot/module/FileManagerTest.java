package com.clairiot.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Properties;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AssertThrows;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clairiot.exception.ResourceAccessException;
import com.clairiot.module.FileManager;

@ContextConfiguration(locations={"classpath:applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class FileManagerTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private String example = "src/test/resources/files/filemanager.test";
	
	@Autowired(required=true)
	private FileManager manager;
	
	@Test
	public void checkIfParentPathIsCorrectlySet() {
		Properties props = new Properties();
		try {
			props.load(FileManager.class.getResourceAsStream("classpath:environment-test.properties"));
			assertEquals("storage.files.parentPath", props.getProperty("/home/erik/tmp/storage"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void saveIncorrectContent() {
		new AssertThrows(IllegalArgumentException.class) {
			@Override
			public void test() throws Exception {
				manager.copy(null, folder.getRoot().getPath() + "/destination.file");
			}
		};
	}
	
	@Test
	public void saveEmptyContent() throws ResourceAccessException {
		InputStream input = new StringBufferInputStream("");
		assertTrue(manager.copy(input, folder.getRoot().getPath() + "/destination.file"));
	}
	
	@Test
	public void saveContentToIncorrectDestination() throws ResourceAccessException {
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
		} catch (ResourceAccessException e) {
			fail();
		}
	}
	
	@Test
	public void readStringFromNonExistingFile() {
		new AssertThrows(ResourceAccessException.class) {
			@Override
			public void test() throws Exception {
				manager.readStringFromFile(folder.getRoot().getPath() + "/woot.file");
			}
		};
	}
	
	@Test
	public void saveContentAndReadIt() throws ResourceAccessException, FileNotFoundException {
		String randomText = UUID.randomUUID().toString();
		String destination = folder.getRoot().getPath() + "/saveContentAndReadIt.test";
		
		InputStream input = new StringBufferInputStream(randomText);
		assertTrue(manager.copy(input, destination));
		
		assertEquals(randomText, manager.readStringFromFile(destination));
	}
}
