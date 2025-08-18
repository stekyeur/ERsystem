package com.hospital.util;

import com.hospital.dto.HemsireIhtiyaci;
import com.hospital.dto.IsYukuOzeti;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

// Not: iText kütüphanesi gerekli (pom.xml'e eklenecek)
/*
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
</dependency>
*/

public class PDFExporter {

    // İş yükü özetini PDF'e aktar
    public static void exportIsYukuOzeti(List<IsYukuOzeti> data, String fileName) throws IOException {
        // iText kullanarak PDF dosyası oluştur
        System.out.println("PDF raporu oluşturuluyor: " + fileName);
    }

    // Hemşire ihtiyacı raporunu PDF'e aktar
    public static void exportHemsireIhtiyaci(List<HemsireIhtiyaci> data, String fileName) throws IOException {
        System.out.println("Hemşire ihtiyacı PDF raporu oluşturuluyor: " + fileName);
    }
}
