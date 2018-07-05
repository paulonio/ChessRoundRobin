package com.paulonio.chess.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.paulonio.chess.R;
import com.paulonio.chess.dao.DBHelper;
import com.paulonio.chess.models.Tournament;
import com.paulonio.chess.utils.FileDownloader;
import com.paulonio.chess.utils.TournamentAdapter;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private ListView tournamentsListView;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(this);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        String currentDate = String.valueOf(year) + "/" + String.valueOf(month);
        String lastUpdatedFideList = db.getProperty(DBHelper.LAST_UPDATE_FIDE_LIST_PROPERTY);
        if (!currentDate.equals(lastUpdatedFideList)) {
            AlertDialog dialog = prepareAlertDialog();
            dialog.show();
        }


        List<Tournament> tournaments = db.getAllTournaments();
        tournamentsListView = (ListView) findViewById(R.id.tournamentsListView);

        TournamentAdapter adapter = new TournamentAdapter(this, tournaments);
        tournamentsListView.setAdapter(adapter);

        tournamentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tournament tournament = (Tournament) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, StartListActivity.class);
                intent.putExtra("Tournament", tournament);
                MainActivity.this.startActivity(intent);
            }

        });
    }

    public void onNewTournamnentClick(View vew) {
        Intent myIntent = new Intent(MainActivity.this, NewTournamentActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    private AlertDialog prepareAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(R.string.AlerTitle);
        alertDialogBuilder
                .setMessage(R.string.AlertDIalogMessage)
                .setCancelable(false)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new FileDownloader(getApplicationContext()).execute("http://ratings.fide.com/download/blitz_rating_list_xml.zip");
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return alertDialogBuilder.create();
    }
}
