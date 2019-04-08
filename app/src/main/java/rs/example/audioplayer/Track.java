package rs.example.audioplayer;

public class Track {
    public String trackName;
    public String artist;
    public String trackLength;

    public Track(String trackName,String artist, String trackLength){
        this.artist = artist;
        this.trackName = trackName;
        this.trackLength = trackLength;
    }
}
