package com.dub.spring.controller;

import java.util.List;

import com.dub.spring.entities.Photo;

public class PhotoWebServiceList {
 
	private List<Photo> photos;

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}   
	
}
