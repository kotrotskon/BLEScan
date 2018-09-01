package com.kotrots.blescan;

public class Mesurement {

    private int rssi_ibksA;
    private int rssi_ibksB;
    private int rssi_ibksC;
    private int rssi_ibksD;
    private int rssi_ibksE;
    private double acceler_X;
    private double acceler_Y;
    private double acceler_Z;
    private float steps;
    private long timestamp;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Mesurement(int[] rssis, double[] accelers, float steps, long timestamp, String location){
        this.rssi_ibksA = rssis[0];
        this.rssi_ibksB = rssis[1];
        this.rssi_ibksC = rssis[2];
        this.rssi_ibksD = rssis[3];
        this.rssi_ibksE = rssis[4];

        this.acceler_X = accelers[0];
        this.acceler_Y = accelers[1];
        this.acceler_Z = accelers[2];

        this.steps = steps;
        this.timestamp = timestamp;

        this.location = location;

    }

    public int getRssi_ibksA() {
        return rssi_ibksA;
    }

    public void setRssi_ibksA(int rssi_ibksA) {
        this.rssi_ibksA = rssi_ibksA;
    }

    public int getRssi_ibksB() {
        return rssi_ibksB;
    }

    public void setRssi_ibksB(int rssi_ibksB) {
        this.rssi_ibksB = rssi_ibksB;
    }

    public int getRssi_ibksC() {
        return rssi_ibksC;
    }

    public void setRssi_ibksC(int rssi_ibksC) {
        this.rssi_ibksC = rssi_ibksC;
    }

    public int getRssi_ibksD() {
        return rssi_ibksD;
    }

    public void setRssi_ibksD(int rssi_ibksD) {
        this.rssi_ibksD = rssi_ibksD;
    }

    public int getRssi_ibksE() {
        return rssi_ibksE;
    }

    public void setRssi_ibksE(int rssi_ibksE) {
        this.rssi_ibksE = rssi_ibksE;
    }

    public double getAcceler_X() {
        return acceler_X;
    }

    public void setAcceler_X(double acceler_X) {
        this.acceler_X = acceler_X;
    }

    public double getAcceler_Y() {
        return acceler_Y;
    }

    public void setAcceler_Y(double acceler_Y) {
        this.acceler_Y = acceler_Y;
    }

    public double getAcceler_Z() {
        return acceler_Z;
    }

    public void setAcceler_Z(double acceler_Z) {
        this.acceler_Z = acceler_Z;
    }

    public double getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
