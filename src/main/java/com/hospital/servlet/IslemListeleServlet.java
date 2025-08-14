package com.hospital.servlet;

import com.hospital.dao.IslemDAO;
import com.hospital.model.Islem;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class IslemListeleServlet extends HttpServlet {
    private IslemDAO islemDAO = new IslemDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kullanici") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String baslangicStr = request.getParameter("baslangicTarihi");
        String bitisStr = request.getParameter("bitisTarihi");

        if (baslangicStr != null && bitisStr != null &&
                !baslangicStr.trim().isEmpty() && !bitisStr.trim().isEmpty()) {

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date baslangic = new Date(sdf.parse(baslangicStr).getTime());
                Date bitis = new Date(sdf.parse(bitisStr).getTime());

                List<Islem> islemler = islemDAO.findByDateRange(baslangic, bitis);
                request.setAttribute("islemler", islemler);
                request.setAttribute("baslangicTarihi", baslangicStr);
                request.setAttribute("bitisTarihi", bitisStr);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("hata", "Geçersiz tarih formatı.");
            }
        }

        request.getRequestDispatcher("islem-listele.jsp").forward(request, response);
    }
}
