package com.hospital.servlet;

import com.hospital.dao.BirimDAO;
import com.hospital.dao.IslemDAO;
import com.hospital.dao.KayitIslemDAO;
import com.hospital.model.Birim;
import com.hospital.model.Islem;
import com.hospital.model.KayitIslem;
import com.hospital.util.ValidationUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class IslemKayitServlet extends HttpServlet {

    private KayitIslemDAO kayitIslemDAO;
    private IslemDAO islemDAO;
    private BirimDAO birimDAO;

    @Override
    public void init() throws ServletException {
        kayitIslemDAO = new KayitIslemDAO();
        islemDAO = new IslemDAO();
        birimDAO = new BirimDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Islem> islemler = islemDAO.getAktifIslemler();
            List<Birim> birimler = birimDAO.getAktifBirimler();

            request.setAttribute("islemler", islemler);
            request.setAttribute("birimler", birimler);

            request.getRequestDispatcher("islem-kayit.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Veritabanı hatası", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Form verilerini al
            String hemsireKategori = request.getParameter("hemsireKategori");
            String islemIdStr = request.getParameter("islemId");
            String birimIdStr = request.getParameter("birimId");
            String gercekSureStr = request.getParameter("gercekSure");
            String hastaDurumu = request.getParameter("hastaDurumu");
            String notlar = request.getParameter("notlar");

            // Validasyon
            if (!ValidationUtils.isValidHemsireKategori(hemsireKategori)) {
                request.setAttribute("error", "Geçersiz hemşire kategorisi");
                doGet(request, response);
                return;
            }

            if (!ValidationUtils.isValidHastaDurumu(hastaDurumu)) {
                hastaDurumu = "normal"; // Default değer
            }

            int islemId = Integer.parseInt(islemIdStr);
            int birimId = Integer.parseInt(birimIdStr);
            int gercekSure = Integer.parseInt(gercekSureStr);

            if (!ValidationUtils.isPositiveInteger(gercekSure)) {
                request.setAttribute("error", "Süre pozitif bir değer olmalıdır");
                doGet(request, response);
                return;
            }

            // Yeni kayıt oluştur
            KayitIslem kayit = new KayitIslem(hemsireKategori, islemId, birimId, gercekSure, hastaDurumu);
            kayit.setKayitZamani(LocalDateTime.now());
            kayit.setNotlar(notlar);


            // Veritabanına kaydet
            int kayitId = kayitIslemDAO.insertKayitIslem(kayit);

            if (kayitId > 0) {
                // `redirect` sonrası hata veya başarı mesajı göstermek için
                // Flash attribute kullanılabilir veya sessiona set edilebilir.
                // Şimdilik sadece yönlendirme yapıldı.
                response.sendRedirect("dashboard");
            } else {
                request.setAttribute("error", "Kayıt sırasında hata oluştu");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Geçersiz sayısal değer");
            doGet(request, response);
        } catch (SQLException e) {
            throw new ServletException("Veritabanı hatası", e);
        }
    }
}