package rs.example.audioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TrackArrayAdapter extends ArrayAdapter<Track> {
    private static class ViewHolder{
        ImageView trackImgView;
        TextView trackTextView;
        TextView artistTextView;
        TextView lengthTextView;
    }
    public TrackArrayAdapter(Context context, List<Track> playlist) {
        super(context, -1, playlist);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) { //create new viewholder
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.track_list_item, parent, false);
            viewHolder.trackImgView = (ImageView) convertView.findViewById(R.id.trackImgView);
            viewHolder.trackTextView = (TextView) convertView.findViewById(R.id.trackTextView);
            viewHolder.artistTextView = (TextView) convertView.findViewById(R.id.artistTextView);
            viewHolder.lengthTextView = (TextView) convertView.findViewById(R.id.lengthTextView);
            convertView.setTag(viewHolder);
        }
        else { // reuse existing ViewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.trackImgView.setImageResource(track.getImgId());

        viewHolder.trackTextView.setText(track.getTrackName());
        viewHolder.artistTextView.setText(track.getArtist());
        viewHolder.lengthTextView.setText(track.getTrackLength());

        return convertView; // return completed list item to display
    }

    public void setImg(int position, View convertView) {
        Track track = getItem(position);
        //convertView MUST exist for this to work
        if (convertView != null) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.trackImgView.setImageResource(track.getImgId());
        }

    }
}
