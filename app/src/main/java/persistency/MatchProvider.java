package persistency;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static persistency.MatchContract.CONTENT_AUTHORITY;
import static persistency.MatchContract.MatchEntry.TABLE_NAME;
import static persistency.MatchContract.PATH_MATCHES;

/**
 * Created by Impling on 06-Nov-17.
 */

//Provider Class
public class MatchProvider extends ContentProvider {

    private static final String TAG = MatchProvider.class.getSimpleName();
    private MatchContract.MatchDbHelper databaseHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int MATCHES = 1; //whole table
    private static final int MATCHES_ID = 2;//row by id

    static{
        //contains all URI patterns
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MATCHES, MATCHES);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MATCHES + "/#", MATCHES_ID);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new MatchContract.MatchDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {

            case MATCHES:
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case MATCHES_ID:
                selection = MatchContract.MatchEntry._ID + " = ?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException(TAG + "Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

/*
        String[] projection = {
                MatchContract.MatchEntry._ID,

                MatchContract.MatchEntry.COLUMN_LOCATION_ID,
                MatchContract.MatchEntry.COLUMN_DIFFICULTY_ID,
                MatchContract.MatchEntry.COLUMN_VALOR_ID,
                MatchContract.MatchEntry.COLUMN_HOME_ID,
                MatchContract.MatchEntry.COLUMN_VISITOR_ID,
                MatchContract.MatchEntry.COLUMN_SCORE_HOME,
                MatchContract.MatchEntry.COLUMN_DATE,
                MatchContract.MatchEntry.COLUMN_TIME_PLAYED,
                MatchContract.MatchEntry.COLUMN_CREATED_AT,
                MatchContract.MatchEntry.COLUMN_UPDATED_AT

        };

                // Filter results. Make these null if you want to query all rows
                String selection = null;
                String[] selectionArgs = null;

                String sortOrder = null;    // Ascending or Descending ...

        /*
        Cursor cursor = database.query(NationEntry.TABLE_NAME,        // The table name
                //projection,                 // The columns to return
                //selection,                  // Selection: WHERE clause OR the condition
                //selectionArgs,              // Selection Arguments for the WHERE clause
                //null,                       // don't group the rows
                //null,                       // don't filter by row groups
                //sortOrder);                    // The sort order

                Uri uri = MatchContract.MatchEntry.CONTENT_URI;
                Log.i("DBTEST", ""+uri);
                Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

                if (cursor != null) {

                String str = "";
                while (cursor.moveToNext()) {    // Cursor iterates through all rows

                String[] columns = cursor.getColumnNames();
                for (String column : columns) {
                str += "\t" + cursor.getString(cursor.getColumnIndex(column));
                }
                str += "\n";
                }

                cursor.close();
                Log.i("DbTEst", str);
                }
* */