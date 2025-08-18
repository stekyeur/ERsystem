package com.hospital.util;

import com.hospital.dto.HemsireIhtiyaci;
import com.hospital.dto.IsYukuOzeti;
import com.hospital.model.Hemsire;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

// Not: Apache POI kütüphanesi gerekli (pom.xml'e eklenecek)
/*
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
</dependency>
*/

public class ExcelExporter {

    // İş yükü özetini Excel'e aktar
    public static void exportIsYukuOzeti(List<IsYukuOzeti> data, String fileName) throws IOException {
        // Apache POI kullanarak Excel dosyası oluştur
        // Implementasyon detayları burada olacak
        System.out.println("Excel raporu oluşturuluyor: " + fileName);
    }

    // Hemşire ihtiyacı raporunu Excel'e aktar
    public static void exportHemsireIhtiyaci(List<HemsireIhtiyaci> data, String fileName) throws IOException {
        // Apache POI kullanarak Excel dosyası oluştur
        System.out.println("Hemşire ihtiyacı raporu oluşturuluyor: " + fileName);
    }
}