package com.hospital.dto;

/**
 * Bu sınıf, genel iş yükü özeti raporları için kullanılacak veri transfer nesnesidir.
 * Toplam işlem sayısı ve toplam süre gibi metrikleri içerir.
 */
public class IslemOzeti {
    private String islemTuru;
    private int toplamIslem;
    private int toplamSureDk;
    private double ortalamaSureDk;

    // Constructors
    public IslemOzeti() {}

    /**
     * İşlem özeti oluşturmak için kullanılan constructor.
     * @param islemTuru İşlemin türü veya adı (örn: "Direct İşlemler", "Sabah Vardiyası").
     * @param toplamIslem Toplam işlem sayısı.
     * @param toplamSureDk Toplam süre (dakika).
     */
    public IslemOzeti(String islemTuru, int toplamIslem, int toplamSureDk) {
        this.islemTuru = islemTuru;
        this.toplamIslem = toplamIslem;
        this.toplamSureDk = toplamSureDk;
        this.ortalamaSureDk = (toplamIslem > 0) ? (double) toplamSureDk / toplamIslem : 0;
    }

    // Getters and Setters
    public String getIslemTuru() {
        return islemTuru;
    }

    public void setIslemTuru(String islemTuru) {
        this.islemTuru = islemTuru;
    }

    public int getToplamIslem() {
        return toplamIslem;
    }

    public void setToplamIslem(int toplamIslem) {
        this.toplamIslem = toplamIslem;
        this.ortalamaSureDk = (this.toplamIslem > 0) ? (double) this.toplamSureDk / this.toplamIslem : 0;
    }

    public int getToplamSureDk() {
        return toplamSureDk;
    }

    public void setToplamSureDk(int toplamSureDk) {
        this.toplamSureDk = toplamSureDk;
        this.ortalamaSureDk = (this.toplamIslem > 0) ? (double) this.toplamSureDk / this.toplamIslem : 0;
    }

    public double getOrtalamaSureDk() {
        return ortalamaSureDk;
    }

    public void setOrtalamaSureDk(double ortalamaSureDk) {
        this.ortalamaSureDk = ortalamaSureDk;
    }
}

