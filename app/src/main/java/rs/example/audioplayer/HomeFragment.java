package rs.example.audioplayer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class HomeFragment extends Fragment {

    Button testButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the layout you want to display in First Fragment
        View view = inflater.inflate(R.layout.home_menu_fragment, container, false);

        testButton = (Button) view.findViewById(R.id.testButton);
        testButton.setOnClickListener(testClickListener);

        return view;

    }

    View.OnClickListener testClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            PlaylistBrowser pb = new PlaylistBrowser();
            fragmentTransaction.replace(R.id.home_menu, pb);
                //provide the fragment ID of your first fragment which you have given in
                //fragment_layout_example.xml file in place of first argument
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };
}
