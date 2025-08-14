package com.hospital.servlet;

import com.hospital.dao.BirimDAO;
import com.hospital.dao.HemsireDAO;
import com.hospital.dao.IslemDAO;
import com.hospital.dao.IslemTuruDAO;
import com.hospital.model.Birim;
import com.hospital.model.Islem;
import com.hospital.model.Kullanici;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import com.google.gson.Gson;

public class IslemKayitServlet extends HttpServlet {
    private IslemDAO islemDAO = new IslemDAO();
    private HemsireDAO hemsireDAO = new HemsireDAO();
    private IslemTuruDAO islemTuruDAO = new IslemTuruDAO();
    private BirimDAO birimDAO = new BirimDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("getBirimlerByIslem".equals(action)) {
            getBirimlerByIslem(request, response);
            return;
        }

        // Form için gerekli verileri yükle
        request.setAttribute("hemsireler", hemsireDAO.findAll());
        request.setAttribute("islemTurleri", islemTuruDAO.findAll());
        request.setAttribute("birimler", birimDAO.findAll());

        request.getRequestDispatcher("islem-kayit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kullanici") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Form verilerini al
            String tarihStr = request.getParameter("tarih");
            int hemsireId = Integer.parseInt(request.getParameter("hemsire"));
            int islemId = Integer.parseInt(request.getParameter("islem"));
            int birimId = Integer.parseInt(request.getParameter("birim"));
            int sureDakika = Integer.parseInt(request.getParameter("sure"));
            String islemTipi = request.getParameter("islemTipi");
            String kritiklik = request.getParameter("kritiklik");
            String notlar = request.getParameter("notlar");

            // Tarih dönüşümü
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date tarih = new Date(sdf.parse(tarihStr).getTime());

            // İşlem objesi oluştur
            Islem islem = new Islem(tarih, hemsireId, islemId, birimId, sureDakika, islemTipi, kritiklik);
            islem.setNotlar(notlar);

            Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
            islem.setKaydedenKullaniciId(kullanici.getId());

            // Kaydet
            boolean basarili = islemDAO.create(islem);

            if (basarili) {
                request.setAttribute("basari", "İşlem başarıyla kaydedildi.");
            } else {
                request.setAttribute("hata", "İşlem kaydedilirken hata oluştu.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("hata", "Geçersiz veri girişi: " + e.getMessage());
        }

        // Form verilerini tekrar yükle
        request.setAttribute("hemsireler", hemsireDAO.findAll());
        request.setAttribute("islemTurleri", islemTuruDAO.findAll());
        request.setAttribute("birimler", birimDAO.findAll());

        request.getRequestDispatcher("islem-kayit.jsp").forward(request, response);
    }

    private void getBirimlerByIslem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String islemIdStr = request.getParameter("islemId");
        if (islemIdStr != null) {
            try {
                int islemId = Integer.parseInt(islemIdStr);
                List<Birim> birimler = birimDAO.findByIslemId(islemId);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Gson gson = new Gson();
                response.getWriter().write(gson.toJson(birimler));

            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
