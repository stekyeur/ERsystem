package com.hospital.servlet;

import com.hospital.dto.HemsireIhtiyaci;
import com.hospital.dto.IsYukuOzeti;
import com.hospital.service.IsYukuAnalizeService;
import com.hospital.service.RaporService;
import com.hospital.util.DateUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RaporServlet extends HttpServlet {

    private RaporService raporService;
    private IsYukuAnalizeService analizeService;

    @Override
    public void init() throws ServletException {
        raporService = new RaporService();
        analizeService = new IsYukuAnalizeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        try {
            if ("gunluk".equals(action)) {
                handleGunlukRapor(request, response);
            } else if ("haftalik".equals(action)) {
                handleHaftalikRapor(request, response);
            } else if ("aylik".equals(action)) {
                handleAylikRapor(request, response);
            } else if ("ozel".equals(action)) {
                handleOzelRapor(request, response);
            } else if ("liste".equals(action)) {
                // Rapor listeleme isteği
                int kullaniciId = 1; // Örnek Kullanıcı ID, gerçek uygulamada session'dan alınmalı.
                request.setAttribute("raporlar", raporService.getKullaniciRaporlari(kullaniciId));
                request.getRequestDispatcher("/rapor-listele.jsp").forward(request, response);
            } else {
                // Varsayılan olarak rapor oluşturma formuna yönlendir
                request.getRequestDispatcher("/rapor-form.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Veritabanı hatası", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String raporTipi = request.getParameter("raporTipi");
        String format = request.getParameter("format");
        String baslangicStr = request.getParameter("baslangic");
        String bitisStr = request.getParameter("bitis");
        String raporAdi = request.getParameter("raporAdi");

        int kullaniciId = 1; // Örnek Kullanıcı ID, gerçek uygulamada session'dan alınmalı.

        try {
            LocalDateTime baslangic = LocalDateTime.parse(baslangicStr + "T00:00:00");
            LocalDateTime bitis = (bitisStr != null && !bitisStr.isEmpty()) ?
                    LocalDateTime.parse(bitisStr + "T23:59:59") : baslangic.plusDays(1).minusMinutes(1);

            // Asenkron rapor oluşturma servisini çağır
            raporService.createRaporAsync(raporAdi, raporTipi.toUpperCase(), format,
                            baslangic, bitis, kullaniciId)
                    .thenAccept(rapor -> {
                        System.out.println("Rapor oluşturma işlemi tamamlandı: " + rapor.getRaporAdi());
                    })
                    .exceptionally(ex -> {
                        System.err.println("Rapor oluşturma hatası: " + ex.getMessage());
                        return null;
                    });

            request.setAttribute("success", "Rapor oluşturma işlemi başlatıldı. Raporunuz hazır olduğunda listeye eklenecektir.");

            // Kullanıcıyı rapor listesi sayfasına yönlendir
            response.sendRedirect(request.getContextPath() + "/rapor?action=liste");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Rapor oluşturma hatası: " + e.getMessage());
            request.getRequestDispatcher("/rapor-form.jsp").forward(request, response);
        }
    }
    private void handleGunlukRapor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String tarihStr = request.getParameter("tarih");
        LocalDateTime tarih = tarihStr != null ?
                LocalDateTime.parse(tarihStr + "T00:00:00") : LocalDateTime.now();

        List<IsYukuOzeti> gunlukAnaliz = analizeService.getGunlukIsYukuAnalizi(tarih);

        request.setAttribute("gunlukAnaliz", gunlukAnaliz);
        request.setAttribute("raporTarihi", tarih.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        request.getRequestDispatcher("/gunluk-rapor.jsp").forward(request, response);
    }

    private void handleHaftalikRapor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String haftaStr = request.getParameter("hafta");
        LocalDateTime haftaBaslangici = haftaStr != null ?
                LocalDateTime.parse(haftaStr + "T00:00:00") : DateUtils.getStartOfDaysAgo(7);

        LocalDateTime haftaBitisi = haftaBaslangici.plusDays(7);
        List<HemsireIhtiyaci> hemsireAnalizi = analizeService.hesaplaHemsireIhtiyaci(haftaBaslangici, haftaBitisi);

        request.setAttribute("hemsireAnalizi", hemsireAnalizi);
        request.setAttribute("haftaBaslangici", haftaBaslangici.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        request.setAttribute("haftaBitisi", haftaBitisi.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        request.getRequestDispatcher("/haftalik-rapor.jsp").forward(request, response);
    }

    private void handleAylikRapor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String ayStr = request.getParameter("ay");
        LocalDateTime ayBaslangici = ayStr != null ?
                LocalDateTime.parse(ayStr + "-01T00:00:00") : LocalDateTime.now().withDayOfMonth(1);

        LocalDateTime ayBitisi = ayBaslangici.plusMonths(1);
        List<HemsireIhtiyaci> hemsireAnalizi = analizeService.hesaplaHemsireIhtiyaci(ayBaslangici, ayBitisi);

        request.setAttribute("hemsireAnalizi", hemsireAnalizi);
        request.setAttribute("raporAyi", ayBaslangici.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        request.getRequestDispatcher("/aylik-rapor.jsp").forward(request, response);
    }

    private void handleOzelRapor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String baslangicStr = request.getParameter("baslangic");
        String bitisStr = request.getParameter("bitis");

        LocalDateTime baslangic = baslangicStr != null ?
                LocalDateTime.parse(baslangicStr + "T00:00:00") : DateUtils.getStartOfDaysAgo(30);
        LocalDateTime bitis = bitisStr != null ?
                LocalDateTime.parse(bitisStr + "T23:59:59") : LocalDateTime.now();

        List<HemsireIhtiyaci> hemsireAnalizi = analizeService.hesaplaHemsireIhtiyaci(baslangic, bitis);
        List<IsYukuOzeti> isYukuAnalizi = analizeService.getGunlukIsYukuAnalizi(baslangic);

        request.setAttribute("hemsireAnalizi", hemsireAnalizi);
        request.setAttribute("isYukuAnalizi", isYukuAnalizi);
        request.setAttribute("baslangic", baslangic.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        request.setAttribute("bitis", bitis.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        request.getRequestDispatcher("/ozel-rapor.jsp").forward(request, response);
    }
}