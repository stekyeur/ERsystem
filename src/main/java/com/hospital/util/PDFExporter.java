package com.hospital.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
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
            Paragraph date = new Paragraph("Rapor Tarihi: " + DateUtils.formatDateTime(new java.util.Date()), dateFont);
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
}

