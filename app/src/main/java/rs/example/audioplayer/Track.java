package rs.example.audioplayer;

public class Track implements Comparable<Track>{
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
        this.trackLength = formatTime(trackLength);
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
        this.trackLength = formatTime(trackLength);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String formatTime(String rawTime){
        int ms = Integer.parseInt(rawTime);
        int min = ms/60000; //get amount of minutes first
        ms = ms - (min*60000); //subtract those minutes from ms
        int sec = ms/1000; //get amount of seconds from result of above operation
        //build the strings
        String minString;
        String secString;
        if(min < 10) {
            minString = "0" + min;  //result: 0N
        } else if (min >= 10 && min < 99) {
            minString = "" + min;   //result: NN
        } else {
            minString = "99";       //result: 99
        }
        if(sec < 10) {
            secString = "0" + sec;  //result: 0N
        } else if (sec >= 10 && sec < 99) {
            secString = "" + sec;   //result: NN
        } else {
            secString = "99";       //result: 99
        }

        String result = minString + ":" + secString;

        return result;
    }


    @Override
    public int compareTo(Track compTrack) {
        return this.artist.compareTo(compTrack.getArtist());
    }
}