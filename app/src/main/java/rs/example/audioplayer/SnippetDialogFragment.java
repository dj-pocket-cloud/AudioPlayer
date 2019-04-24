package rs.example.audioplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;


public class SnippetDialogFragment extends DialogFragment {
    private CrystalRangeSeekbar timeBar;
    private TextView startTextView;
    private TextView endTextView;
    private TextInputLayout title;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create the dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View snippetWidthDialogView = getActivity().getLayoutInflater().inflate(R.layout.snippet_menu, null);
        builder.setView(snippetWidthDialogView); // add GUI to dialog

        startTextView = (TextView) snippetWidthDialogView.findViewById(R.id.startTextView);
        endTextView = (TextView) snippetWidthDialogView.findViewById(R.id.endTextView);
        title = (TextInputLayout) snippetWidthDialogView.findViewById(R.id.titleTextInputLayout);

        // set the AlertDialog's message
        builder.setTitle("Snippet Maker");

        ControlsFragment controlsFragment = (ControlsFragment) getFragmentManager().findFragmentById(R.id.controls);


        timeBar = (CrystalRangeSeekbar) snippetWidthDialogView.findViewById(R.id.timeSeekbar);
        timeBar.setMaxValue(controlsFragment.getTracklength());
        timeBar.setMinValue(0);
        timeBar.setMaxStartValue(controlsFragment.getSnipEnd());
        timeBar.setMinStartValue(controlsFragment.getSnipBegin());
        timeBar.apply();
        timeBar.setOnRangeSeekbarChangeListener(timeChanged);

        startTextView.setText(formatTime(controlsFragment.getSnipBegin()));
        endTextView.setText(formatTime(controlsFragment.getSnipEnd()));

        // add Set Line Width Button
        builder.setPositiveButton("Make Snippet",
                (dialog, id) -> {
                    controlsFragment.setSnipBegin(timeBar.getSelectedMinValue().intValue());
                    controlsFragment.setSnipEnd(timeBar.getSelectedMaxValue().intValue());
                    controlsFragment.createSnippet(title.getEditText().getText().toString());
                }
        );

        return builder.create(); // return dialog
    }


    private OnRangeSeekbarChangeListener timeChanged =
            new OnRangeSeekbarChangeListener() {

                @Override
                public void valueChanged(Number minValue, Number maxValue) {
                    startTextView.setText(formatTime(minValue.intValue()));
                    endTextView.setText(formatTime(maxValue.intValue()));
                }
            };
    // OnSeekBarChangeListener for the SeekBar in the width dialog
    //private final CrystalRangeSeekbar.OnDragListener timeChanged =
            //new CrystalRangeSeekbar.OnDragListener() {

                //@Override
                //public boolean onDrag(View v, DragEvent event) {
                //    return true;
                //}
            //};

    private String formatTime(int ms){
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

}
