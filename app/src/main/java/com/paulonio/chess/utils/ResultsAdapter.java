package com.paulonio.chess.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paulonio.chess.R;
import com.paulonio.chess.models.Player;

import java.util.List;


public class ResultsAdapter extends ArrayAdapter<Player> {
    public ResultsAdapter(Context context, List<Player> players) {
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Player player = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_results_list, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.nameSurnameTextView);
        TextView tvScore = convertView.findViewById(R.id.scoreTextView);
        TextView tvBerger = convertView.findViewById(R.id.bergerTextView);
        TextView tvPosition = convertView.findViewById(R.id.positionTextView);

        assert player != null;
        tvName.setText(String.format("%s, %s", player.getSurname(), player.getName()));
        tvPosition.setText(Integer.toString(position + 1));
        tvScore.setText(Double.toString(player.getScore()));
        tvBerger.setText(Double.toString(player.getBergerScore()));

        return convertView;
    }
}
