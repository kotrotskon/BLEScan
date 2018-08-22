package com.kotrots.blescan;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener{

    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    ScanSettings scanSettings;
    BluetoothGatt btGatt;

    private SensorManager senSensorAccelerometerManager;
    private Sensor senAccelerometer;

    private SensorManager senSensorStepManager;
    private Sensor senSensorStep;

    EditText edTxt_timestamp;
    EditText edTxt_ibks_A;
    EditText edTxt_ibks_B;
    EditText edTxt_ibks_C;
    EditText edTxt_ibks_D;
    EditText edTxt_ibks_E;

    EditText edTxt_X;
    EditText edTxt_Y;
    EditText edTxt_Z;
    EditText edTxt_steps;

    EditText edTxt_test;

    Button btn_a1;
    Button btn_a2;
    Button btn_a3;
    Button btn_a4;
    Button btn_b1;
    Button btn_b2;
    Button btn_b3;
    Button btn_b4;
    Button btn_c1;
    Button btn_c2;
    Button btn_c3;
    Button btn_c4;
    Button btn_d1;
    Button btn_d2;
    Button btn_d3;
    Button btn_d4;

    Long tsLong;

    int ibks_rssi_A = -200;
    int ibks_rssi_B = -200;
    int ibks_rssi_C = -200;
    int ibks_rssi_D = -200;
    int ibks_rssi_E = -200;

    int[] rssis = new int[5];

    long ibks_time_A;
    long ibks_time_B;
    long ibks_time_C;
    long ibks_time_D;
    long ibks_time_E;

    double acceler_x = 0;
    double acceler_y = 0;
    double acceler_z = 0;
    double[] accelers = new double[3];

    int steps = 0;
    long timestamp;

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edTxt_timestamp = findViewById(R.id.edTxt_timestamp);
        edTxt_ibks_A = findViewById(R.id.edTxt_ibks_A);
        edTxt_ibks_B = findViewById(R.id.edTxt_ibks_B);
        edTxt_ibks_C = findViewById(R.id.edTxt_ibks_C);
        edTxt_ibks_D = findViewById(R.id.edTxt_ibks_D);
        edTxt_ibks_E = findViewById(R.id.edTxt_ibks_E);

        edTxt_X = findViewById(R.id.edtxt_X);
        edTxt_Y = findViewById(R.id.edtxt_Y);
        edTxt_Z = findViewById(R.id.edtxt_Z);
        edTxt_steps = findViewById(R.id.edtxt_steps);

        edTxt_test = findViewById(R.id.edtxt_test);

        btn_a1 = findViewById(R.id.btn_A1);
        btn_a1.setOnClickListener(this);
        btn_a2 = findViewById(R.id.btn_A2);
        btn_a2.setOnClickListener(this);
        btn_a3 = findViewById(R.id.btn_A3);
        btn_a3.setOnClickListener(this);
        btn_a4 = findViewById(R.id.btn_A4);
        btn_a4.setOnClickListener(this);
        btn_b1 = findViewById(R.id.btn_B1);
        btn_b1.setOnClickListener(this);
        btn_b2 = findViewById(R.id.btn_B2);
        btn_b2.setOnClickListener(this);
        btn_b3 = findViewById(R.id.btn_B3);
        btn_b3.setOnClickListener(this);
        btn_b4 = findViewById(R.id.btn_B4);
        btn_b4.setOnClickListener(this);
        btn_c1 = findViewById(R.id.btn_C1);
        btn_c1.setOnClickListener(this);
        btn_c2 = findViewById(R.id.btn_C2);
        btn_c2.setOnClickListener(this);
        btn_c3 = findViewById(R.id.btn_C3);
        btn_c3.setOnClickListener(this);
        btn_c4 = findViewById(R.id.btn_C4);
        btn_c4.setOnClickListener(this);
        btn_d1 = findViewById(R.id.btn_D1);
        btn_d1.setOnClickListener(this);
        btn_d2 = findViewById(R.id.btn_D2);
        btn_d2.setOnClickListener(this);
        btn_d3 = findViewById(R.id.btn_D3);
        btn_d3.setOnClickListener(this);
        btn_d4 = findViewById(R.id.btn_D4);
        btn_d4.setOnClickListener(this);



        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

        senSensorAccelerometerManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorAccelerometerManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorAccelerometerManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        senSensorStepManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senSensorStep = senSensorStepManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        senSensorStepManager.registerListener(this, senSensorStep , SensorManager.SENSOR_DELAY_NORMAL);

        initBT();

        startScanning();
    }

    private void initBT(){
        final BluetoothManager btManager =  (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        //Create the scan settings
        ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder();
        //Set scan latency mode. Lower latency, faster device detection/more battery and resources consumption
        scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        //Wrap settings together and save on a settings var (declared globally).
        scanSettings = scanSettingsBuilder.build();
        //Get the BLE scanner from the BT adapter (var declared globally)
        btScanner = btAdapter.getBluetoothLeScanner();

        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();
    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d("MyLog", result.getDevice().getName());

            String beaconName = result.getDevice().getName();
            int beaconRssi = result.getRssi();

            tsLong = System.currentTimeMillis()/1000;
            edTxt_timestamp.setText(String.valueOf(tsLong));



            switch (beaconName){
                case "iBKS105_A":
                    ibks_rssi_A = beaconRssi;
                    ibks_time_A = tsLong;
                    break;
                case "iBKS105_B":
                    ibks_rssi_B = beaconRssi;
                    ibks_time_B = tsLong;
                    break;
                case "iBKS105_C":
                    ibks_rssi_C = beaconRssi;
                    ibks_time_C = tsLong;
                    break;
                case "iBKS105_D":
                    ibks_rssi_D = beaconRssi;
                    ibks_time_D = tsLong;
                    break;
                case "iBKS105_E":
                    ibks_rssi_E = beaconRssi;
                    ibks_time_E = tsLong;
                    break;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (tsLong-ibks_time_A > 5){
                        ibks_rssi_A = -200;
                        edTxt_ibks_A.setTextColor(Color.RED);
                    }
                    else{
                        edTxt_ibks_A.setTextColor(Color.BLACK);
                    }
                    if (tsLong-ibks_time_B > 5){
                        ibks_rssi_B = -200;
                        edTxt_ibks_B.setTextColor(Color.RED);
                    }else{
                        edTxt_ibks_B.setTextColor(Color.BLACK);
                    }
                    if (tsLong-ibks_time_C > 5){
                        ibks_rssi_C = -200;
                        edTxt_ibks_C.setTextColor(Color.RED);
                    }else{
                        edTxt_ibks_C.setTextColor(Color.BLACK);
                    }
                    if (tsLong-ibks_time_D > 5){
                        ibks_rssi_D = -200;
                        edTxt_ibks_D.setTextColor(Color.RED);
                    }else{
                        edTxt_ibks_D.setTextColor(Color.BLACK);
                    }
                    if (tsLong-ibks_time_E > 5){
                        ibks_rssi_E = -200;
                        edTxt_ibks_E.setTextColor(Color.RED);
                    }else{
                        edTxt_ibks_E.setTextColor(Color.BLACK);
                    }

                    rssis[0] = ibks_rssi_A;
                    rssis[1] = ibks_rssi_B;
                    rssis[2] = ibks_rssi_C;
                    rssis[3] = ibks_rssi_D;
                    rssis[4] = ibks_rssi_E;

                    edTxt_ibks_A.setText(String.valueOf(ibks_rssi_A));
                    edTxt_ibks_B.setText(String.valueOf(ibks_rssi_B));
                    edTxt_ibks_C.setText(String.valueOf(ibks_rssi_C));
                    edTxt_ibks_D.setText(String.valueOf(ibks_rssi_D));
                    edTxt_ibks_E.setText(String.valueOf(ibks_rssi_E));
                }
            });


        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void startScanning() {
        System.out.println("start scanning");

        btScanner.startScan(null, scanSettings, leScanCallback);

//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btScanner.startScan(leScanCallback);
//            }
//        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acceler_x = Math.round(sensorEvent.values[0]*100);
            acceler_y = Math.round(sensorEvent.values[1]*100);
            acceler_z = Math.round(sensorEvent.values[2]*100);

            edTxt_X.setText(String.valueOf(acceler_x/100));
            edTxt_Y.setText(String.valueOf(acceler_y/100));
            edTxt_Z.setText(String.valueOf(acceler_z/100));

            accelers[0] = acceler_x;
            accelers[1] = acceler_y;
            accelers[2] = acceler_z;
        }

        if (mySensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            float steps = sensorEvent.values[0];

            long timestamp = sensorEvent.timestamp;

            edTxt_test.setText(String.valueOf(timestamp));

            edTxt_steps.setText(String.valueOf(steps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorAccelerometerManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorAccelerometerManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onClick(View v) {
        String location = "";
        switch (v.getId()){
            case R.id.btn_A1:
                location = "A1";
                break;
            case R.id.btn_A2:
                location = "A2";
                break;
            case R.id.btn_A3:
                location = "A3";
                break;
            case R.id.btn_A4:
                location = "A4";
                break;
            case R.id.btn_B1:
                location = "B1";
                break;
            case R.id.btn_B2:
                location = "B2";
                break;
            case R.id.btn_B3:
                location = "B3";
                break;
            case R.id.btn_B4:
                location = "B4";
                break;
            case R.id.btn_C1:
                location = "C1";
                break;
            case R.id.btn_C2:
                location = "C2";
                break;
            case R.id.btn_C3:
                location = "C3";
                break;
            case R.id.btn_C4:
                location = "C4";
                break;
            case R.id.btn_D1:
                location = "D1";
                break;
            case R.id.btn_D2:
                location = "D2";
                break;
            case R.id.btn_D3:
                location = "D3";
                break;
            case R.id.btn_D4:
                location = "D4";
                break;
        }
        saveDB(location);
        Toast.makeText(MainActivity.this, location,Toast.LENGTH_LONG).show();
    }

    public void saveDB(String location){
        Mesurement mesurement = new Mesurement(rssis, accelers, steps, System.currentTimeMillis()/1000, location);
        DataSource dataSource = new DataSource(MainActivity.this);

        dataSource.open();
        dataSource.inserMesurement(mesurement);
        dataSource.close();
    }
}
