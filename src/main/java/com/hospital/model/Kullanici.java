package com.hospital.model;

import java.sql.Timestamp;
import java.util.Date;

public class Kullanici {
    private String username;
    private String adSoyad;
    private String password;
    private Date acilisTarihi;
    private Date kapanisTarihi;
    private boolean durum;

    // Constructors
    public Kullanici() {}

    public Kullanici(String username, String adSoyad, String password) {
        this.username = username;
        this.adSoyad = adSoyad;
        this.password = password;
        this.durum = true; // Yeni kullanıcılar varsayılan olarak aktif
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }
    public Date getAcilisTarihi() { return acilisTarihi; }
    public void setAcilisTarihi(Timestamp acilisTarihi) { this.acilisTarihi = acilisTarihi; }
    public Date getKapanisTarihi() { return kapanisTarihi; }
    public void setKapanisTarihi(Date kapanisTarihi) { this.kapanisTarihi = kapanisTarihi; }
    public boolean isDurum() { return durum; }
    public void setDurum(boolean durum) { this.durum = durum; }
}