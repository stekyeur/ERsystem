package com.hospital.model;

public class IslemTuru {
    private int id;
    private String islemKodu;
    private String islemAdi;
    private String islemTipi; // DIRECT, INDIRECT
    private int standartSureDk;
    private String aciklama;
    private boolean aktif;

    // Constructors
    public IslemTuru() {}

    public IslemTuru(String islemKodu, String islemAdi, String islemTipi, int standartSureDk) {
        this.islemKodu = islemKodu;
        this.islemAdi = islemAdi;
        this.islemTipi = islemTipi;
        this.standartSureDk = standartSureDk;
        this.aktif = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getIslemKodu() { return islemKodu; }
    public void setIslemKodu(String islemKodu) { this.islemKodu = islemKodu; }
    public String getIslemAdi() { return islemAdi; }
    public void setIslemAdi(String islemAdi) { this.islemAdi = islemAdi; }
    public String getIslemTipi() { return islemTipi; }
    public void setIslemTipi(String islemTipi) { this.islemTipi = islemTipi; }
    public int getStandartSureDk() { return standartSureDk; }
    public void setStandartSureDk(int standartSureDk) { this.standartSureDk = standartSureDk; }
    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}
