package persistency;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Impling on 06-Nov-17.
 */

//Following method seen in class - ?Compatible with backend?
public final class MatchContract {

    public static final String CONTENT_AUTHORITY = "voom.be:12005/";
    public static final Uri BASE_CONTENT_URI = Uri.parse("http://" + CONTENT_AUTHORITY);
    public static final String PATH_MATCHES = "api/matches/";

    //Entry class
    public static final class MatchEntry implements BaseColumns {

        //Uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MATCHES);

        //Table name
        public static final String TABLE_NAME = "";

        //Columns
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_LOCATION_ID = "location";
        public static final String COLUMN_DIFFICULTY_ID = "difficulty";
        public static final String COLUMN_VALOR_ID = "valor";
        public static final String COLUMN_HOME_ID = "home";
        public static final String COLUMN_VISITOR_ID = "visitor";
        public static final String COLUMN_SCORE_HOME = "homeScore";
        public static final String COLUMN_SCORE_VISITOR = "visitorScore";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME_PLAYED = "timePlayed";
        public static final String COLUMN_CREATED_AT = "timeCreated";
        public static final String COLUMN_UPDATED_AT = "timeUpdated";

    }

    //Helper class
    public static class MatchDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "matches.db";
        private static final int DATABASE_VERSION = 1;

        private final String SQL_CREATE_MATCH_TABLE = "CREATE TABLE "
                + MatchContract.MatchEntry.TABLE_NAME
                + " ("
                + MatchEntry._ID                    + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MatchEntry.COLUMN_LOCATION_ID     + "INTEGER NOT NULL, "
                + MatchEntry.COLUMN_DIFFICULTY_ID   + "INTEGER NOT NULL, "
                + MatchEntry.COLUMN_VALOR_ID        + "INTEGER NOT NULL, "
                + MatchEntry.COLUMN_HOME_ID         + "INTEGER NOT NULL, "
                + MatchEntry.COLUMN_VISITOR_ID      + "INTEGER NOT NULL, "
                + MatchEntry.COLUMN_SCORE_HOME      + "INTEGER, "
                + MatchEntry.COLUMN_SCORE_VISITOR   + "INTEGER, "
                + MatchEntry.COLUMN_DATE            + "DATE NOT NULL, "
                + MatchEntry.COLUMN_TIME_PLAYED     + "TIMESTAMP, "
                + MatchEntry.COLUMN_CREATED_AT      + "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + MatchEntry.COLUMN_UPDATED_AT      + "TIMESTAMP, "
                + ");";


        public MatchDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MATCH_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(String.format("DROP TABLE IF EXISTS %s", MatchEntry.TABLE_NAME));
            onCreate(db);
        }


    }



}
