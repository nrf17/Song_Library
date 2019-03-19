package application;

/**
 * 
 * @author Nick Fasullo (nrf17)
 *
 */

public class Song implements Comparable<Song>{
	private String name;
	private String artist;
	private String album;
	private String year;
	
	public Song(String name, String artist) {
		this.name = name;
		this.artist = artist;
	}
	
	public String toString() {
		return name + " | " + artist;
	}
	
	public int compareTo(Song other) {
		int nameComparison = name.compareToIgnoreCase(other.getName());
		
		if(nameComparison != 0)
			return nameComparison;
		
		return artist.compareToIgnoreCase(other.getArtist());
	}
	
	public boolean equals(Object o) {
		if(o == null || (!(o instanceof Song)))
			return false;
		
		Song other = (Song) o;
		
		if(!name.equals(other.getName()))
			return false;
		
		if(!artist.equals(other.getArtist()))
			return false;
		
		return true;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
}
