package com.paulonio.chess.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.paulonio.chess.R;
import com.paulonio.chess.dao.DBHelper;
import com.paulonio.chess.dao.FidePlayer;
import com.paulonio.chess.utils.SearchAdapter;

import java.util.List;

public class SearchPlayerActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ListView searchListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_player);
        final DBHelper db = new DBHelper(SearchPlayerActivity.this);
        searchListView = findViewById(R.id.searchListView);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FidePlayer player = (FidePlayer) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(SearchPlayerActivity.this, AddPlayerActivity.class);
                intent.putExtra("Player", player);
                setResult(RESULT_OK, intent);
                finish();
            }

        });

        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                if (edit.length() > 2) {
                    String textToSearch = searchEditText.getText().toString();
                    List<FidePlayer> players = db.findFidePlayer(textToSearch);
                    SearchAdapter adapter = new SearchAdapter(SearchPlayerActivity.this, players);
                    searchListView.setAdapter(adapter);
                }
            }
        });

    }
}
