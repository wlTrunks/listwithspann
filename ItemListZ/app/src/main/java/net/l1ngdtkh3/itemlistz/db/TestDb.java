package net.l1ngdtkh3.itemlistz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;


import java.io.ByteArrayOutputStream;

/**
 * Created by Trunks on 02.03.2016.
 */
public class TestDb {
    private static ContentValues cv;
    private static SQLiteDatabase mDb;
    private static Context mContext;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "cool.db";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
    private static DbHelper instance;
    public static final String KEY_ID = "_id";
    public static final String C_TYPE = "type";
    public static final String C_TEXT = "sometext";
    public static final String C_IMG = "img";
    private static final String SOME_TABLE = "Smthg";
    private static final String CREATE_TABLE = "CREATE TABLE "
            + SOME_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_TYPE + " INTEGER NOT NULL, "
            + C_TEXT + " TEXT, "
            + C_IMG + " BLOB" + ");";


    public TestDb(Context context) {
        mContext = context;
        open();
    }

    public void open() {
        instance = DbHelper.getInstance(mContext);
        mDb = instance.getWritableDatabase();
    }

    public void putTxtImgData(int type, String txt, Bitmap img) {
        mDb = instance.getWritableDatabase();
        cv = new ContentValues();
        cv.put(C_TYPE, type);
        cv.put(C_TEXT, txt);
        cv.put(C_IMG, bitmapToByte(img));
        mDb.insert(SOME_TABLE, null, cv);
        mDb.close();
    }

    public void putTxtData(int type, String txt) {
        mDb = instance.getWritableDatabase();
        cv = new ContentValues();
        cv.put(C_TYPE, type);
        cv.put(C_TEXT, txt);
        mDb.insert(SOME_TABLE, null, cv);
        mDb.close();
    }

    public void putImgData(int type, Bitmap img) {
        mDb = instance.getWritableDatabase();
        cv = new ContentValues();
        cv.put(C_TYPE, type);
        cv.put(C_IMG, bitmapToByte(img));
        mDb.insert(SOME_TABLE, null, cv);
        mDb.close();
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Cursor fetchAllData() {
        open();
        String[] columns = new String[]{KEY_ID, C_TYPE, C_TEXT, C_IMG};
        Cursor c = mDb.query(SOME_TABLE, columns, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public static synchronized DbHelper getInstance(Context context) {
            if (instance == null) {
                instance = new DbHelper(context);
            }
            return instance;
        }

        private DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE_IF_EXISTS + SOME_TABLE);
        }
    }
}
