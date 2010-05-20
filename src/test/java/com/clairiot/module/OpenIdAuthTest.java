package com.clairiot.module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clairiot.utils.ExecutionContextHolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-test.xml"})
public class OpenIdAuthTest {
	
	private ConsumerManager manager;
	private String userSupplied = "ipingu.myopenid.com";

	@Autowired
	private ExecutionContextHolder context;

	
	@Before
	public void setUp() throws ConsumerException {
		context.configureProxy();
		context.proxyAuthentication();
		this.manager = new ConsumerManager();
	}

	@Test
	public void checkConnectionByPingingGoogle___thisTestSucks___asThisMethodName() throws MalformedURLException {
		URL google = new URL("http", "www.google.com", "/");
		
		try {
			final InputStream input = google.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void discover() {
		try {
			manager.discover(userSupplied);
		} catch (DiscoveryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
