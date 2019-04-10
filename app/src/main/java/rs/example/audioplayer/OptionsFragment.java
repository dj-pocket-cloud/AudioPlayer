package rs.example.audioplayer;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OptionsFragment extends Fragment {

    private ImageButton backButton;
    private List<Option> optionList = new ArrayList<>();
    private ListView optionListView;
    private OptionArrayAdapter optionArrayAdapter;
    private final String THEMESTRING = "Theme";
    private final String THEMEDESCRIPTIONSTRING = "Set the display theme to use";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the layout you want to display in First Fragment
        View view = inflater.inflate(R.layout.options_fragment, container, false);

        optionListView = (ListView) view.findViewById(R.id.optionsListView);
        optionArrayAdapter = new OptionArrayAdapter(getContext(), optionList);
        optionListView.setAdapter(optionArrayAdapter);

        backButton = (ImageButton) view.findViewById(R.id.optionsBackButton);
        backButton.setOnClickListener(optionsBackButtonClickListener);

        loadOptions();

        //gets track item when clicked
        optionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get the option then launch the corresponding method
                Option selectedOption = (Option) parent.getItemAtPosition(position);
                switch(selectedOption.getOptionName()) {
                    case THEMESTRING:
                        executeTheme();
                        break;
                }

            }
        });

        return view;

    }

    View.OnClickListener optionsBackButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ControlsFragment.setOptionsOpen(false);
            try {
                getFragmentManager().popBackStack();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    private void loadOptions() {
        optionList.clear();

        //theme option
        Option theme = new Option();
        theme.setOptionName(THEMESTRING);
        theme.setOptionDescription(THEMEDESCRIPTIONSTRING);
        theme.setImgId(R.drawable.ic_gradient_black_24dp);

        optionList.add(theme);
    }

    private void executeTheme() {
        ThemeDialogFragment td = new ThemeDialogFragment();
        td.show(getFragmentManager(), "theme dialog");
    }
}
