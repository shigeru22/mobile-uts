package id.ac.umn.uts_27018;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String artist;
    private String album;
    private String uri;
    private String artData;

    public Song(String title, String artist, String album, String uri) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.uri = uri;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getURI() { return uri; }
    // public String getAlbumArtPath() { return artData; }

    public void setAlbum(String album) { this.album = album; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setTitle(String title) { this.title = title; }
    public void setURI(String uri) { this.uri = uri; }
    // public void setAlbumArtPath(String artPath) { this.artData = artData; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(artist);
        sb.append(" - ");
        sb.append(title);

        return sb.toString();
    }
}
