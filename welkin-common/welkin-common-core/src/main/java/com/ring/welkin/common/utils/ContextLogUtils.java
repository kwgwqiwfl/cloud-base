package com.ring.welkin.common.utils;

import com.ring.welkin.common.core.saas.SaasContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

@Slf4j
public class ContextLogUtils {

    public static void logCurrentContext(Level level, Class<?> clazz, Object obj) {
        Thread currentThread = Thread.currentThread();
        StringBuffer buf = new StringBuffer();
        buf.append("\n        ")//
            .append("current Class:").append(clazz.getName()).append("\n        ")//
            .append("current Thread:")
            .append(String.format("group=%s, id=%s, name=%s.", currentThread.getThreadGroup().getName(),
                currentThread.getId(), currentThread.getName()))
            .append("\n        ")//
            .append("current SaasContext:").append(JsonUtils.toJson(SaasContext.getCurrentSaasContext()));//
        if (obj != null) {
            buf.append("additional info:" + JsonUtils.toJson(obj));
        }
        log(level, buf.toString());
    }

    public static void logCurrentContext(Level level, Class<?> clazz) {
        logCurrentContext(level, clazz, null);
    }

    public static void traceCurrentContext(Class<?> clazz) {
        logCurrentContext(Level.TRACE, clazz);
    }

    public static void debugCurrentContext(Class<?> clazz) {
        logCurrentContext(Level.DEBUG, clazz);
    }

    public static void infoCurrentContext(Class<?> clazz) {
        logCurrentContext(Level.INFO, clazz);
    }

    public static void warnCurrentContext(Class<?> clazz) {
        logCurrentContext(Level.WARN, clazz);
    }

    public static void errorCurrentContext(Class<?> clazz) {
        logCurrentContext(Level.ERROR, clazz);
    }

    public static void traceCurrentContext(Class<?> clazz, Object obj) {
        logCurrentContext(Level.TRACE, clazz, obj);
    }

    public static void debugCurrentContext(Class<?> clazz, Object obj) {
        logCurrentContext(Level.DEBUG, clazz, obj);
    }

    public static void infoCurrentContext(Class<?> clazz, Object obj) {
        logCurrentContext(Level.INFO, clazz, obj);
    }

    public static void warnCurrentContext(Class<?> clazz, Object obj) {
        logCurrentContext(Level.WARN, clazz, obj);
    }

    public static void errorCurrentContext(Class<?> clazz, Object obj) {
        logCurrentContext(Level.ERROR, clazz, obj);
    }

    public static void log(Level level, String msg) {
        switch (level) {
            case TRACE:
                log.trace(msg);
                break;
            case INFO:
                log.info(msg);
                break;
            case WARN:
                log.warn(msg);
                break;
            case ERROR:
                log.error(msg);
                break;
            case DEBUG:
            default:
                log.debug(msg);
                break;
        }
    }
}
