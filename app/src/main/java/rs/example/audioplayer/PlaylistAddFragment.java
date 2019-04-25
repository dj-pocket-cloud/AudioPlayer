package rs.example.audioplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

public class PlaylistAddFragment extends DialogFragment {

    private EditText playlistEditText;
    private String playlistTitle;
    private PlaylistBrowser pb;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.playlist_add_fragment, null);
        builder.setView(view); // add GUI to dialog

        playlistEditText = (EditText)view.findViewById(R.id.playlistEditText);

        //add to playlist button
        builder.setPositiveButton("Add to playlist",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        playlistTitle = playlistEditText.getText().toString();
                        if (!playlistTitle.isEmpty()) {
                            pb.setPlaylistName(playlistTitle);
                            pb.writePlaylist();
                        }
                    }
                }
        );

        return builder.create(); // return dialog
    }

    public void setPlaylistBrowserReference(PlaylistBrowser pb) {
        this.pb = pb;
    }
}
