package com.clairiot.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AssertThrows;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clairiot.exception.ResourceAccessException;

@ContextConfiguration(locations={"classpath:applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class FileManagerTest {
	
	private String example = "src/test/resources/files/filemanager.test";
	
	private File folder;
	
	private static final Logger log = Logger.getLogger(FileManagerTest.class);
	
	@Autowired(required=true)
	private FileManager manager;

	@Before
	public void createTemporaryFolder() throws IOException {		
		this.folder = new File(manager.getParentPath() + File.separatorChar + "test-junks");
		
		boolean success = folder.mkdir();
		
		log.debug("Folder " + folder.getAbsolutePath() + " is " + (success ? "created" : "not created"));
	}
	
	@After
	public void deleteTemporaryFolder() {
		deleteFilesAndFolders(this.folder);
		
		boolean success = folder.delete();
		log.debug("Folder " + folder.getAbsolutePath() + " is " + (success ? "deleted" : "not deleted"));
	}

	private void deleteFilesAndFolders(File toDelete) {
		if (toDelete.isDirectory()) {
			final File[] files = toDelete.listFiles();
			for (File file : files) {
				deleteFilesAndFolders(file);
			}
		}
		toDelete.delete();
	}
		
	@Test
	public void checkIfParentPathIsCorrectlySet() {
		Properties props = new Properties();
		try {
			props.load(FileManager.class.getResourceAsStream("classpath:environment-test.properties"));
			assertNotNull(props.getProperty("storage.files.parentPath"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void saveIncorrectContent() {
		new AssertThrows(IllegalArgumentException.class) {
			@Override
			public void test() throws Exception {
				manager.copy(null, folder.getPath() + "/destination.file");
			}
		};
	}
	
	@Test
	public void saveEmptyContent() throws ResourceAccessException {
		InputStream input = new StringBufferInputStream("");
		assertTrue(manager.copy(input, folder.getPath() + "/destination.file"));
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
				manager.readStringFromFile(folder.getPath() + "/woot.file");
			}
		};
	}
	
	@Test
	public void saveContentAndReadIt() throws ResourceAccessException, FileNotFoundException {
		String randomText = UUID.randomUUID().toString();
		String destination = folder.getPath() + "/saveContentAndReadIt.test";
		
		InputStream input = new StringBufferInputStream(randomText);
		assertTrue(manager.copy(input, destination));
		
		assertEquals(randomText, manager.readStringFromFile(destination));
	}
}
