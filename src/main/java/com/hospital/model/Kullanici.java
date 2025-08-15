package com.hospital.model;

public class Kullanici {
    private int kullaniciId;
    private String kullaniciAdi;
    private String sifre;
    private String rol;

    // Constructors
    public Kullanici() {}

    public Kullanici(String kullaniciAdi, String sifre, String rol) {
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.rol = rol;
    }

    // Getters and Setters
    public int getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(int kullaniciId) { this.kullaniciId = kullaniciId; }

    public int getId() { return kullaniciId; } // Eski kodlarla uyumluluk için
    public void setId(int id) { this.kullaniciId = id; }

    public String getKullaniciAdi() { return kullaniciAdi; }
    public void setKullaniciAdi(String kullaniciAdi) { this.kullaniciAdi = kullaniciAdi; }

    public String getSifre() { return sifre; }
    public void setSifre(String sifre) { this.sifre = sifre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
