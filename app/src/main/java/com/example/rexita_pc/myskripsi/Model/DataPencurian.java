package com.example.rexita_pc.myskripsi.Model;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class DataPencurian implements Serializable {
    private String Waktu;
    private String Tanggal;
    private String Kejadian;
    private String Korban;
    private String Lokasi;
    private String Kecamatan;
    private int Status;
    private String Kerugian;
    private String Interval;
    private double Rangking;
    private String Key;

    public DataPencurian() {
    }

    public DataPencurian(String waktu, String tanggal, String kejadian, String korban, String lokasi, String kecamatan,
                         int status, String kerugian, String interval, double rangking, String key) {
        Waktu = waktu;
        Tanggal = tanggal;
        Kejadian = kejadian;
        Korban = korban;
        Lokasi = lokasi;
        Kecamatan = kecamatan;
        Status = status;
        Kerugian = kerugian;
        Interval = interval;
        Rangking = rangking;
        Key = key;
    }

    public double getRangking() {

        return Rangking;
    }

    public String getWaktu() {
        return Waktu;
    }

    public void setWaktu(String waktu) {
        Waktu = waktu;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }

    public String getKejadian() {
        return Kejadian;
    }

    public void setKejadian(String kejadian) {
        Kejadian = kejadian;
    }

    public String getKorban() {
        return Korban;
    }

    public void setKorban(String korban) {
        Korban = korban;
    }

    public String getLokasi() {
        return Lokasi;
    }

    public void setLokasi(String lokasi) {
        Lokasi = lokasi;
    }

    public String getKecamatan() {
        return Kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        Kecamatan = kecamatan;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getKerugian() {
        return Kerugian;
    }

    public void setKerugian(String kerugian) {
        Kerugian = kerugian;
    }

    public String getInterval() {
        return Interval;
    }

    public void setInterval(String interval) {
        Interval = interval;
    }

    public void setRangking(double rangking) {
        Rangking = rangking;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
