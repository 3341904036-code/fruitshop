package com.fruitshop.listener;

import lombok.extern.slf4j. Slf4j;
import org. springframework.stereotype.Component;
import javax.servlet.annotation.WebListener;
import javax. servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java. util.concurrent.atomic.AtomicInteger;

/**
 * Session监听器
 */
@Slf4j
@Component
@WebListener
public class SessionListener implements HttpSessionListener {

    private static AtomicInteger activeSessions = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = activeSessions.incrementAndGet();
        log.info("Session创建:  sessionId={}, 当前活跃Session数:  {}", se.getSession().getId(), count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = activeSessions.decrementAndGet();
        Object userId = se.getSession().getAttribute("userId");
        log.info("Session销毁: sessionId={}, userId={}, 当前活跃Session数: {}",
                se.getSession().getId(), userId, count);
    }

    /**
     * 获取当前活跃的Session数
     */
    public static int getActiveSessions() {
        return activeSessions.get();
    }
}