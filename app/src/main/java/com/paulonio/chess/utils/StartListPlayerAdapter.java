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

public class StartListPlayerAdapter extends ArrayAdapter<Player> {
    public StartListPlayerAdapter(Context context, List<Player> players) {
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Player player = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_start_list_player, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView tvNumber = (TextView) convertView.findViewById(R.id.numberTexView);
        TextView tvRating = (TextView) convertView.findViewById(R.id.ratingTextView);

        assert player != null;
        tvRating.setText(Integer.toString(player.getRating()));
        tvNumber.setText(Integer.toString(player.getStartListNumber()));
        tvName.setText(String.format("%s, %s", player.getSurname(), player.getName()));

        return convertView;
    }
}

