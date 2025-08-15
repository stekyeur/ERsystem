// ExcelExporter.java (Güncellenmiş)
package com.hospital.util;

import com.hospital.model.Hemsire;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    public static boolean createIslemRaporu(List<Object[]> veriler, String dosyaYolu) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("İşlem Raporu");

            // Başlık satırı
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Hemşire Kategorisi", "İşlem", "Tip", "Kritiklik", "Toplam İşlem", "Ortalama Süre (dk)"};

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Veri satırları
            int rowNum = 1;
            for (Object[] row : veriler) {
                Row dataRow = sheet.createRow(rowNum++);

                for (int i = 0; i < row.length; i++) {
                    Cell cell = dataRow.createCell(i);
                    if (row[i] instanceof Number) {
                        cell.setCellValue(((Number) row[i]).doubleValue());
                    } else {
                        cell.setCellValue(row[i] != null ? row[i].toString() : "");
                    }
                }
            }

            // Sütun genişliklerini ayarla
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Dosyayı kaydet
            try (FileOutputStream outputStream = new FileOutputStream(dosyaYolu)) {
                workbook.write(outputStream);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createHemsireListesi(List<Hemsire> hemsireler, String dosyaYolu) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Hemşire Listesi");

            // Başlık satırı
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Personel No", "Ad Soyad", "Tecrübe Seviyesi", "Kurum ID", "İşe Giriş Tarihi", "Aktif"};

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Tarih formatı için stil
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));

            // Veri satırları
            int rowNum = 1;
            for (Hemsire hemsire : hemsireler) {
                Row dataRow = sheet.createRow(rowNum++);

                dataRow.createCell(0).setCellValue(hemsire.getPersonelNo() != null ? hemsire.getPersonelNo() : "");
                dataRow.createCell(1).setCellValue(hemsire.getAdSoyad() != null ? hemsire.getAdSoyad() : "");
                dataRow.createCell(2).setCellValue(hemsire.getTecrubeSeviyesi() != null ? hemsire.getTecrubeSeviyesi() : "");
                dataRow.createCell(3).setCellValue(hemsire.getKurumId());

                // İşe giriş tarihi
                if (hemsire.getIseGirisTarihi() != null) {
                    Cell dateCell = dataRow.createCell(4);
                    dateCell.setCellValue(hemsire.getIseGirisTarihi());
                    dateCell.setCellStyle(dateStyle);
                } else {
                    dataRow.createCell(4).setCellValue("");
                }

                dataRow.createCell(5).setCellValue(hemsire.isAktif() ? "Evet" : "Hayır");
            }

            // Sütun genişliklerini ayarla
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Dosyayı kaydet
            try (FileOutputStream outputStream = new FileOutputStream(dosyaYolu)) {
                workbook.write(outputStream);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
