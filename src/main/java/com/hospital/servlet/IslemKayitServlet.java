package com.hospital.servlet;

import com.google.gson.Gson; // Gson kütüphanesini ekleyin.
import com.hospital.dao.BirimDAO;
import com.hospital.dao.HemsireDAO; // Yeni eklendi
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
    private HemsireDAO hemsireDAO; // Yeni eklendi

    @Override
    public void init() throws ServletException {
        kayitIslemDAO = new KayitIslemDAO();
        islemDAO = new IslemDAO();
        birimDAO = new BirimDAO();
        hemsireDAO = new HemsireDAO(); // Yeni eklendi
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // AJAX isteğini kontrol et: Birim listesi isteniyor mu?
        String action = request.getParameter("action");
        if ("getBirimlerByIslem".equals(action)) {
            String islemIdStr = request.getParameter("islemId");
            if (islemIdStr != null && !islemIdStr.isEmpty()) {
                try {
                    int islemId = Integer.parseInt(islemIdStr);
                    // islemId'ye göre birimleri veritabanından çekin
                    List<Birim> birimler = birimDAO.getBirimlerByIslemId(islemId);

                    // Birimleri JSON formatına dönüştürüp yanıt olarak gönderin
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    String json = new Gson().toJson(birimler);
                    response.getWriter().write(json);
                } catch (NumberFormatException | SQLException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\": \"Sunucu hatası: " + e.getMessage() + "\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"islemId parametresi eksik\"}");
            }
            return; // AJAX isteği işlendi, daha fazla işlem yapmaya gerek yok
        }

        // Normal sayfa yükleme işlemi
        try {
            // Sadece tecrübe seviyelerini çekin, tüm hemşireleri değil
            List<String> tecrubeSeviyeleri = hemsireDAO.getUniqueTecrubeSeviyeleri();

            List<Islem> islemler = islemDAO.getAktifIslemler();
            // Birimler direkt getirilmiyor, AJAX ile çağrılacak. Bu satır silindi:
            // List<Birim> birimler = birimDAO.getAktifBirimler();

            request.setAttribute("tecrubeSeviyeleri", tecrubeSeviyeleri);
            request.setAttribute("islemler", islemler);

            request.getRequestDispatcher("islem-kayit.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Veritabanı hatası", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // JSP formundaki 'name' özelliklerine göre parametreleri alın
            String tecrubeSeviyesi = request.getParameter("hemsire"); // Formdan gelen 'hemsire'
            String islemIdStr = request.getParameter("islem"); // Formdan gelen 'islem'
            String birimIdStr = request.getParameter("birim"); // Formdan gelen 'birim'
            String gercekSureStr = request.getParameter("sure"); // Formdan gelen 'sure'
            String islemTipi = request.getParameter("islemTipi"); // Formdan gelen 'islemTipi'
            String kritiklik = request.getParameter("kritiklik"); // Formdan gelen 'kritiklik'
            String notlar = request.getParameter("notlar");

            // Validasyon
            if (tecrubeSeviyesi == null || tecrubeSeviyesi.isEmpty()) {
                request.setAttribute("hata", "Lütfen bir tecrübe seviyesi seçin.");
                doGet(request, response);
                return;
            }
            // Diğer validasyonlar da eklenebilir.

            int islemId = Integer.parseInt(islemIdStr);
            int birimId = Integer.parseInt(birimIdStr);
            int gercekSure = Integer.parseInt(gercekSureStr);

            // Eğer KayitIslem modelinizde "islemTipi" ve "kritiklik" için alanlar yoksa,
            // bu modeli güncellemeniz veya bu verileri "notlar" gibi bir alanda birleştirmeniz gerekebilir.
            // Bu örnekte, 'kritiklik' değerini 'hastaDurumu' olarak kullanacağız.
            String hastaDurumu = kritiklik;

            // Yeni kayıt oluştur
            KayitIslem kayit = new KayitIslem(tecrubeSeviyesi, islemId, birimId, gercekSure, hastaDurumu);
            kayit.setKayitZamani(LocalDateTime.now());
            kayit.setNotlar(notlar);

            // Veritabanına kaydet
            int kayitId = kayitIslemDAO.insertKayitIslem(kayit);

            if (kayitId > 0) {
                request.getSession().setAttribute("basari", "İşlem başarıyla kaydedildi!");
                response.sendRedirect("dashboard");
            } else {
                request.setAttribute("hata", "Kayıt sırasında hata oluştu");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("hata", "Geçersiz sayısal değer: " + e.getMessage());
            doGet(request, response);
        } catch (SQLException e) {
            throw new ServletException("Veritabanı hatası", e);
        }
    }
}