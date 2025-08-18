package com.hospital.model;

import java.util.Date;

public class Hemsire {
    private int hemsireId;
    private String personelNo;
    private String adSoyad;
    private String tecrubeSeviyesi;
    private int kurumId;
    private boolean aktif;
    private Date iseGirisTarihi;

    // Constructors
    public Hemsire() {}

    public Hemsire(int hemsireId, String personelNo, String adSoyad, String tecrubeSeviyesi, int kurumId, boolean aktif, Date iseGirisTarihi) {
        this.hemsireId = hemsireId;
        this.personelNo = personelNo;
        this.adSoyad = adSoyad;
        this.tecrubeSeviyesi = tecrubeSeviyesi;
        this.kurumId = kurumId;
        this.aktif = aktif;
        this.iseGirisTarihi = iseGirisTarihi;
    }

    // Getters and Setters for all attributes
    public int getHemsireId() {
        return hemsireId;
    }

    public void setHemsireId(int hemsireId) {
        this.hemsireId = hemsireId;
    }

    public String getPersonelNo() {
        return personelNo;
    }

    public void setPersonelNo(String personelNo) {
        this.personelNo = personelNo;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getTecrubeSeviyesi() {
        return tecrubeSeviyesi;
    }

    public void setTecrubeSeviyesi(String tecrubeSeviyesi) {
        this.tecrubeSeviyesi = tecrubeSeviyesi;
    }

    public int getKurumId() {
        return kurumId;
    }

    public void setKurumId(int kurumId) {
        this.kurumId = kurumId;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public Date getIseGirisTarihi() {
        return iseGirisTarihi;
    }

    public void setIseGirisTarihi(Date iseGirisTarihi) {
        this.iseGirisTarihi = iseGirisTarihi;
    }
}