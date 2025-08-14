package com.hospital.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SessionTimeoutFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter başlatma işlemleri
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        if (session != null) {
            // Session'ın maksimum inactive interval'ını kontrol et
            long lastAccessedTime = session.getLastAccessedTime();
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - lastAccessedTime;

            // 30 dakika (1800000 ms) geçtiyse session'ı invalid et
            if (timeDifference > 1800000) {
                session.invalidate();
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?timeout=true");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Filter temizleme işlemleri
    }
}
