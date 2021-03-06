package com.paulonio.chess.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.paulonio.chess.R;
import com.paulonio.chess.models.Tournament;
import com.paulonio.chess.utils.StartListPlayerAdapter;

public class StartListActivity extends AppCompatActivity {

    private Tournament tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_list);
        tournament = (Tournament) getIntent().getSerializableExtra("Tournament");
        displayStartList();
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

    private void displayStartList() {
        StartListPlayerAdapter adapter = new StartListPlayerAdapter(this, tournament.getStartList());
        ListView listView = (ListView) findViewById(R.id.startListListView);
        listView.setAdapter(adapter);
    }

    public void onPairingsClick(View vew) {
        Intent intent = new Intent(StartListActivity.this, PairingsActivity.class);
        intent.putExtra("Tournament", tournament);
        StartListActivity.this.startActivity(intent);

    }

    public void onResultsClick(View vew) {
        Intent intent = new Intent(StartListActivity.this, ResultsActivity.class);
        intent.putExtra("Tournament", tournament);
        StartListActivity.this.startActivity(intent);

    }
}
