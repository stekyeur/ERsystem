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
            String[] headers = {"Sicil No", "Ad Soyad", "Tecrübe Kategorisi", "Bölüm", "Telefon", "Email", "İşe Giriş Tarihi"};

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

                dataRow.createCell(0).setCellValue(hemsire.getSicilNo());
                dataRow.createCell(1).setCellValue(hemsire.getAdSoyad());
                dataRow.createCell(2).setCellValue(hemsire.getTecrube() != null ?
                        hemsire.getTecrube().getKategoriKodu() + " - " + hemsire.getTecrube().getKategoriAdi() : "");
                dataRow.createCell(3).setCellValue(hemsire.getBolum() != null ? hemsire.getBolum() : "");
                dataRow.createCell(4).setCellValue(hemsire.getTelefon() != null ? hemsire.getTelefon() : "");
                dataRow.createCell(5).setCellValue(hemsire.getEmail() != null ? hemsire.getEmail() : "");

                if (hemsire.getIseGirisTarihi() != null) {
                    Cell dateCell = dataRow.createCell(6);
                    dateCell.setCellValue(hemsire.getIseGirisTarihi());
                    dateCell.setCellStyle(dateStyle);
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
}

