package com.example.chatgptuiapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "auditLogs.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_AUDIT_PROMPT = "AuditPrompt";
    private static final String TABLE_RESPONSES = "Responses";

    // AuditPrompt Columns
    private static final String KEY_AUDIT_ID = "sequenceNumber";
    private static final String KEY_AUDIT_DATE = "dateTime";
    private static final String KEY_PROMPT = "prompt";

    // Responses Columns
    private static final String KEY_RESPONSE_ID = "sequenceNumber";
    private static final String KEY_RESPONSE_DATE = "dateTime";
    private static final String KEY_RESPONSE = "response";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_AUDIT_PROMPT_TABLE = "CREATE TABLE " + TABLE_AUDIT_PROMPT +
                "(" +
                KEY_AUDIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Define a primary key
                KEY_AUDIT_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                KEY_PROMPT + " TEXT NOT NULL" +
                ")";

        String CREATE_RESPONSES_TABLE = "CREATE TABLE " + TABLE_RESPONSES +
                "(" +
                KEY_RESPONSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_RESPONSE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                KEY_RESPONSE + " TEXT NOT NULL" +
                ")";

        db.execSQL(CREATE_AUDIT_PROMPT_TABLE);
        db.execSQL(CREATE_RESPONSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIT_PROMPT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);
        onCreate(db);
    }

    // Method to insert data into AuditPrompt table
    public void insertPrompt(String prompt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROMPT, prompt);

        db.insert(TABLE_AUDIT_PROMPT, null, values);
        db.close();
    }

    // Method to insert data into Responses table
    public void insertResponse(String response) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RESPONSE, response);

        db.insert(TABLE_RESPONSES, null, values);
        db.close();
    }
    public void getAllPrompts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AUDIT_PROMPT, null);

        if (cursor.moveToFirst()) {
            do {
                // Assuming your sequenceNumber is an integer, dateTime is a string, and prompt is a string
                @SuppressLint("Range") int sequenceNumber = cursor.getInt(cursor.getColumnIndex(KEY_AUDIT_ID));
                @SuppressLint("Range") String dateTime = cursor.getString(cursor.getColumnIndex(KEY_AUDIT_DATE));
                @SuppressLint("Range") String prompt = cursor.getString(cursor.getColumnIndex(KEY_PROMPT));

                Log.d("AuditPrompt", "Sequence Number: " + sequenceNumber + ", Date Time: " + dateTime + ", Prompt: " + prompt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void getAllResponses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESPONSES, null);

        if (cursor.moveToFirst()) {
            do {
                // Assuming your sequenceNumber is an integer, dateTime is a string, and response is a string
                @SuppressLint("Range") int sequenceNumber = cursor.getInt(cursor.getColumnIndex(KEY_RESPONSE_ID));
                @SuppressLint("Range") String dateTime = cursor.getString(cursor.getColumnIndex(KEY_RESPONSE_DATE));
                @SuppressLint("Range") String response = cursor.getString(cursor.getColumnIndex(KEY_RESPONSE));

                Log.d("Responses", "Sequence Number: " + sequenceNumber + ", Date Time: " + dateTime + ", Response: " + response);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
}
