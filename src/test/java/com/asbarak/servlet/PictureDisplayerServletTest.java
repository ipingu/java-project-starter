package com.asbarak.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public class PictureDisplayerServletTest 
	extends AbstractTransactionalDataSourceSpringContextTests {

	private PictureDisplayerServlet pictureDisplayerServlet;
	
	public PictureDisplayerServletTest() {
		this.pictureDisplayerServlet = (PictureDisplayerServlet) getApplicationContext()
			.getBean("pictureDisplayerServlet");
	
		assertNotNull("servlet is not injected", pictureDisplayerServlet);
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
	public void testGetExistingPicture() {		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("id", "1");
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		try {
			this.pictureDisplayerServlet.handleRequest(request, response);
		} catch (ServletException e) {
			fail();
			e.printStackTrace();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
		
		assertEquals(200, response.getStatus());
		assertTrue(response.getContentLength() > 0);
		assertEquals("image/jpg", response.getContentType());
	}
	
	@Test
	public void testGetNonExistingPicture() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("id", "42");
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		try {
			this.pictureDisplayerServlet.handleRequest(request, response);
		} catch (ServletException e) {
			fail();
			e.printStackTrace();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
		
		assertEquals(404, response.getStatus());
	}
	
	@Test
	public void testGetNoResource() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		try {
			this.pictureDisplayerServlet.handleRequest(request, response);
		} catch (ServletException e) {
			fail();
			e.printStackTrace();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
		
		assertEquals(404, response.getStatus());
	}
	
}
