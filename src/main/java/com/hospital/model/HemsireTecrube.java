package com.hospital.model;

public class HemsireTecrube {
    private int id;
    private String kategoriKodu;
    private String kategoriAdi;
    private String aciklama;
    private boolean aktif;

    // Constructors
    public HemsireTecrube() {}

    public HemsireTecrube(String kategoriKodu, String kategoriAdi) {
        this.kategoriKodu = kategoriKodu;
        this.kategoriAdi = kategoriAdi;
        this.aktif = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getKategoriKodu() { return kategoriKodu; }
    public void setKategoriKodu(String kategoriKodu) { this.kategoriKodu = kategoriKodu; }
    public String getKategoriAdi() { return kategoriAdi; }
    public void setKategoriAdi(String kategoriAdi) { this.kategoriAdi = kategoriAdi; }
    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}

