package com.hospital.servlet;

import com.hospital.dao.IslemDAO;
import com.hospital.dao.RaporDAO;
import com.hospital.model.Kullanici;
import com.hospital.model.Rapor;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class RaporServlet extends HttpServlet {
    private RaporDAO raporDAO = new RaporDAO();
    private IslemDAO islemDAO = new IslemDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kullanici") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("liste".equals(action)) {
            Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
            List<Rapor> raporlar = raporDAO.findByKullanici(kullanici.getId());
            request.setAttribute("raporlar", raporlar);
            request.getRequestDispatcher("rapor-liste.jsp").forward(request, response);
            return;
        }

        if ("indir".equals(action)) {
            downloadRapor(request, response);
            return;
        }

        // Rapor oluşturma formu
        request.getRequestDispatcher("rapor-form.jsp").forward(request, response);
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
            String raporAdi = request.getParameter("raporAdi");
            String raporTipi = request.getParameter("raporTipi");
            String format = request.getParameter("format");
            String baslangicStr = request.getParameter("baslangicTarihi");
            String bitisStr = request.getParameter("bitisTarihi");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date baslangic = new Date(sdf.parse(baslangicStr).getTime());
            Date bitis = new Date(sdf.parse(bitisStr).getTime());

            Rapor rapor = new Rapor(raporAdi, raporTipi, format, baslangic, bitis);
            Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
            rapor.setOlusturanKullaniciId(kullanici.getId());

            boolean basarili = raporDAO.create(rapor);

            if (basarili) {
                // Rapor oluşturma işlemini başlat (arka planda)
                createRaporAsync(rapor);
                response.sendRedirect("rapor?action=liste&basari=1");
            } else {
                request.setAttribute("hata", "Rapor oluşturulurken hata oluştu.");
                request.getRequestDispatcher("rapor-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("hata", "Geçersiz veri girişi: " + e.getMessage());
            request.getRequestDispatcher("rapor-form.jsp").forward(request, response);
        }
    }

    private void createRaporAsync(Rapor rapor) {
        // Bu metod arka planda rapor oluşturma işlemini simüle eder
        // Gerçek uygulamada burada Excel/PDF oluşturma kodu olacak
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simülasyon için bekleme

                // Rapor verilerini al
                List<Object[]> veriler = islemDAO.getGunlukIstatistikler((Date) rapor.getBaslangicTarihi());

                // Dosya yolu oluştur
                String dosyaAdi = "rapor_" + rapor.getId() + "_" + System.currentTimeMillis();
                if ("PDF".equals(rapor.getFormat())) {
                    dosyaAdi += ".pdf";
                } else {
                    dosyaAdi += ".xlsx";
                }

                String dosyaYolu = "/raporlar/" + dosyaAdi;

                // Burada gerçek dosya oluşturma işlemi yapılacak
                // createPdfRapor(veriler, dosyaYolu) veya createExcelRapor(veriler, dosyaYolu)

                // Rapor durumunu güncelle
                raporDAO.updateDurum(rapor.getId(), "HAZIR", dosyaYolu);

            } catch (Exception e) {
                e.printStackTrace();
                raporDAO.updateDurum(rapor.getId(), "HATA", null);
            }
        }).start();
    }

    private void downloadRapor(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Rapor indirme işlemi
        String raporId = request.getParameter("id");
        // Bu metod rapor dosyasını kullanıcıya indirtir
        // Gerçek implementasyonda dosya okuma ve response'a yazma işlemleri yapılacak
        response.getWriter().write("Rapor indirme işlemi - ID: " + raporId);
    }
}
