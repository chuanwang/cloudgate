/**
 *
 * SessionManager.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Settings;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO Add class description here.
 */
public class SessionsManager {

    @Autowired
    private static SessionFactory factory;
    
    public static Settings getSettings() {
        return ((SessionFactoryImpl)factory).getSettings();
    }
}
