package com.hospital.servlet;

import com.hospital.dao.HemsireDAO;
import com.hospital.dao.HemsireTecrubeDAO;
import com.hospital.model.Hemsire;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class HemsireServlet extends HttpServlet {
    private HemsireDAO hemsireDAO = new HemsireDAO();
    private HemsireTecrubeDAO tecrubeDAO = new HemsireTecrubeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("yeni".equals(action)) {
            // Yeni hemşire formu
            request.setAttribute("tecrubeKategorileri", tecrubeDAO.findAll());
            request.getRequestDispatcher("hemsire-form.jsp").forward(request, response);
            return;
        }

        // Hemşire listesi
        List<Hemsire> hemsireler = hemsireDAO.findAll();
        request.setAttribute("hemsireler", hemsireler);
        request.getRequestDispatcher("hemsire-liste.jsp").forward(request, response);
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
            String sicilNo = request.getParameter("sicilNo");
            String adSoyad = request.getParameter("adSoyad");
            int tecrubeId = Integer.parseInt(request.getParameter("tecrubeId"));
            String bolum = request.getParameter("bolum");
            String telefon = request.getParameter("telefon");
            String email = request.getParameter("email");
            String iseGirisTarihiStr = request.getParameter("iseGirisTarihi");

            Hemsire hemsire = new Hemsire(sicilNo, adSoyad, tecrubeId);
            hemsire.setBolum(bolum);
            hemsire.setTelefon(telefon);
            hemsire.setEmail(email);

            if (iseGirisTarihiStr != null && !iseGirisTarihiStr.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date iseGirisTarihi = new Date(sdf.parse(iseGirisTarihiStr).getTime());
                hemsire.setIseGirisTarihi(iseGirisTarihi);
            }

            boolean basarili = hemsireDAO.create(hemsire);

            if (basarili) {
                response.sendRedirect("hemsire?basari=1");
            } else {
                request.setAttribute("hata", "Hemşire kaydedilirken hata oluştu.");
                request.setAttribute("tecrubeKategorileri", tecrubeDAO.findAll());
                request.getRequestDispatcher("hemsire-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("hata", "Geçersiz veri girişi: " + e.getMessage());
            request.setAttribute("tecrubeKategorileri", tecrubeDAO.findAll());
            request.getRequestDispatcher("hemsire-form.jsp").forward(request, response);
        }
    }
}
