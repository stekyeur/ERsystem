package com.hospital.model;

public class Birim {
    private int id;
    private String birimKodu;
    private String birimAdi;
    private int kapasite;
    private String aciklama;
    private boolean aktif;

    // Constructors
    public Birim() {}

    public Birim(String birimKodu, String birimAdi, int kapasite) {
        this.birimKodu = birimKodu;
        this.birimAdi = birimAdi;
        this.kapasite = kapasite;
        this.aktif = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBirimKodu() { return birimKodu; }
    public void setBirimKodu(String birimKodu) { this.birimKodu = birimKodu; }
    public String getBirimAdi() { return birimAdi; }
    public void setBirimAdi(String birimAdi) { this.birimAdi = birimAdi; }
    public int getKapasite() { return kapasite; }
    public void setKapasite(int kapasite) { this.kapasite = kapasite; }
    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}
