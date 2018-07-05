package com.paulonio.chess.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.paulonio.chess.R;
import com.paulonio.chess.dao.DBHelper;
import com.paulonio.chess.models.Game;
import com.paulonio.chess.models.Score;
import com.paulonio.chess.models.Tournament;
import com.paulonio.chess.utils.PairingsAdapter;

import java.util.List;

public class PairingsActivity extends AppCompatActivity {

    Tournament tournament;
    int actualRound;
    ListView pairingsListView;
    TextView roundTV;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairings);
        tournament = (Tournament) getIntent().getSerializableExtra("Tournament");
        actualRound = 1;
        roundTV = findViewById(R.id.roundTextView);
        pairingsListView = findViewById(R.id.pairingsListView);
        displayPairings();

        db = new DBHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tournament, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void displayPairings() {
        roundTV.setText(String.format(getString(R.string.round), actualRound));
        List<Game> pairings = tournament.getPairingsForRound(actualRound);
        PairingsAdapter adapter = new PairingsAdapter(this, pairings);
        pairingsListView.setAdapter(adapter);
    }

    public void onResultsClick(View vew) {
        Intent intent = new Intent(PairingsActivity.this, ResultsActivity.class);
        intent.putExtra("Tournament", tournament);
        PairingsActivity.this.startActivity(intent);

    }

    public void onStartListClick(View vew) {
        Intent intent = new Intent(PairingsActivity.this, StartListActivity.class);
        intent.putExtra("Tournament", tournament);
        PairingsActivity.this.startActivity(intent);

    }

    public void onNextClick(View vew) {
        actualRound = (actualRound + 1);
        if (actualRound > tournament.getPairings().size()) {
            actualRound = 1;
        }
        displayPairings();
    }

    public void onPrevClick(View vew) {
        --actualRound;
        if (actualRound < 1) {
            actualRound = tournament.getPairings().size();
        }
        displayPairings();
    }

    public void onSaveScoreClick(View view) {
        Spinner scoreSpinner;
        Game game;
        boolean ifRoundAlreadySaved = true;
        for (int i = 0; i < pairingsListView.getCount(); i++) {
            scoreSpinner = pairingsListView.getChildAt(i).findViewById(R.id.scoreSpinner);
            game = tournament.getPairingsForRound(actualRound).get(i);
            if (!game.getWhitePlayer().equals(Tournament.FAKE_BYE_PLAYER) && !game.getBlackPlayer().equals(Tournament.FAKE_BYE_PLAYER)) {
                Score score = Score.getValueFromSpinnerIndex(scoreSpinner.getSelectedItemPosition());
                if (game.getGameScore() == null) {
                    game.setGameScore(score);
                    ifRoundAlreadySaved = false;
                }
                scoreSpinner.setEnabled(false);
            }
        }
        if (!ifRoundAlreadySaved) {
            tournament.updateResults(actualRound, true);
        }
        db.insertOrUpdateTournament(tournament);
    }
}
