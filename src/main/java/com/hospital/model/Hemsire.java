package com.hospital.model;

import java.sql.Timestamp;
import java.util.Date;

public class Hemsire {
    private int hemsireId;
    private String personelNo;
    private String adSoyad;
    private String tecrubeSeviyesi;
    private int kurumId;
    private boolean aktif;
    private Date iseGirisTarihi;
    private Timestamp olusturmaTarihi;
    private Timestamp guncellemeTarihi;

    // İlişkili objeler için
    private HemsireTecrube tecrube;

    // Constructors
    public Hemsire() {}

    public Hemsire(String personelNo, String adSoyad, String tecrubeSeviyesi) {
        this.personelNo = personelNo;
        this.adSoyad = adSoyad;
        this.tecrubeSeviyesi = tecrubeSeviyesi;
        this.aktif = true;
        this.kurumId = 1; // Default kurum
    }

    // Getters and Setters
    public int getHemsireId() { return hemsireId; }
    public void setHemsireId(int hemsireId) { this.hemsireId = hemsireId; }

    public int getId() { return hemsireId; } // Eski kodlarla uyumluluk için
    public void setId(int id) { this.hemsireId = id; }

    public String getPersonelNo() { return personelNo; }
    public void setPersonelNo(String personelNo) { this.personelNo = personelNo; }

    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }

    public String getTecrubeSeviyesi() { return tecrubeSeviyesi; }
    public void setTecrubeSeviyesi(String tecrubeSeviyesi) { this.tecrubeSeviyesi = tecrubeSeviyesi; }

    public int getKurumId() { return kurumId; }
    public void setKurumId(int kurumId) { this.kurumId = kurumId; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public Date getIseGirisTarihi() { return iseGirisTarihi; }
    public void setIseGirisTarihi(Date iseGirisTarihi) { this.iseGirisTarihi = iseGirisTarihi; }

    public Timestamp getOlusturmaTarihi() { return olusturmaTarihi; }
    public void setOlusturmaTarihi(Timestamp olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }

    public Timestamp getGuncellemeTarihi() { return guncellemeTarihi; }
    public void setGuncellemeTarihi(Timestamp guncellemeTarihi) { this.guncellemeTarihi = guncellemeTarihi; }

    public HemsireTecrube getTecrube() { return tecrube; }
    public void setTecrube(HemsireTecrube tecrube) { this.tecrube = tecrube; }
}