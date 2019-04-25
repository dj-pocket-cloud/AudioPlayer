package rs.example.audioplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

public class ConfirmDeleteFragment extends DialogFragment {

    private HomeFragment hf;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.confirm_delete_fragment, null);
        builder.setView(view); // add GUI to dialog

        //add to playlist button
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        hf.deletePlaylist();
                    }
                }
        );

        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close dialog
            }
        });

        return builder.create(); // return dialog
    }

    public void setHomeFragmentReference(HomeFragment hf) {
        this.hf = hf;
    }
}
