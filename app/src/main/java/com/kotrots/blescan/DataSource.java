package com.kotrots.blescan;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {
    private SQLiteDatabase database;
    private SQLliteHelper dbHelper;

    private String[] allMesurementsColumns = { SQLliteHelper.COLUMN_MEASUREMENTS_ID,
            SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_A, SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_B,
            SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_C, SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_D,
            SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_E, SQLliteHelper.COLUMN_MEASUREMENTS_ACCELEROMETER_X,
            SQLliteHelper.COLUMN_MEASUREMENTS_ACCELEROMETER_Y, SQLliteHelper.COLUMN_MEASUREMENTS_ACCELEROMETER_Z,
            SQLliteHelper.COLUMN_MEASUREMENTS_STEPS, SQLliteHelper.COLUMN_MEASUREMENTS_TIMESTAMP
    };

    public DataSource(Context context){
        dbHelper = new SQLliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void inserMesurement(Mesurement mesurement){
        String sqlQuery = "INSERT INTO "+SQLliteHelper.TABLE_MEASUREMENTS+" ("
                +SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_A+", "+SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_B+", "
                +SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_C+", "+SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_D+", "
                +SQLliteHelper.COLUMN_MEASUREMENTS_RSSI_IBKS_E+", "+SQLliteHelper.COLUMN_MEASUREMENTS_ACCELEROMETER_X+", "
                +SQLliteHelper.COLUMN_MEASUREMENTS_ACCELEROMETER_Y+", "+SQLliteHelper.COLUMN_MEASUREMENTS_ACCELEROMETER_Z+", "
                +SQLliteHelper.COLUMN_MEASUREMENTS_STEPS+", "+SQLliteHelper.COLUMN_MEASUREMENTS_TIMESTAMP+", "
                +SQLliteHelper.COLUMN_MEASUREMENTS_LOCATION+") VALUES ("
                +mesurement.getRssi_ibksA()+", "+mesurement.getRssi_ibksB()+", "
                +mesurement.getRssi_ibksC()+", "+mesurement.getRssi_ibksD()+", "
                +mesurement.getRssi_ibksE()+", "+mesurement.getAcceler_X()+", "
                +mesurement.getAcceler_Y()+", "+mesurement.getAcceler_Z()+", "
                +mesurement.getSteps()+", "+mesurement.getTimestamp()+", '"
                +mesurement.getLocation()+"')";

        Log.d("insertLog", sqlQuery);

        database.execSQL(sqlQuery);
    }

}
