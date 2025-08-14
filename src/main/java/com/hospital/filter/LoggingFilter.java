package com.hospital.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Log bilgilerini topla
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        String remoteAddr = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        // Session bilgisi
        HttpSession session = httpRequest.getSession(false);
        String kullaniciAdi = "Anonim";
        if (session != null && session.getAttribute("kullaniciAdi") != null) {
            kullaniciAdi = (String) session.getAttribute("kullaniciAdi");
        }

        // Timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());

        // Log mesajı oluştur
        String logMessage = String.format("[%s] %s %s - IP: %s - Kullanıcı: %s - User-Agent: %s",
                timestamp, method, uri, remoteAddr, kullaniciAdi, userAgent);

        System.out.println(logMessage);

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long endTime = System.currentTimeMillis();
            long processingTime = endTime - startTime;
            System.out.println(String.format("[%s] %s %s - İşlem süresi: %d ms",
                    timestamp, method, uri, processingTime));
        }
    }

    @Override
    public void destroy() {
        System.out.println("LoggingFilter destroyed");
    }
}
