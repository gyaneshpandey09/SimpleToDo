package com.gp.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * This Class helps with various DB calls.
 */
public class ItemsDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "itemsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Post Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT = "text";
    private static final String KEY_ITEM_DATE = "date";
    private static final String TAG = "ItemsDatabaseHelper";
    private static ItemsDatabaseHelper sInstance;

    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ItemsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private ItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_TEXT + " TEXT," +
                KEY_ITEM_DATE + " DATE" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);

        Log.i(TAG, "items table created.");
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    // Insert a item into the database
    public void addItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, item.getText());
            values.put(KEY_ITEM_DATE, item.getDate());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add item to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();

        // SELECT * FROM ITEMS
        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM " + TABLE_ITEMS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item newItem = new Item();
                    newItem.setText(cursor.getString(cursor.getColumnIndex(KEY_ITEM_TEXT)));
                    newItem.setDate(cursor.getString(cursor.getColumnIndex(KEY_ITEM_DATE)));

                    items.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }


    public void deleteAllPostsAndUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_ITEMS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all items");
        } finally {
            db.endTransaction();
        }
    }
}


