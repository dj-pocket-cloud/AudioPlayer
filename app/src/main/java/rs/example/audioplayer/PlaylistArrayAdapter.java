package rs.example.audioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlaylistArrayAdapter extends ArrayAdapter<Playlist> {
    private static class ViewHolder{
        ImageView playlistImgView;
        TextView playlistTextView;
    }
    public PlaylistArrayAdapter(Context context, List<Playlist> option) {
        super(context, -1, option);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Playlist playlist = getItem(position);

        PlaylistArrayAdapter.ViewHolder viewHolder;

        if (convertView == null) { //create new viewholder
            viewHolder = new PlaylistArrayAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.option_list_item, parent, false);
            viewHolder.playlistImgView = (ImageView) convertView.findViewById(R.id.optionImgView);
            viewHolder.playlistTextView = (TextView) convertView.findViewById(R.id.optionTextView);
            convertView.setTag(viewHolder);
        }
        else { // reuse existing ViewHolder
            viewHolder = (PlaylistArrayAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.playlistImgView.setImageResource(playlist.getImgId());

        viewHolder.playlistTextView.setText(playlist.getPlaylistName());


        return convertView; // return completed list item to display
    }
}
