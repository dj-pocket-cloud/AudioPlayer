package rs.example.audioplayer;

import android.support.v7.widget.RecyclerView;

public class Track implements Comparable<Track>{
    private String trackName;
    private String artist;
    private int trackStart;
    private int trackEnd;
    private String trackLength;
    private int rawTrackLength;
    private String album;
    private String path;
    private int imgId;
    RecyclerView.ViewHolder viewHolder;

    public Track(){

    }

    public Track(String trackName, String artist, int trackStart, int trackEnd, String album, String path, int imgId){
        this.trackName = trackName;
        this.artist = artist;
        this.album = album;
        this.path = path;
        this.imgId = imgId;
        this.trackStart = trackStart;
        this.trackEnd = trackEnd;
        trackLength = String.valueOf(trackEnd-trackStart);
        rawTrackLength = trackEnd-trackStart;
        trackLength = formatTime(String.valueOf(rawTrackLength));
    }

    public Track(String trackName, String artist, String trackLength, String album, String path, int imgId) {
        this.trackName = trackName;
        this.artist = artist;
        this.trackLength = formatTime(trackLength);
        this.album = album;
        this.path = path;
        this.imgId = imgId;
        trackStart = 0;
        trackEnd = Integer.parseInt(trackLength);

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

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getTrackStart() {
        return trackStart;
    }

    public int getTrackEnd() {
        return trackEnd;
    }

    public int getRawTrackLength(){
        return rawTrackLength;
    }

    private String formatTime(String rawTime){
        int ms = Integer.parseInt(rawTime);
        rawTrackLength = ms;
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

    public String toString() {
        return trackName
                + "`" + artist
                + "`" + album
                + "`" + path
                + "`" + imgId
                + "`" + trackStart
                + "`" + trackEnd;
    }


    @Override
    public int compareTo(Track compTrack) {
        return this.artist.compareTo(compTrack.getArtist());
    }
}
