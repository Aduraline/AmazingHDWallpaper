package amazing.hd.amazinghdwallpaper.picasa.model;

import java.io.Serializable;

public class Wallpaper implements Serializable {
	private static final long serialVersionUID = 1L;
	private String url, photographer;
	private int width, height;

	public Wallpaper(String photographer, String url, int width, int height) {
		this.photographer = photographer;
		this.url = url;
		this.width = width;
		this.height = height;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhotographer() {
		return photographer;
	}

	public void setPhotographer(String photographer) {
		this.photographer = photographer;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
