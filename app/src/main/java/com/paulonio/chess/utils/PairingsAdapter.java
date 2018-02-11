package com.paulonio.chess.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.paulonio.chess.R;
import com.paulonio.chess.models.Game;
import com.paulonio.chess.models.Tournament;

import java.util.List;

public class PairingsAdapter extends ArrayAdapter<Game> {
    public PairingsAdapter(Context context, List<Game> games) {
        super(context, 0, games);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Game game = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pairings_list, parent, false);
        }
        TextView tvWhite = convertView.findViewById(R.id.whiteTextView);
        TextView tvBlack = convertView.findViewById(R.id.blackTextView);
        Spinner scoreSpinner = convertView.findViewById(R.id.scoreSpinner);

        assert game != null;
        tvWhite.setText(String.format("%s, %s", game.getWhitePlayer().getSurname(), game.getWhitePlayer().getName()));
        tvBlack.setText(String.format("%s, %s", game.getBlackPlayer().getSurname(), game.getBlackPlayer().getName()));

        if (game.getWhitePlayer().equals(Tournament.FAKE_BYE_PLAYER) || game.getBlackPlayer().equals(Tournament.FAKE_BYE_PLAYER)) {
            scoreSpinner.setEnabled(false);
            scoreSpinner.setAdapter(null);
        }
        if (game.getGameScore() != null) {
            scoreSpinner.setEnabled(false);
            scoreSpinner.setSelection(game.getGameScore().getOrderInSpinner());
        }

        return convertView;
    }
}
