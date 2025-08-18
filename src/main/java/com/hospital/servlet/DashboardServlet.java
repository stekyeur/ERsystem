package com.hospital.servlet;

import com.hospital.dao.HemsireDAO;
import com.hospital.dto.HemsireIhtiyaci;
import com.hospital.dto.IsYukuOzeti;
import com.hospital.model.KayitIslem;
import com.hospital.service.IsYukuAnalizeService;
import com.hospital.util.DateUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardServlet extends HttpServlet {

    private IsYukuAnalizeService analizeService;
    private HemsireDAO hemsireDAO;

    @Override
    public void init() throws ServletException {
        analizeService = new IsYukuAnalizeService();
        hemsireDAO = new HemsireDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Bugünkü iş yükü özeti
            List<IsYukuOzeti> gunlukOzet = analizeService.getGunlukIsYukuAnalizi(LocalDateTime.now());
            request.setAttribute("gunlukOzet", gunlukOzet);

            // Bu haftaki hemşire ihtiyacı
            LocalDateTime haftaBaslangici = DateUtils.getStartOfDaysAgo(7);
            LocalDateTime bugun = LocalDateTime.now();
            List<HemsireIhtiyaci> hemsireIhtiyaci = analizeService.hesaplaHemsireIhtiyaci(haftaBaslangici, bugun);
            request.setAttribute("hemsireIhtiyaci", hemsireIhtiyaci);

            // Kritik durumlar
            List<KayitIslem> kritikDurumlar = analizeService.getKritikDurumAnalizi(DateUtils.getStartOfToday(), DateUtils.getEndOfToday());
            request.setAttribute("kritikDurumlar", kritikDurumlar);

            // Hemşire sayılarını çekin ve request'e ekleyin
            int toplamHemsire = hemsireDAO.getToplamHemsireSayisi();
            int tecrubeli = hemsireDAO.getHemsireSayisiByKategori("Tecrübeli");
            int ortaTecrubeli = hemsireDAO.getHemsireSayisiByKategori("Orta Tecrübeli");
            int tecrubesiz = hemsireDAO.getHemsireSayisiByKategori("Tecrübesiz");

            request.setAttribute("toplamHemsire", toplamHemsire);
            request.setAttribute("tecrubeliSayisi", tecrubeli);
            request.setAttribute("ortaTecrubeliSayisi", ortaTecrubeli);
            request.setAttribute("tecrubesizSayisi", tecrubesiz);

            // Tarih bilgisini java.util.Date olarak ekleyin
            request.setAttribute("bugun", new java.util.Date());

            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            // Hata durumunda, kullanıcının görebileceği bir hata mesajı ayarlayın
            request.setAttribute("error", "Veritabanı hatası: " + e.getMessage());
            // Loglama yapın
            e.printStackTrace();
            // Gerekirse başka bir JSP'ye yönlendirin
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}