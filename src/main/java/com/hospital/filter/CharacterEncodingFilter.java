package com.hospital.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {
    private String encoding;
    private boolean forceEncoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        String forceEncodingParam = filterConfig.getInitParameter("forceEncoding");
        forceEncoding = Boolean.parseBoolean(forceEncodingParam);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (encoding != null && (forceEncoding || httpRequest.getCharacterEncoding() == null)) {
            httpRequest.setCharacterEncoding(encoding);
        }

        if (encoding != null && forceEncoding) {
            httpResponse.setCharacterEncoding(encoding);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Filter temizleme işlemleri
    }
}
