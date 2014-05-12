package com.geeko.photocollageproject;

public class Data {
	private int imageId;
	private String title;
	private String sub;

	public Data(int imageId, String title, String sub) {
		this.imageId = imageId;
		this.title = title;
		this.sub = sub;
	}

	public int getImageId() {
		return this.imageId;
	}

	public String getTitle() {
		return this.title;
	}

	public String getsub() {
		return this.sub;
	}

}
