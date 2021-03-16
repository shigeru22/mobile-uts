package id.ac.umn.uts_27018;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String artist;
    private String album;
    private int artResource;
    private int songResource;

    public Song(String title, String artist, String album, int albumArt, int songResource) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.artResource = albumArt;
        this.songResource = songResource;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public int getArtResource() { return artResource; }
    public int getSongResource() { return songResource; }

    public void setAlbum(String album) { this.album = album; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setTitle(String title) { this.title = title; }
    public void setArtResource(int artResource) { this.artResource = artResource; }
    public void setSongResource(int songResource) { this.songResource = songResource; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(artist);
        sb.append(" - ");
        sb.append(title);

        return sb.toString();
    }
}
