package com.hospital.service;

import com.hospital.dto.HemsireIhtiyaci;
import com.hospital.dto.IsYukuOzeti;
import com.hospital.util.ExcelExporter;
import com.hospital.util.PDFExporter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RaporService {

    private IsYukuAnalizeService analizeService;

    public RaporService() {
        this.analizeService = new IsYukuAnalizeService();
    }

    // Günlük rapor oluştur
    public void createGunlukRapor(LocalDateTime tarih, String format) throws SQLException, IOException {
        List<IsYukuOzeti> gunlukAnaliz = analizeService.getGunlukIsYukuAnalizi(tarih);

        String tarihStr = tarih.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fileName = "gunluk_rapor_" + tarihStr;

        if ("excel".equalsIgnoreCase(format)) {
            ExcelExporter.exportIsYukuOzeti(gunlukAnaliz, fileName + ".xlsx");
        } else if ("pdf".equalsIgnoreCase(format)) {
            PDFExporter.exportIsYukuOzeti(gunlukAnaliz, fileName + ".pdf");
        }
    }

    // Haftalık rapor oluştur
    public void createHaftalikRapor(LocalDateTime haftaBaslangici, String format) throws SQLException, IOException {
        LocalDateTime haftaBitisi = haftaBaslangici.plusDays(7);
        List<HemsireIhtiyaci> hemsireAnalizi = analizeService.hesaplaHemsireIhtiyaci(haftaBaslangici, haftaBitisi);

        String tarihStr = haftaBaslangici.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fileName = "haftalik_rapor_" + tarihStr;

        if ("excel".equalsIgnoreCase(format)) {
            ExcelExporter.exportHemsireIhtiyaci(hemsireAnalizi, fileName + ".xlsx");
        } else if ("pdf".equalsIgnoreCase(format)) {
            PDFExporter.exportHemsireIhtiyaci(hemsireAnalizi, fileName + ".pdf");
        }
    }

    // Aylık kapsamlı rapor oluştur
    public void createAylikRapor(LocalDateTime ayBaslangici, String format) throws SQLException, IOException {
        LocalDateTime ayBitisi = ayBaslangici.plusMonths(1);

        // Hem iş yükü hem de hemşire ihtiyacı analizleri
        List<HemsireIhtiyaci> hemsireAnalizi = analizeService.hesaplaHemsireIhtiyaci(ayBaslangici, ayBitisi);

        String tarihStr = ayBaslangici.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String fileName = "aylik_rapor_" + tarihStr;

        if ("excel".equalsIgnoreCase(format)) {
            ExcelExporter.exportHemsireIhtiyaci(hemsireAnalizi, fileName + ".xlsx");
        } else if ("pdf".equalsIgnoreCase(format)) {
            PDFExporter.exportHemsireIhtiyaci(hemsireAnalizi, fileName + ".pdf");
        }
    }

    // Özel tarih aralığı raporu
    public void createOzelRapor(LocalDateTime baslangic, LocalDateTime bitis, String raporTipi, String format) throws SQLException, IOException {
        String fileName = "ozel_rapor_" + baslangic.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "_" +
                bitis.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        switch (raporTipi.toLowerCase()) {
            case "is_yuku":
                // İş yükü analizi için tarihe göre ortalama al
                List<IsYukuOzeti> isYukuAnaliz = analizeService.getGunlukIsYukuAnalizi(baslangic);
                if ("excel".equalsIgnoreCase(format)) {
                    ExcelExporter.exportIsYukuOzeti(isYukuAnaliz, fileName + "_is_yuku.xlsx");
                } else {
                    PDFExporter.exportIsYukuOzeti(isYukuAnaliz, fileName + "_is_yuku.pdf");
                }
                break;

            case "hemsire_ihtiyaci":
                List<HemsireIhtiyaci> hemsireIhtiyaci = analizeService.hesaplaHemsireIhtiyaci(baslangic, bitis);
                if ("excel".equalsIgnoreCase(format)) {
                    ExcelExporter.exportHemsireIhtiyaci(hemsireIhtiyaci, fileName + "_hemsire.xlsx");
                } else {
                    PDFExporter.exportHemsireIhtiyaci(hemsireIhtiyaci, fileName + "_hemsire.pdf");
                }
                break;

            default:
                throw new IllegalArgumentException("Geçersiz rapor tipi: " + raporTipi);
        }
    }
}

