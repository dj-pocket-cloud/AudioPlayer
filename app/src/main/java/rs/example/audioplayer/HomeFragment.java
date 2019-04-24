package rs.example.audioplayer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button testButton;
    private List<Track> playlistList = new ArrayList<>();
    private ListView playlistListView;
    private TrackArrayAdapter playlistArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the layout you want to display in First Fragment
        View view = inflater.inflate(R.layout.home_menu_fragment, container, false);

        testButton = (Button) view.findViewById(R.id.testButton);
        testButton.setOnClickListener(testClickListener);
        playlistListView = (ListView) view.findViewById(R.id.playlistsListView);
        playlistArrayAdapter = new TrackArrayAdapter(getContext(),playlistList);
        playlistListView.setAdapter(playlistArrayAdapter);

        //todo: find all saved playlists and display them in a displayAdapter
        //todo: clicking a playlist will load the playlistbrowser with the filename as the argument
        //todo: also add "all tracks" at the top of this list, which will pass a special argument to load all tracks
        //todo: finally, longclick a playlist to delete it

        playlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                PlaylistBrowser pb = new PlaylistBrowser();
                pb.setPlaylistName(null /*todo: get playlist here*/);
                fragmentTransaction.replace(R.id.fragment_container, pb).addToBackStack(null).commit();
            }
        });

        playlistListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return false;
            }
        });

        return view;

    }

    View.OnClickListener testClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            PlaylistBrowser pb = new PlaylistBrowser();
            pb.setPlaylistName(null);
            fragmentTransaction.replace(R.id.fragment_container, pb).addToBackStack(null).commit();

            //fragmentTransaction.addToBackStack(null);
            //fragmentTransaction.commit();
        }
    };
}
