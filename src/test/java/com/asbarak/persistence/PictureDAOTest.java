package com.asbarak.persistence;

import java.io.FileInputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import com.asbarak.domain.Picture;

public class PictureDAOTest 
	extends AbstractTransactionalDataSourceSpringContextTests {

	private PictureDAO pictureDao;
	
	private static final Logger log = Logger.getLogger(PictureDAOTest.class);
	
	
	public PictureDAOTest() {
		this.pictureDao = (PictureDAO) getApplicationContext().getBean("pictureDAO");
		assertNotNull("dao is not found in app context", pictureDao);
	}

	@Override
	protected void onSetUpInTransaction() throws Exception {
		DataSource dataSource = jdbcTemplate.getDataSource();
		Connection con = DataSourceUtils.getConnection(dataSource);
		IDatabaseConnection dbUnitCon = new DatabaseConnection(con);
		
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		IDataSet dataSet = builder.build(
				new FileInputStream("src/test/resources/dbunit/PictureDAO.xml"));
		
		try {
			DatabaseOperation.REFRESH.execute(dbUnitCon, dataSet);
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}
	
	@Override
	protected String[] getConfigLocations() {
		return new String[] {"applicationContext-test.xml"};
	}

	@Test
	public void testFindAllPictures() {
		assertEquals(3, pictureDao.findAll().size());
	}
	
	@Test
	public void testFindExistingPicture() {
		assertNotNull(pictureDao.find(new Long(1)));
		assertNotNull(pictureDao.find(new Long(2)));
		assertNotNull(pictureDao.find(new Long(3)));
	}
	
	@Test
	public void testFindNonExistingPicture() {
		assertNull(pictureDao.find(new Long(42)));
		assertNull(pictureDao.find(new Long(0)));
	}
	
	@Test
	public void testSavingNullPicture() {
		assertNull(pictureDao.save(null));
	}
	
	@Test
	public void testSavingPicture() {
		Picture picture = new Picture("/home/erik/avatar.jpg");
		assertNotNull(pictureDao.save(picture));
		assertEquals(new Long(4), picture.getId());
	}
	
	// injection
	
	public void setPictureDAO(PictureDAO dao) {
		this.pictureDao = dao;
	}
}
