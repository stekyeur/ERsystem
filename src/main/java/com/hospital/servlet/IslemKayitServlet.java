package com.hospital.servlet;

import com.google.gson.*;
import com.hospital.dao.BirimDAO;
import com.hospital.dao.HemsireDAO;
import com.hospital.dao.IslemDAO;
import com.hospital.dao.KayitIslemDAO;
import com.hospital.model.Birim;
import com.hospital.model.Islem;
import com.hospital.model.KayitIslem;
import com.hospital.servlet.LocalDateTimeAdapter;
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
    private HemsireDAO hemsireDAO;

    // Gson nesnesini sınıf değişkeni olarak tanımlayın
    private Gson gson;

    @Override
    public void init() throws ServletException {
        kayitIslemDAO = new KayitIslemDAO();
        islemDAO = new IslemDAO();
        birimDAO = new BirimDAO();
        hemsireDAO = new HemsireDAO();

        // Gson nesnesini burada, sadece bir kere oluşturun ve adaptörü kaydedin
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("getBirimlerByIslem".equals(action)) {
            String islemIdStr = request.getParameter("islemId");
            if (islemIdStr != null && !islemIdStr.isEmpty()) {
                try {
                    int islemId = Integer.parseInt(islemIdStr);
                    List<Birim> birimler = birimDAO.getBirimlerByIslemId(islemId);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    // Burada, daha önce init() metodunda oluşturulan ve konfigüre edilen 'gson' nesnesini kullanın
                    String json = this.gson.toJson(birimler);
                    response.getWriter().write(json);
                } catch (NumberFormatException | SQLException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\": \"Sunucu hatası: " + e.getMessage() + "\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"islemId parametresi eksik\"}");
            }
            return;
        }

        try {
            List<String> tecrubeSeviyeleri = hemsireDAO.getUniqueTecrubeSeviyeleri();
            List<Islem> islemler = islemDAO.getAktifIslemler();

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
            String tecrubeSeviyesi = request.getParameter("hemsire");
            String islemIdStr = request.getParameter("islem");
            String birimIdStr = request.getParameter("birim");
            String gercekSureStr = request.getParameter("sure");
            String islemTipi = request.getParameter("islemTipi");
            String kritiklik = request.getParameter("kritiklik");
            String notlar = request.getParameter("notlar");

            if (tecrubeSeviyesi == null || tecrubeSeviyesi.isEmpty()) {
                request.setAttribute("hata", "Lütfen bir tecrübe seviyesi seçin.");
                doGet(request, response);
                return;
            }

            int islemId = Integer.parseInt(islemIdStr);
            int birimId = Integer.parseInt(birimIdStr);
            int gercekSure = Integer.parseInt(gercekSureStr);

            KayitIslem kayit = new KayitIslem(tecrubeSeviyesi, islemId, birimId, gercekSure, kritiklik);
            kayit.setKayitZamani(LocalDateTime.now());
            kayit.setNotlar(notlar);

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
