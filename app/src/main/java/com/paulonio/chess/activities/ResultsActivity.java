package com.paulonio.chess.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.paulonio.chess.R;
import com.paulonio.chess.models.Tournament;
import com.paulonio.chess.utils.ResultsAdapter;

public class ResultsActivity extends AppCompatActivity {

    Tournament tournament;
    ListView resultsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        tournament = (Tournament) getIntent().getSerializableExtra("Tournament");
        resultsListView = (ListView) findViewById(R.id.resultsListView);
        View header = getLayoutInflater().inflate(R.layout.header_results_list, null);
        resultsListView.addHeaderView(header);
        displayResults();
    }

    public void onPairingsClick(View vew) {
        Intent intent = new Intent(ResultsActivity.this, PairingsActivity.class);
        intent.putExtra("Tournament", tournament);
        ResultsActivity.this.startActivity(intent);

    }

    public void onStartListClick(View vew) {
        Intent intent = new Intent(ResultsActivity.this, StartListActivity.class);
        intent.putExtra("Tournament", tournament);
        ResultsActivity.this.startActivity(intent);

    }

    private void displayResults() {
        if (tournament.getNumberOfRoundsPlayed() > 0) {
            ResultsAdapter adapter = new ResultsAdapter(this, tournament.getResults());
            resultsListView.setAdapter(adapter);
        }
    }
}
