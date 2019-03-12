package androidlabs.example.com.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;
import android.app.Activity;


class MyOpener extends SQLiteOpenHelper {
    public final static String TABLE_NAME = "messageTable";
    public final static String DATABASE_NAME = "MessageDB";
    public final static String COL_MESSAGE = "message";
    public final static String COL_ID = "_id";
    public final static String COL_TYPE = "type";
    public static final int VERSION_NUM = 1;

    public MyOpener(Activity ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_MESSAGE + " TEXT, " + COL_TYPE + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version_old, int current_version) {
        Log.i("Database upgrade", "Old version:" + version_old + " newVersion:"+ current_version);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int version_old, int current_version)
    {
        Log.i("Database downgrade", "Old version:" + version_old + " newVersion:"+ current_version);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }



}
