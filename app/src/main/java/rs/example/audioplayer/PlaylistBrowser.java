package rs.example.audioplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistBrowser extends Fragment {
    private List<Track> trackList = new ArrayList<>();
    private ListView trackListView;
    private TrackArrayAdapter trackArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the layout you want to display in First Fragment
        View view = inflater.inflate(R.layout.playlist_fragment, container, false);

        trackListView = (ListView) view.findViewById(R.id.tracksListView);
        trackArrayAdapter = new TrackArrayAdapter(getContext(),trackList);
        trackListView.setAdapter(trackArrayAdapter);

        setTestTracks();
        return view;

    }

    private void setTestTracks(){
        for(int i = 0; i < 10; i++){
            Track newTrack = new Track("Hello" + i,"nope", "12112");
            trackList.add(newTrack);
        }

    }


}
