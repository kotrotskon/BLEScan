package com.kotrots.blescan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "location_db.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MEASUREMENTS = "measurements";
    public static final String COLUMN_MEASUREMENTS_ID = "_id";
    public static final String COLUMN_MEASUREMENTS_RSSI_IBKS_A = "rssi_ibksA";
    public static final String COLUMN_MEASUREMENTS_RSSI_IBKS_B = "rssi_ibksB";
    public static final String COLUMN_MEASUREMENTS_RSSI_IBKS_C = "rssi_ibksC";
    public static final String COLUMN_MEASUREMENTS_RSSI_IBKS_D = "rssi_ibksD";
    public static final String COLUMN_MEASUREMENTS_RSSI_IBKS_E = "rssi_ibksE";
    public static final String COLUMN_MEASUREMENTS_ACCELEROMETER_X = "acceler_X";
    public static final String COLUMN_MEASUREMENTS_ACCELEROMETER_Y = "acceler_Y";
    public static final String COLUMN_MEASUREMENTS_ACCELEROMETER_Z = "acceler_Z";
    public static final String COLUMN_MEASUREMENTS_STEPS = "steps";
    public static final String COLUMN_MEASUREMENTS_TIMESTAMP = "timestamp";
    public static final String COLUMN_MEASUREMENTS_LOCATION = "location";


    private static final String DATABASE_CREATE_MEASUREMENTS = "create table "
            + TABLE_MEASUREMENTS + "(" + COLUMN_MEASUREMENTS_ID + " integer primary key autoincrement, "
            + COLUMN_MEASUREMENTS_RSSI_IBKS_A  + " integer,"
            + COLUMN_MEASUREMENTS_RSSI_IBKS_B + " integer,"
            + COLUMN_MEASUREMENTS_RSSI_IBKS_C + " integer,"
            + COLUMN_MEASUREMENTS_RSSI_IBKS_D + " integer,"
            + COLUMN_MEASUREMENTS_RSSI_IBKS_E + " integer,"
            + COLUMN_MEASUREMENTS_ACCELEROMETER_X + " real,"
            + COLUMN_MEASUREMENTS_ACCELEROMETER_Y + " real,"
            + COLUMN_MEASUREMENTS_ACCELEROMETER_Z + " real,"
            + COLUMN_MEASUREMENTS_STEPS + " integer,"
            + COLUMN_MEASUREMENTS_TIMESTAMP + " integer,"
            + COLUMN_MEASUREMENTS_LOCATION + " text)";

    public SQLliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_MEASUREMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASUREMENTS);

        onCreate(db);
    }
}
