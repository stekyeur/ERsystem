package com.hospital.model;

import java.time.LocalDateTime;

public class BirimIslem {
    private int id;
    private int birimId;
    private int islemId;
    private int t1Sure; // Tecrübesiz hemşire süresi (dakika)
    private int t2Sure; // Orta tecrübeli hemşire süresi (dakika)
    private int t3Sure; // Tecrübeli hemşire süresi (dakika)
    private boolean aktif;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Navigation properties (lazy loading için)
    private Birim birim;
    private Islem islem;

    // Constructors
    public BirimIslem() {}

    public BirimIslem(int birimId, int islemId, int t1Sure, int t2Sure, int t3Sure) {
        this.birimId = birimId;
        this.islemId = islemId;
        this.t1Sure = t1Sure;
        this.t2Sure = t2Sure;
        this.t3Sure = t3Sure;
        this.aktif = true;
    }

    // Hemşire kategorisine göre süre döndüren method
    public int getSureByKategori(String kategori) {
        switch (kategori.toLowerCase()) {
            case "t1": return t1Sure;
            case "t2": return t2Sure;
            case "t3": return t3Sure;
            default: return t2Sure; // Default olarak orta tecrübeli
        }
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBirimId() { return birimId; }
    public void setBirimId(int birimId) { this.birimId = birimId; }

    public int getIslemId() { return islemId; }
    public void setIslemId(int islemId) { this.islemId = islemId; }

    public int getT1Sure() { return t1Sure; }
    public void setT1Sure(int t1Sure) { this.t1Sure = t1Sure; }

    public int getT2Sure() { return t2Sure; }
    public void setT2Sure(int t2Sure) { this.t2Sure = t2Sure; }

    public int getT3Sure() { return t3Sure; }
    public void setT3Sure(int t3Sure) { this.t3Sure = t3Sure; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Birim getBirim() { return birim; }
    public void setBirim(Birim birim) { this.birim = birim; }

    public Islem getIslem() { return islem; }
    public void setIslem(Islem islem) { this.islem = islem; }

    @Override
    public String toString() {
        return String.format("BirimIslem{birimId=%d, islemId=%d, t1=%d, t2=%d, t3=%d}",
                birimId, islemId, t1Sure, t2Sure, t3Sure);
    }
}
