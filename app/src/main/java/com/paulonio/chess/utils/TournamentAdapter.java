package com.paulonio.chess.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paulonio.chess.R;
import com.paulonio.chess.models.Tournament;

import java.text.DateFormat;
import java.util.List;

public class TournamentAdapter extends ArrayAdapter<Tournament> {
    public TournamentAdapter(Context context, List<Tournament> tournaments) {
        super(context, 0, tournaments);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Tournament tournament = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tournaments_list, parent, false);
        }
        TextView tvDate = convertView.findViewById(R.id.dateTextView);
        TextView tvName = convertView.findViewById(R.id.tournamentNameTextView);
        TextView tvRounds = convertView.findViewById(R.id.roundsTextView);

        assert tournament != null;
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        tvDate.setText(dateFormat.format(tournament.getCreationDate()));
        tvName.setText(tournament.getName());
        tvRounds.setText(String.format("%s/%s", tournament.getNumberOfRoundsPlayed(), tournament.getPairings().size()));

        return convertView;
    }
}
