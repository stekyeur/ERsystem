package com.hospital.dto;

/**
 * Bu sınıf, günlük iş yükü raporu için kullanılacak veri transfer nesnesidir.
 * Saatlik aralıklarda hasta sayısı, bekleme süresi ve diğer metrikleri tutar.
 */
public class IsYukuOzeti {
    private String saatAraligi;
    private int hastaSayisi;
    private int ortalamaBeklemeSuresi; // dakika cinsinden

    // Constructors
    public IsYukuOzeti() {}

    /**
     * Saatlik iş yükü özeti oluşturmak için kullanılan constructor.
     * @param saatAraligi Saat aralığı (örn: "08:00 - 09:00").
     * @param hastaSayisi Bu saat aralığındaki hasta sayısı.
     * @param ortalamaBeklemeSuresi Ortalama bekleme süresi (dakika).
     */
    public IsYukuOzeti(String saatAraligi, int hastaSayisi, int ortalamaBeklemeSuresi) {
        this.saatAraligi = saatAraligi;
        this.hastaSayisi = hastaSayisi;
        this.ortalamaBeklemeSuresi = ortalamaBeklemeSuresi;
    }

    // Getters and Setters
    public String getSaatAraligi() {
        return saatAraligi;
    }

    public void setSaatAraligi(String saatAraligi) {
        this.saatAraligi = saatAraligi;
    }

    public int getHastaSayisi() {
        return hastaSayisi;
    }

    public void setHastaSayisi(int hastaSayisi) {
        this.hastaSayisi = hastaSayisi;
    }

    public int getOrtalamaBeklemeSuresi() {
        return ortalamaBeklemeSuresi;
    }

    public void setOrtalamaBeklemeSuresi(int ortalamaBeklemeSuresi) {
        this.ortalamaBeklemeSuresi = ortalamaBeklemeSuresi;
    }
}
