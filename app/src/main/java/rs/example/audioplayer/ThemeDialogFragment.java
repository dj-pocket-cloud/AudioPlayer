package rs.example.audioplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ThemeDialogFragment extends DialogFragment {
    private RadioGroup radioGroup;
    private RadioButton light;
    private RadioButton dark;
    private boolean theme; //true = light, false = dark

    // create an AlertDialog and return it
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        final View pokemonDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.theme_select_fragment, null);
        builder.setView(pokemonDialogView); // add GUI to dialog

        // set the AlertDialog's message
        builder.setTitle("Set Theme");

        radioGroup = (RadioGroup) pokemonDialogView.findViewById(
                R.id.themeRadioGroup);
        light = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_light);
        dark = (RadioButton) pokemonDialogView.findViewById(
                R.id.radio_dark);

        View.OnClickListener lightClickListener = new View.OnClickListener(){
            public void onClick(View v) {
                //set light theme
                theme = true;
            }
        };

        View.OnClickListener darkClickListener = new View.OnClickListener(){
            public void onClick(View v) {
                //set dark theme
                theme = false;
            }
        };

        light.setOnClickListener(lightClickListener);
        dark.setOnClickListener(darkClickListener);


        // add set theme button that applies the selected theme
        builder.setPositiveButton("Set Theme",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (theme) {
                            //todo: set light theme across the entire program
                        } else {
                            //todo: set dark theme across the entire program
                        }
                    }
                }
        );

        return builder.create(); // return dialog
    }
}
