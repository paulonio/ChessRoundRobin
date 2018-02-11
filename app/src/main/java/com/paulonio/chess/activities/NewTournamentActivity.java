package com.paulonio.chess.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paulonio.chess.R;
import com.paulonio.chess.models.ListOrderingType;
import com.paulonio.chess.models.Player;
import com.paulonio.chess.models.Tournament;
import com.paulonio.chess.models.TournamentTieBreaks;
import com.paulonio.chess.models.TournamentType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewTournamentActivity extends AppCompatActivity {

    EditText tournamentNameET;
    Spinner roundRobinsSpinner;
    Spinner tiebreaksSpinner;
    Spinner startListOrderSpinner;
    Spinner tournamnetTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tournament);

        tournamentNameET = findViewById(R.id.tournamentNameEditText);
        roundRobinsSpinner = findViewById(R.id.numberOfRobinsSpinner);
        tiebreaksSpinner = findViewById(R.id.tieBrealSpinner);
        startListOrderSpinner = findViewById(R.id.orderSpinner);
        tournamnetTypeSpinner = findViewById(R.id.tournamnetTypeSpinner);
    }

    public void onNextButtonClick(View vew) {
        Tournament tournament = Tournament.of(tournamentNameET.getText().toString(),
                new Date(),
                ListOrderingType.getValueFromSpinnerIndex(startListOrderSpinner.getSelectedItemPosition()),
                TournamentTieBreaks.getValueFromSpinnerIndex(tiebreaksSpinner.getSelectedItemPosition()),
                TournamentType.getValueFromSpinnerIndex(tournamnetTypeSpinner.getSelectedItemPosition()),
                roundRobinsSpinner.getSelectedItemPosition() + 1);
        Intent intent = new Intent(NewTournamentActivity.this, AddPlayerActivity.class);
        intent.putExtra("Tournament", tournament);
        NewTournamentActivity.this.startActivity(intent);
    }
}
