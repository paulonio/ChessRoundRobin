package com.paulonio.chess.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.paulonio.chess.R;
import com.paulonio.chess.models.Player;
import com.paulonio.chess.models.Tournament;

public class AddPlayerActivity extends AppCompatActivity {

    private Tournament tournament;
    private EditText nameET;
    private EditText surnameET;
    private EditText ratingET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        tournament = (Tournament) getIntent().getSerializableExtra("Tournament");

        nameET = findViewById(R.id.nameEditText);
        surnameET = findViewById(R.id.surnameEditText);
        ratingET = findViewById(R.id.ratingEditText);
    }

    public void onAddButtonClick(View vew) {
        String name = nameET.getText().toString();
        String surname = surnameET.getText().toString();
        Integer rating = Integer.valueOf(ratingET.getText().toString());
        if (name.isEmpty() || surname.isEmpty() || rating == null) {
            Toast.makeText(getApplicationContext(), R.string.not_all_filled, Toast.LENGTH_SHORT).show();
            return;
        }
        Player player = Player.of(name, surname, rating);
        tournament.addPlayer(player);
        nameET.setText("");
        surnameET.setText("");
        ratingET.setText("0");

    }

    public void onFinishButtonClick(View vew) {
        if (tournament.getNumberOfPlayers() < 2) {
            Toast.makeText(getApplicationContext(), R.string.too_less_players, Toast.LENGTH_SHORT).show();
            return;
        }
        tournament.startTournament();
        Intent intent = new Intent(AddPlayerActivity.this, StartListActivity.class);
        intent.putExtra("Tournament", tournament);
        AddPlayerActivity.this.startActivity(intent);
    }
}
