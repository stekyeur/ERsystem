package com.hospital.model;

import java.time.LocalDateTime;

public class Islem {
    private int id;
    private String islemAdi;
    private String aciklama;
    private boolean isDirect; // true: Direct, false: Indirect
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Islem() {}

    public Islem(String islemAdi, String aciklama, boolean isDirect) {
        this.islemAdi = islemAdi;
        this.aciklama = aciklama;
        this.isDirect = isDirect;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIslemAdi() { return islemAdi; }
    public void setIslemAdi(String islemAdi) { this.islemAdi = islemAdi; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public boolean isDirect() { return isDirect; }
    public void setDirect(boolean isDirect) { this.isDirect = isDirect; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return String.format("Islem{id=%d, islemAdi='%s', isDirect=%s}",
                id, islemAdi, isDirect);
    }
}
