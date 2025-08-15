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
    private final IslemDAO islemDAO = new IslemDAO();
    private final HemsireDAO hemsireDAO = new HemsireDAO();
    private final IslemTuruDAO islemTuruDAO = new IslemTuruDAO();
    private final BirimDAO birimDAO = new BirimDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("getBirimlerByIslem".equals(action)) {
            getBirimlerByIslem(request, response);
            return;
        }

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
            String tarihStr = request.getParameter("tarih");
            int hemsireId = Integer.parseInt(request.getParameter("hemsire_id"));
            int islemId = Integer.parseInt(request.getParameter("islem_id"));
            int birimId = Integer.parseInt(request.getParameter("birim_id"));
            int sureDakika = Integer.parseInt(request.getParameter("sure_dakika"));
            String islemTipi = request.getParameter("islem_tipi");
            String kritiklik = request.getParameter("kritiklik");
            String notlar = request.getParameter("notlar");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date tarih = new Date(sdf.parse(tarihStr).getTime());

            Islem islem = new Islem(tarih, hemsireId, islemId, birimId, sureDakika, islemTipi, kritiklik);
            islem.setNotlar(notlar);

            Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
            islem.setKaydedenKullaniciId(kullanici.getId());

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
