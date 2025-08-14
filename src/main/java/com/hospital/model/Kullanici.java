package com.hospital.model;

import java.sql.Timestamp;
import java.util.Date;

public class Kullanici {
    private int id;
    private String username;
    private String password;
    private String adSoyad;
    private String email;
    private String rol;
    private boolean aktif;
    private Date kayitTarihi;

    // Constructors
    public Kullanici() {}

    public Kullanici(String username, String password, String adSoyad, String email) {
        this.username = username;
        this.password = password;
        this.adSoyad = adSoyad;
        this.email = email;
        this.rol = "USER";
        this.aktif = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
    public Date getKayitTarihi() { return kayitTarihi; }
    public void setKayitTarihi(Timestamp kayitTarihi) { this.kayitTarihi = kayitTarihi; }
}

