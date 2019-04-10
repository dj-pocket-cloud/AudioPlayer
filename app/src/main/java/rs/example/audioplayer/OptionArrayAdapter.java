package rs.example.audioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class OptionArrayAdapter extends ArrayAdapter<Option> {
    private static class ViewHolder{
        ImageView optionImgView;
        TextView optionTextView;
        TextView descriptionTextView;
    }
    public OptionArrayAdapter(Context context, List<Option> option) {
        super(context, -1, option);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Option option = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) { //create new viewholder
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.option_list_item, parent, false);
            viewHolder.optionImgView = (ImageView) convertView.findViewById(R.id.optionImgView);
            viewHolder.optionTextView = (TextView) convertView.findViewById(R.id.optionTextView);
            viewHolder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
            convertView.setTag(viewHolder);
        }
        else { // reuse existing ViewHolder
            viewHolder = (OptionArrayAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.optionImgView.setImageResource(R.drawable.ic_settings_black_24dp);

        viewHolder.optionTextView.setText(option.getOptionName());
        viewHolder.descriptionTextView.setText(option.getOptionDescription());


        return convertView; // return completed list item to display
    }
}
