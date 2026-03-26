package com.ring.welkin.common.utils;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author EDY
 */
public class WelkinUrlClassloader extends URLClassLoader {

    private final ClassLoader myPrent = ClassLoader.getSystemClassLoader();

    public WelkinUrlClassloader(URL[] urls) {
        super(urls, null);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            return myPrent.loadClass(name);
        }
    }

}
