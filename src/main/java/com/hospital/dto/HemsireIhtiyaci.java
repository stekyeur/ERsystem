package com.hospital.dto;

import java.sql.Date;

/**
 * Bu sınıf, günlük hemşire ihtiyacı raporu için kullanılacak veri transfer nesnesidir.
 * Her bir tarih için gerekli ve mevcut hemşire sayısını tutar.
 */
public class HemsireIhtiyaci {

    private Date tarih;
    private int gerekliHemsire;
    private int mevcutHemsire;

    // Constructors
    public HemsireIhtiyaci() {}

    public HemsireIhtiyaci(Date tarih, int gerekliHemsire, int mevcutHemsire) {
        this.tarih = tarih;
        this.gerekliHemsire = gerekliHemsire;
        this.mevcutHemsire = mevcutHemsire;
    }

    // Getters and Setters
    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public int getGerekliHemsire() {
        return gerekliHemsire;
    }

    public void setGerekliHemsire(int gerekliHemsire) {
        this.gerekliHemsire = gerekliHemsire;
    }

    public int getMevcutHemsire() {
        return mevcutHemsire;
    }

    public void setMevcutHemsire(int mevcutHemsire) {
        this.mevcutHemsire = mevcutHemsire;
    }
}
