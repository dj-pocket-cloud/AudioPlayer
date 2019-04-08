package rs.example.audioplayer;

public class Track {
    private String trackName;
    private String artist;
    private String trackLength;
    private String album;
    private String path;

    public Track(){

    }

    public Track(String trackName, String artist, String trackLength, String album, String path) {
        this.trackName = trackName;
        this.artist = artist;
        this.trackLength = trackLength;
        this.album = album;
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(String trackLength) {
        this.trackLength = trackLength;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
