package com.paulonio.chess.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paulonio.chess.R;
import com.paulonio.chess.dao.FidePlayer;

import java.util.List;

public class SearchAdapter extends ArrayAdapter<FidePlayer> {
    public SearchAdapter(Context context, List<FidePlayer> tournaments) {
        super(context, 0, tournaments);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        FidePlayer player = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search_list, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.nameTextView);
        TextView tvRating = convertView.findViewById(R.id.ratingTextView);

        assert player != null;
        tvName.setText(String.format("%s, %s", player.getSurname(), player.getName()));
        tvRating.setText(player.getRating());

        return convertView;
    }
}
