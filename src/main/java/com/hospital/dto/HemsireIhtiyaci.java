package com.hospital.dto;

public class HemsireIhtiyaci {
    private String birimAdi;
    private String hemsireKategori;
    private int mevcutHemsireSayisi;
    private double gunlukIsYukuSaat;
    private double gunlukKapasiteSaat; // 8 saatlik vardiya varsayımı
    private int onerilen_HemsireSayisi;
    private double isYukuYuzdesi;

    // Constructors
    public HemsireIhtiyaci() {}

    public HemsireIhtiyaci(String birimAdi, String hemsireKategori, double gunlukIsYukuSaat) {
        this.birimAdi = birimAdi;
        this.hemsireKategori = hemsireKategori;
        this.gunlukIsYukuSaat = gunlukIsYukuSaat;
        this.gunlukKapasiteSaat = 8.0; // Standart vardiya süresi
        hesaplaOneriler();
    }

    private void hesaplaOneriler() {
        // Günlük iş yüküne göre hemşire ihtiyacı hesapla
        this.onerilen_HemsireSayisi = (int) Math.ceil(gunlukIsYukuSaat / gunlukKapasiteSaat);

        // İş yükü yüzdesi hesapla
        if (mevcutHemsireSayisi > 0) {
            double mevcutKapasite = mevcutHemsireSayisi * gunlukKapasiteSaat;
            this.isYukuYuzdesi = (gunlukIsYukuSaat / mevcutKapasite) * 100;
        }
    }

    // Kritik durum kontrolü
    public boolean isKritikDurum() {
        return isYukuYuzdesi > 90; // %90'dan fazla iş yükü
    }

    public boolean isEksikHemsire() {
        return onerilen_HemsireSayisi > mevcutHemsireSayisi;
    }

    // Getters and Setters
    public String getBirimAdi() { return birimAdi; }
    public void setBirimAdi(String birimAdi) { this.birimAdi = birimAdi; }

    public String getHemsireKategori() { return hemsireKategori; }
    public void setHemsireKategori(String hemsireKategori) { this.hemsireKategori = hemsireKategori; }

    public int getMevcutHemsireSayisi() { return mevcutHemsireSayisi; }
    public void setMevcutHemsireSayisi(int mevcutHemsireSayisi) {
        this.mevcutHemsireSayisi = mevcutHemsireSayisi;
        hesaplaOneriler();
    }

    public double getGunlukIsYukuSaat() { return gunlukIsYukuSaat; }
    public void setGunlukIsYukuSaat(double gunlukIsYukuSaat) {
        this.gunlukIsYukuSaat = gunlukIsYukuSaat;
        hesaplaOneriler();
    }

    public double getGunlukKapasiteSaat() { return gunlukKapasiteSaat; }
    public void setGunlukKapasiteSaat(double gunlukKapasiteSaat) {
        this.gunlukKapasiteSaat = gunlukKapasiteSaat;
        hesaplaOneriler();
    }

    public int getOnerilenHemsireSayisi() { return onerilen_HemsireSayisi; }
    public double getIsYukuYuzdesi() { return isYukuYuzdesi; }

    @Override
    public String toString() {
        return String.format("HemsireIhtiyaci{birim='%s', kategori='%s', mevcut=%d, onerilen=%d, yukYuzde=%.1f%%}",
                birimAdi, hemsireKategori, mevcutHemsireSayisi, onerilen_HemsireSayisi, isYukuYuzdesi);
    }
}
