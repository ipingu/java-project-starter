package com.clairiot.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PicturesList {
	
	public final List<String> pictures = new ArrayList<String>();

}
