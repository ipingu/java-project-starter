package com.asbarak.domain;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Picture {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String path;
	
	public Picture() { }
		
	public Picture(String path) {
		this.path = path;
	}

	public File getFile() {
		return new File(path);
	}

	public long length() {
		return new File(path).length();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return this.id + "@" + this.path;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Picture) {
			Picture that = (Picture) obj;
			return this.path.equals(that.path);
		}
		
		return false;
	}
	
}
