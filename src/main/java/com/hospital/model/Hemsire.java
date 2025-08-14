package com.hospital.model;

import java.util.Date;

public class Hemsire {
    private int id;
    private String sicilNo;
    private String adSoyad;
    private int tecrubeId;
    private String bolum;
    private String telefon;
    private String email;
    private Date iseGirisTarihi;
    private boolean aktif;

    // İlişkili objeler
    private HemsireTecrube tecrube;

    // Constructors
    public Hemsire() {}

    public Hemsire(String sicilNo, String adSoyad, int tecrubeId) {
        this.sicilNo = sicilNo;
        this.adSoyad = adSoyad;
        this.tecrubeId = tecrubeId;
        this.aktif = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSicilNo() { return sicilNo; }
    public void setSicilNo(String sicilNo) { this.sicilNo = sicilNo; }
    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }
    public int getTecrubeId() { return tecrubeId; }
    public void setTecrubeId(int tecrubeId) { this.tecrubeId = tecrubeId; }
    public String getBolum() { return bolum; }
    public void setBolum(String bolum) { this.bolum = bolum; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getIseGirisTarihi() { return iseGirisTarihi; }
    public void setIseGirisTarihi(Date iseGirisTarihi) { this.iseGirisTarihi = iseGirisTarihi; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
    public HemsireTecrube getTecrube() { return tecrube; }
    public void setTecrube(HemsireTecrube tecrube) { this.tecrube = tecrube; }
}
