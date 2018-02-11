package com.paulonio.chess.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.paulonio.chess.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNewTournamnentClick(View vew) {
        Intent myIntent = new Intent(MainActivity.this, NewTournamentActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
}
