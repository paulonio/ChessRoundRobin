package com.paulonio.chess.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.google.common.primitives.Bytes;
import com.paulonio.chess.dao.DBHelper;
import com.paulonio.chess.dao.FidePlayer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class FileDownloader extends AsyncTask<String, String, String> {

    private Context context;

    public FileDownloader(Context context) {
        this.context = context;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();

            ZipInputStream zis = new ZipInputStream(conection.getInputStream());
            ZipEntry ze = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            List<Byte> bytes = new ArrayList<Byte>();

            OutputStream output = new FileOutputStream("/data/data/com.paulonio.chess/fidePlayers.zip");

            while ((count = zis.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
            output.flush();
            output.close();


            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new FileInputStream("/data/data/com.paulonio.chess/fidePlayers.zip"), null);
            parser.nextTag();
            List<FidePlayer> players = new ArrayList<>();
            parser.require(XmlPullParser.START_TAG, null, "playerslist");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("player")) {
                    players.add(readPlayer(parser));
                } else {
                    skip(parser);
                }
            }

            DBHelper db = new DBHelper(this.context);
            db.insertOrUpdateFidePlayers(players);

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);

            String lastUpdate = String.valueOf(year) + "/" + String.valueOf(month);
            db.setProperty(DBHelper.LAST_UPDATE_FIDE_LIST_PROPERTY, lastUpdate);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    private FidePlayer readPlayer(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "player");
        String fideId = null;
        String playerName = null;
        String playerSurname = null;
        String playerRating = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("fideid")) {
                parser.require(XmlPullParser.START_TAG, null, "fideid");
                fideId = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "fideid");
            } else if (name.equals("name")) {
                parser.require(XmlPullParser.START_TAG, null, "name");
                String[] nameAndSurname = readText(parser).split(",");

                parser.require(XmlPullParser.END_TAG, null, "name");
                if (nameAndSurname.length < 2) {
                    nameAndSurname = nameAndSurname[0].split(" ");
                }
                playerSurname = nameAndSurname[0].trim();
                playerName = "";
                for (int i = 1; i < nameAndSurname.length; ++i) {
                    playerName += " " + nameAndSurname[i].trim();
                }


            } else if (name.equals("rating")) {
                parser.require(XmlPullParser.START_TAG, null, "rating");
                playerRating = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "rating");
            } else {
                skip(parser);
            }
        }
        return FidePlayer.of(fideId, playerName, playerSurname, playerRating);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    public void onPostExecute(String file_url) {
        File file = new File("/data/data/com.paulonio.chess/fidePlayers.zip");
        boolean deleted = file.delete();
    }

}