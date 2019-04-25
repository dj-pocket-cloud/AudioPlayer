package rs.example.audioplayer;

public class Playlist {
    private String playlistName;
    private int imgId;

    public Playlist() {

    }

    public Playlist(String playlistName, int imgId) {
        this.playlistName = playlistName;
        this.imgId = imgId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

}
