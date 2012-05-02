/**
 *
 * CustomHibernateDaoSupport.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * TODO Add class description here.
 */
public class CustomHibernateDaoSupport extends HibernateDaoSupport {
    /**
     * @param sessionFactory
     */
    @Autowired
    public void anyMethodName(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }
}
