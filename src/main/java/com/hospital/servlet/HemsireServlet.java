package com.hospital.servlet;

import com.hospital.dao.HemsireDAO;
import com.hospital.model.Hemsire;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class HemsireServlet extends HttpServlet {
    private HemsireDAO hemsireDAO;

    @Override
    public void init() throws ServletException {
        // Initialize the DAO in the init() method
        this.hemsireDAO = new HemsireDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Find all active nurses
            List<Hemsire> hemsireler = hemsireDAO.findAll();
            request.setAttribute("hemsireler", hemsireler);

            // Forward to the list page
            request.getRequestDispatcher("hemsire-liste.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Hemsire listesi yüklenirken bir hata oluştu.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Session check is good, keep it
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kullanici") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Updated parameter names to match the Hemsire model and 'hemsire' table columns
            String personelNo = request.getParameter("personelNo");
            String adSoyad = request.getParameter("adSoyad");
            String tecrubeSeviyesi = request.getParameter("tecrubeSeviyesi");
            String iseGirisTarihiStr = request.getParameter("iseGirisTarihi");
            int kurumId = Integer.parseInt(request.getParameter("kurumId"));
            boolean aktif = Boolean.parseBoolean(request.getParameter("aktif"));

            // Create a new Hemsire object
            Hemsire hemsire = new Hemsire();
            hemsire.setPersonelNo(personelNo);
            hemsire.setAdSoyad(adSoyad);
            hemsire.setTecrubeSeviyesi(tecrubeSeviyesi);
            hemsire.setKurumId(kurumId);
            hemsire.setAktif(aktif);

            // Set the `iseGirisTarihi` attribute if it is provided
            if (iseGirisTarihiStr != null && !iseGirisTarihiStr.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date iseGirisTarihi = new Date(sdf.parse(iseGirisTarihiStr).getTime());
                hemsire.setIseGirisTarihi(iseGirisTarihi);
            }

            // Call the DAO's create method
            boolean basarili = hemsireDAO.create(hemsire);

            if (basarili) {
                response.sendRedirect("hemsire?basari=1");
            } else {
                request.setAttribute("hata", "Hemşire kaydedilirken hata oluştu.");
                request.getRequestDispatcher("hemsire-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("hata", "Geçersiz veri girişi veya veritabanı hatası: " + e.getMessage());
            request.getRequestDispatcher("hemsire-form.jsp").forward(request, response);
        }
    }
}