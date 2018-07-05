package com.paulonio.chess.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.paulonio.chess.models.Game;
import com.paulonio.chess.models.ListOrderingType;
import com.paulonio.chess.models.Player;
import com.paulonio.chess.models.Score;
import com.paulonio.chess.models.Tournament;
import com.paulonio.chess.models.TournamentTieBreaks;
import com.paulonio.chess.models.TournamentType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class DBHelper extends SQLiteOpenHelper {

    public static final String LAST_UPDATE_FIDE_LIST_PROPERTY = "LAST_UPDATE_FIDE_LIST";

    public static final int version = 19;
    public static final String DATABASE_NAME = "ChessApp.db";

    public static final String TOURNAMENTS_TABLE_NAME = "Tournaments";
    public static final String TOURNAMENT_COLUMN_NAME = "Name";
    public static final String TOURNAMENT_COLUMN_CREATION_DATE = "CD";
    public static final String TOURNAMENT_COLUMN_TIE_BRIEAK = "TieBreak";
    public static final String TOURNAMENT_COLUMN_ORDERING_TYPE = "OrderingType";
    public static final String TOURNAMENT_COLUMN_TOURNAMENT_TYPE = "TournamentType";
    public static final String TOURNAMENT_COLUMN_NUMBER_OF_ROUND_ROBINS = "RoundRobins";
    public static final String TOURNAMENT_COLUMN_ROUNDS_PLAYED = "RoundsPlayed";

    private static final String[] TOURNAMENT_COLUMNS = new String[]{TOURNAMENT_COLUMN_NAME, TOURNAMENT_COLUMN_CREATION_DATE, TOURNAMENT_COLUMN_TIE_BRIEAK,
            TOURNAMENT_COLUMN_ORDERING_TYPE, TOURNAMENT_COLUMN_ORDERING_TYPE, TOURNAMENT_COLUMN_TOURNAMENT_TYPE, TOURNAMENT_COLUMN_NUMBER_OF_ROUND_ROBINS,
            TOURNAMENT_COLUMN_ROUNDS_PLAYED};

    public static final String PLAYERS_TABLE_NAME = "Players";
    public static final String PLAYERS_COLUMN_TOURNAMENT_ID = "TournamentId";
    public static final String PLAYERS_COLUMN_NAME = "Name";
    public static final String PLAYERS_COLUMN_SURNAME = "Surname";
    public static final String PLAYERS_COLUMN_RATING = "Rating";
    public static final String PLAYERS_COLUMN_SCORE = "Score";
    public static final String PLAYERS_COLUMN_BERGER = "Berger";
    public static final String PLAYERS_COLUMN_NUMBER = "Number";

    public static final String GAMES_TABLE_NAME = "Games";
    public static final String GAMES_COLUMN_WHITE_PLAYER = "WhitePlayer";
    public static final String GAMES_COLUMN_BLACK_PLAYER = "BlackPlayer";
    public static final String GAMES_COLUMN_SCORE = "Score";
    public static final String GAMES_COLUMN_TOURNAMENT_ID = "TournamentId";
    public static final String GAMES_COLUMN_ROUND_NUMBER = "RoundNumber";
    public static final String GAMES_COLUMN_BOARD_NUMBER = "BoardNumber";

    public static final String FIDE_PLAYERS_TABLE_NAME = "FidePlayers";
    public static final String FIDE_PLAYERS_COLUMN_SURNAME = "Surname";
    public static final String FIDE_PLAYERS_COLUMN_NAME = "Name";
    public static final String FIDE_PLAYERS_COLUMN_RATING = "Rating";

    public static final String OPTIONS_TABLE_NAME = "Options";
    public static final String OPTIONS_COLUMN_PROPERTY = "Property";
    public static final String OPTIONS_COLUMN_VALUE = "Value";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table " + PLAYERS_TABLE_NAME +
                        " (id integer primary key," +
                        "Name text," +
                        "Surname text," +
                        "Rating integer," +
                        "Score integer," +
                        "Berger integer," +
                        "Number integer," +
                        "TournamentId," +
                        "FOREIGN KEY(TournamentId) REFERENCES Tournament(id))"
        );

        db.execSQL(
                "create table " + TOURNAMENTS_TABLE_NAME +
                        " (id integer primary key," +
                        "Name text," +
                        "CD integer," +
                        "TieBreak text," +
                        "OrderingType text," +
                        "TournamentType text," +
                        "RoundRobins integer," +
                        "RoundsPlayed)"

        );

        db.execSQL(
                "create table " + GAMES_TABLE_NAME +
                        " (id integer primary key," +
                        "WhitePlayer integer," +
                        "BlackPlayer integer," +
                        "Score text," +
                        "TournamentId," +
                        "RoundNumber," +
                        "BoardNumber," +
                        "FOREIGN KEY(WhitePlayer) REFERENCES Players(id)," +
                        "FOREIGN KEY(BlackPlayer) REFERENCES Players(id)," +
                        "FOREIGN KEY(TournamentId) REFERENCES Tournament(id))"
        );

        db.execSQL(
                "create table " + FIDE_PLAYERS_TABLE_NAME +
                        " (id integer primary key," +
                        "Surname text," +
                        "Name text," +
                        "Rating text)");

        db.execSQL(
                "create table " + OPTIONS_TABLE_NAME +
                        " (id integer primary key," +
                        "Property text," +
                        "Value text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TOURNAMENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FIDE_PLAYERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OPTIONS_TABLE_NAME);
        this.onCreate(db);
    }

    public String getProperty(String property) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(OPTIONS_TABLE_NAME, new String[]{OPTIONS_COLUMN_VALUE}, "Property = ?", new String[]{property}, null, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
        } else {
            return "";
        }

        String value = cursor.getString(cursor.getColumnIndex(OPTIONS_COLUMN_VALUE));
        cursor.close();
        return value;
    }

    public void setProperty(String property, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(OPTIONS_COLUMN_PROPERTY, property);
        contentValues.put(OPTIONS_COLUMN_VALUE, value);

        long id = db.insertWithOnConflict(OPTIONS_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            db.update(FIDE_PLAYERS_TABLE_NAME, contentValues, "Property = ?", new String[]{String.valueOf(property)});
        }
    }

    public List<FidePlayer> findFidePlayer(String stringToFind) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + FIDE_PLAYERS_TABLE_NAME + " WHERE " + FIDE_PLAYERS_COLUMN_SURNAME + " LIKE '" + stringToFind + "%'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<FidePlayer> players = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(FIDE_PLAYERS_COLUMN_NAME));
                String surname = cursor.getString(cursor.getColumnIndex(FIDE_PLAYERS_COLUMN_SURNAME));
                String rating = cursor.getString(cursor.getColumnIndex(FIDE_PLAYERS_COLUMN_RATING));
                players.add(FidePlayer.of("", name, surname, rating));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return players;
    }

    public void insertOrUpdateFidePlayers(List<FidePlayer> players) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for (FidePlayer player : players) {
            insertOrUpdateFidePlayer(player, db);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void insertOrUpdateFidePlayer(FidePlayer player, SQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Long.parseLong(player.getFideId()));
        contentValues.put(FIDE_PLAYERS_COLUMN_SURNAME, player.getSurname());
        contentValues.put(FIDE_PLAYERS_COLUMN_NAME, player.getName());
        contentValues.put(FIDE_PLAYERS_COLUMN_RATING, player.getRating());

        long id = db.insertWithOnConflict(FIDE_PLAYERS_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            db.update(FIDE_PLAYERS_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(player.getFideId())});
        }
    }

    public void insertOrUpdateTournament(Tournament tournament) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOURNAMENT_COLUMN_NAME, tournament.getName());
        contentValues.put(TOURNAMENT_COLUMN_CREATION_DATE, tournament.getCreationDate().getTime());
        contentValues.put(TOURNAMENT_COLUMN_NUMBER_OF_ROUND_ROBINS, tournament.getNumberOfRoundRobins());
        contentValues.put(TOURNAMENT_COLUMN_ROUNDS_PLAYED, tournament.getNumberOfRoundsPlayed());
        contentValues.put(TOURNAMENT_COLUMN_ORDERING_TYPE, tournament.getOrderingType().getDbName());
        contentValues.put(TOURNAMENT_COLUMN_TIE_BRIEAK, tournament.getTieBreak().getDbName());
        contentValues.put(TOURNAMENT_COLUMN_TOURNAMENT_TYPE, tournament.getTournamentType().getDbName());
        if (tournament.getDataBaseId() == 0) {
            long id = db.insertOrThrow(TOURNAMENTS_TABLE_NAME, null, contentValues);
            tournament.setDataBaseId(id);
        } else {
            db.update(TOURNAMENTS_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(tournament.getDataBaseId())});
        }
        for (Player player : tournament.getPlayers()) {
            insertOrUpdatePlayer(player, tournament.getDataBaseId());
        }

        for (int roundNumber = 0; roundNumber < tournament.getPairings().size(); ++roundNumber) {
            for (int boardNumber = 0; boardNumber < tournament.getPairings().get(roundNumber).size(); ++boardNumber) {
                insertOrUpdateGame(tournament.getPairings().get(roundNumber).get(boardNumber), tournament.getDataBaseId(), roundNumber, boardNumber);
            }
        }
    }

    public void insertOrUpdatePlayer(Player player, long tournamentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYERS_COLUMN_NAME, player.getName());
        contentValues.put(PLAYERS_COLUMN_SURNAME, player.getSurname());
        contentValues.put(PLAYERS_COLUMN_NUMBER, player.getStartListNumber());
        contentValues.put(PLAYERS_COLUMN_RATING, player.getRating());
        contentValues.put(PLAYERS_COLUMN_SCORE, player.getScore());
        contentValues.put(PLAYERS_COLUMN_BERGER, player.getBergerScore());
        contentValues.put(PLAYERS_COLUMN_TOURNAMENT_ID, tournamentId);

        if (player.getDataBaseId() == 0) {
            long id = db.insertOrThrow(PLAYERS_TABLE_NAME, null, contentValues);
            player.setDataBaseId(id);
        } else {
            db.update(PLAYERS_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(player.getDataBaseId())});
        }
    }

    public void insertOrUpdateGame(Game game, long tournamentId, int roundNumber, int boardNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GAMES_COLUMN_BLACK_PLAYER, game.getBlackPlayer().getDataBaseId());
        contentValues.put(GAMES_COLUMN_WHITE_PLAYER, game.getWhitePlayer().getDataBaseId());
        contentValues.put(GAMES_COLUMN_TOURNAMENT_ID, tournamentId);
        contentValues.put(GAMES_COLUMN_SCORE, game.getGameScore() != null ? game.getGameScore().getDbName() : null);
        contentValues.put(GAMES_COLUMN_BOARD_NUMBER, boardNumber);
        contentValues.put(GAMES_COLUMN_ROUND_NUMBER, roundNumber);

        if (game.getDataBaseId() == 0) {
            long id = db.insertOrThrow(GAMES_TABLE_NAME, null, contentValues);
            game.setDataBaseId(id);
        } else {
            db.update(GAMES_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(game.getDataBaseId())});
        }
    }

    public Tournament getTournament(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TOURNAMENTS_TABLE_NAME, TOURNAMENT_COLUMNS, "id = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Tournament tournament = getTournamentFromCursor(cursor, db);
        cursor.close();

        return tournament;
    }

    private Tournament getTournamentFromCursor(Cursor cursor, SQLiteDatabase db) {
        Tournament tournament = Tournament.of(
                cursor.getString(cursor.getColumnIndex(TOURNAMENT_COLUMN_NAME)),
                new Date(cursor.getLong(cursor.getColumnIndex(TOURNAMENT_COLUMN_CREATION_DATE))),
                ListOrderingType.getValueFromDbName(cursor.getString(cursor.getColumnIndex(TOURNAMENT_COLUMN_ORDERING_TYPE))),
                TournamentTieBreaks.getValueFromDbName(cursor.getString(cursor.getColumnIndex(TOURNAMENT_COLUMN_TIE_BRIEAK))),
                TournamentType.getValueFromDbName(cursor.getString(cursor.getColumnIndex(TOURNAMENT_COLUMN_TOURNAMENT_TYPE))),
                cursor.getInt(cursor.getColumnIndex(TOURNAMENT_COLUMN_NUMBER_OF_ROUND_ROBINS)));
        tournament.setNumberOfRoundsPlayed(cursor.getInt(cursor.getColumnIndex(TOURNAMENT_COLUMN_ROUNDS_PLAYED)));
        Long id = cursor.getLong(cursor.getColumnIndex("id"));
        List<Player> players = getPlayersFromTournament(id, db);
        tournament.getPlayers().addAll(players);
        tournament.getStartList().addAll(players);

        int numberOfPlayers = tournament.getNumberOfPlayers();
        int numberOfRounds = (numberOfPlayers % 2 == 1) ? numberOfPlayers : numberOfPlayers - 1;
        List<List<Game>> pairings = new ArrayList<>();
        for (int i = 0; i < numberOfRounds; ++i) {
            pairings.add(new ArrayList<Game>());
        }

        String selectQuery = "SELECT  * FROM " + GAMES_TABLE_NAME + " WHERE " + GAMES_COLUMN_TOURNAMENT_ID + " = " + String.valueOf(id) + " ORDER BY " +
                GAMES_COLUMN_BOARD_NUMBER + " ASC";
        Cursor gamesCursor = db.rawQuery(selectQuery, null);
        if (gamesCursor.moveToFirst()) {
            do {
                Long whitePlayerId = gamesCursor.getLong(gamesCursor.getColumnIndex(GAMES_COLUMN_WHITE_PLAYER));
                Long blackPlayerId = gamesCursor.getLong(gamesCursor.getColumnIndex(GAMES_COLUMN_BLACK_PLAYER));
                Player whitePlayer = getPlayerByDbId(players, whitePlayerId);
                Player blackPlayer = getPlayerByDbId(players, blackPlayerId);
                Score score = Score.getValueFromDbName(gamesCursor.getString(gamesCursor.getColumnIndex(GAMES_COLUMN_SCORE)));
                int roundNumber = gamesCursor.getInt(gamesCursor.getColumnIndex(GAMES_COLUMN_ROUND_NUMBER));

                Game game = Game.of(whitePlayer, blackPlayer);
                game.setDataBaseId(gamesCursor.getLong(gamesCursor.getColumnIndex("id")));

                if (score != null) {
                    game.setGameScore(score);
                    whitePlayer.getPlayedGames().add(game);
                    blackPlayer.getPlayedGames().add(game);
                }
                pairings.get(roundNumber).add(game);
            } while (gamesCursor.moveToNext());
        }
        gamesCursor.close();
        tournament.setDataBaseId(id);
        tournament.getPairings().addAll(pairings);

        return tournament;
    }

    private Player getPlayerFromCursor(Cursor cursor) {
        Player player = Player.of(
                cursor.getString(cursor.getColumnIndex(PLAYERS_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(PLAYERS_COLUMN_SURNAME)),
                cursor.getInt(cursor.getColumnIndex(PLAYERS_COLUMN_RATING)));
        player.setDataBaseId(cursor.getLong(cursor.getColumnIndex("id")));
        player.setScore(cursor.getDouble(cursor.getColumnIndex(PLAYERS_COLUMN_SCORE)));
        player.setBergerScore(cursor.getDouble(cursor.getColumnIndex(PLAYERS_COLUMN_BERGER)));
        player.setStartListNumber(cursor.getInt(cursor.getColumnIndex(PLAYERS_COLUMN_NUMBER)));

        return player;
    }

    private List<Player> getPlayersFromTournament(Long id, SQLiteDatabase db) {
        List<Player> players = new ArrayList<Player>();

        String selectQuery = "SELECT  * FROM " + PLAYERS_TABLE_NAME + " WHERE " + PLAYERS_COLUMN_TOURNAMENT_ID + " = " + String.valueOf(id) + " ORDER BY " +
                PLAYERS_COLUMN_NUMBER + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                players.add(getPlayerFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return players;
    }


    public List<Tournament> getAllTournaments() {
        List<Tournament> tournaments = new ArrayList<Tournament>();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TOURNAMENTS_TABLE_NAME + " ORDER BY " + TOURNAMENT_COLUMN_CREATION_DATE + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                tournaments.add(getTournamentFromCursor(cursor, db));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tournaments;
    }

    private Player getPlayerByDbId(List<Player> players, Long id) {
        if (id == 0) {
            return Tournament.FAKE_BYE_PLAYER;
        }
        for (Player player : players) {
            if (player.getDataBaseId() == id) {
                return player;
            }
        }
        throw new NoSuchElementException("Player with given id not exists!");
    }
}
