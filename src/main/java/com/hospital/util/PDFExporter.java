package com.hospital.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PDFExporter {

    public static boolean createIslemRaporu(List<Object[]> veriler, String dosyaYolu) {
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(dosyaYolu));

            document.open();

            // Başlık
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Acil Servis İşlem Raporu", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Tarih
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Paragraph date = new Paragraph("Rapor Tarihi: " + sdf.format(new java.util.Date()), dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(20);
            document.add(date);

            // Tablo
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Tablo başlıkları
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            String[] headers = {"Hemşire Kategorisi", "İşlem", "Tip", "Kritiklik", "Toplam İşlem", "Ortalama Süre (dk)"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Tablo verileri
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            for (Object[] row : veriler) {
                for (Object cell : row) {
                    PdfPCell pdfCell = new PdfPCell(new Phrase(cell != null ? cell.toString() : "", dataFont));
                    pdfCell.setPadding(5);
                    pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(pdfCell);
                }
            }

            document.add(table);
            document.close();

            return true;

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createHemsireListesi(List<com.hospital.model.Hemsire> hemsireler, String dosyaYolu) {
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(dosyaYolu));

            document.open();

            // Başlık
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Hemşire Listesi Raporu", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Tarih
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Paragraph date = new Paragraph("Rapor Tarihi: " + sdf.format(new java.util.Date()), dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(20);
            document.add(date);

            // Tablo
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Tablo başlıkları
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            String[] headers = {"Personel No", "Ad Soyad", "Tecrübe Seviyesi", "Kurum ID", "İşe Giriş Tarihi", "Aktif"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Tablo verileri
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            for (com.hospital.model.Hemsire hemsire : hemsireler) {
                // Personel No
                PdfPCell cell1 = new PdfPCell(new Phrase(hemsire.getPersonelNo() != null ? hemsire.getPersonelNo() : "", dataFont));
                cell1.setPadding(5);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell1);

                // Ad Soyad
                PdfPCell cell2 = new PdfPCell(new Phrase(hemsire.getAdSoyad() != null ? hemsire.getAdSoyad() : "", dataFont));
                cell2.setPadding(5);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell2);

                // Tecrübe Seviyesi
                PdfPCell cell3 = new PdfPCell(new Phrase(hemsire.getTecrubeSeviyesi() != null ? hemsire.getTecrubeSeviyesi() : "", dataFont));
                cell3.setPadding(5);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell3);

                // Kurum ID
                PdfPCell cell4 = new PdfPCell(new Phrase(String.valueOf(hemsire.getKurumId()), dataFont));
                cell4.setPadding(5);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell4);

                // İşe Giriş Tarihi
                String tarihStr = "";
                if (hemsire.getIseGirisTarihi() != null) {
                    tarihStr = dateFormat.format(hemsire.getIseGirisTarihi());
                }
                PdfPCell cell5 = new PdfPCell(new Phrase(tarihStr, dataFont));
                cell5.setPadding(5);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell5);

                // Aktif
                PdfPCell cell6 = new PdfPCell(new Phrase(hemsire.isAktif() ? "Evet" : "Hayır", dataFont));
                cell6.setPadding(5);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell6);
            }

            document.add(table);
            document.close();

            return true;

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}