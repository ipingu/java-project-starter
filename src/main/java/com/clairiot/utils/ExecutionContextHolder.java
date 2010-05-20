package com.clairiot.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openid4java.util.HttpClientFactory;
import org.openid4java.util.ProxyProperties;

public class ExecutionContextHolder {
	
	private String configurationPath = "configuration.properties"; // default path
	private final Properties configuration = new Properties();
	private static final Logger LOG = Logger.getLogger(ExecutionContextHolder.class);
	private static final boolean DEBUG = LOG.isDebugEnabled();
	
	public ExecutionContextHolder() {
		InputStream stream = ExecutionContextHolder.class.getResourceAsStream(configurationPath);
		
		if (stream != null) {
			try {
				configuration.load(stream);
				LOG.info("Load configuration from " + configurationPath + " file");
			} catch (IOException e) {
				LOG.warn("Cannot read configuration from " + configurationPath + " file");
			}
		} else {
			LOG.warn("Configuration file " + configurationPath + " does not exist");
		}
	}
	
	public void configureProxy() {
		LOG.info("Proxy configuration for system");
		
		setSystemProperty("http.proxyHost");
		setSystemProperty("http.proxyPort");
		setSystemProperty("http.proxyUser");
		setSystemProperty("http.proxyPassword", true);
		
		
		
		LOG.info("Proxy configuration for OpenId4Java");
		
		ProxyProperties proxyProps = new ProxyProperties();
		proxyProps.setProxyHostName(getProperty("http.proxyHost"));
		proxyProps.setProxyPort(Integer.parseInt(getProperty("http.proxyPort")));
		proxyProps.setUserName(getProperty("http.proxyUser"));
		proxyProps.setPassword(getProperty("http.proxyPassword"));
		
		
		HttpClientFactory.setProxyProperties(proxyProps);
	}
	
	/**
	 * Seems to not work.
	 */
	public void proxyAuthentication() {
		Authenticator customAuth = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				if (DEBUG) LOG.debug("Authenticator : ask for password authentication");
				
				return new PasswordAuthentication(
					getProperty("http.proxyUser"),
					getProperty("http.proxyPassword").toCharArray()
				);
			}
			
		};
		
		// register this authenticator on security manager
		LOG.info("Network connection authenticator registered");
		Authenticator.setDefault(customAuth);
	}

	private void setSystemProperty(String key) {
		setSystemProperty(key, false);
	}
	
	private String getProperty(String key) {
		return configuration.getProperty(key);
	}

	private void setSystemProperty(String key, boolean cryptValue) {
		final String value = getProperty(key);
		
		if (value != null) {
			System.setProperty(key, value);
			
			LOG.info("Set system property " + key + " to " + 
				(cryptValue ? value.replaceAll(".", "*") : value));
			
		} else {
			LOG.warn("Cannot set system property " + key + " : not found in configuration file");
		}
	}

	public String getConfigurationPath() {
		return configurationPath;
	}

	public void setConfigurationPath(String configurationPath) {
		this.configurationPath = configurationPath;
	}


}
