package com.hospital.util;

import com.hospital.model.Kullanici;
import jakarta.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {
    private static final ConcurrentHashMap<String, HttpSession> activeSessions = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        // Her 5 dakikada bir expired session'ları temizle
        scheduler.scheduleAtFixedRate(SessionManager::cleanupExpiredSessions, 5, 5, TimeUnit.MINUTES);
    }

    public static void addSession(HttpSession session) {
        activeSessions.put(session.getId(), session);
    }

    public static void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public static int getActiveSessionCount() {
        return activeSessions.size();
    }

    public static void invalidateUserSessions(String username) {
        activeSessions.values().forEach(session -> {
            try {
                Kullanici kullanici = (Kullanici) session.getAttribute("kullanici");
                if (kullanici != null && username.equals(kullanici.getKullaniciAdi())) {
                    session.invalidate();
                }
            } catch (IllegalStateException e) {
                // Session zaten invalid
            }
        });
    }

    private static void cleanupExpiredSessions() {
        activeSessions.entrySet().removeIf(entry -> {
            try {
                HttpSession session = entry.getValue();
                long lastAccessedTime = session.getLastAccessedTime();
                long currentTime = System.currentTimeMillis();

                // 30 dakika inaktif olan session'ları temizle
                return (currentTime - lastAccessedTime) > 1800000;
            } catch (IllegalStateException e) {
                // Session invalid, listeden çıkar
                return true;
            }
        });
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}
