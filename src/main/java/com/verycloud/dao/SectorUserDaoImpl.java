/**
 *
 * SectorUserDaoImpl.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.spi.LoggerFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.verycloud.model.SectorUser;

/**
 * TODO Add class description here.
 */
@Repository("sectorUserDao")
public class SectorUserDaoImpl implements SectorUserDao {

    private static final Log log = LogFactory.getLog(SectorUserDaoImpl.class);
    
    private HibernateTemplate hibernateTemplate;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }
    
    /**
     * @param newUser
     * @see com.verycloud.dao.SectorUserDao#save(com.verycloud.model.SectorUser)
     */
    public int save(SectorUser newUser) {
        return (Integer) getHibernateTemplate().save(newUser);
    }

    /**
     * @param newUser
     * @see com.verycloud.dao.SectorUserDao#update(com.verycloud.model.SectorUser)
     */
    public void update(SectorUser newUser) {
        getHibernateTemplate().update(newUser);
    }

    /**
     * @param newUser
     * @see com.verycloud.dao.SectorUserDao#delete(com.verycloud.model.SectorUser)
     */
    public void delete(SectorUser newUser) {
        getHibernateTemplate().delete(newUser);
    }

    /**
     * @param newName
     * @see com.verycloud.dao.SectorUserDao#findByName(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public SectorUser findByName(String newName) {
        HibernateTemplate template = getHibernateTemplate();
        List<SectorUser> list = template.find("from SectorUser where name=?", newName); //$NON-NLS-1$
        
        return list.get(0);
    }

    /**
     * @return
     * @see com.verycloud.dao.SectorUserDao#findAllUsers()
     */
    @SuppressWarnings("unchecked")
    public List<SectorUser> findAllUsers() {
        HibernateTemplate template = getHibernateTemplate();
        return template.find("from SectorUser"); //$NON-NLS-1$
    }
    
    /**
     * Get the hibernateTemplate.
     * @return HibernateTemplate
     */
    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    /**
     * Set the hibernateTemplate.
     * @param newHibernateTemplate HibernateTemplate
     */
    public void setHibernateTemplate(HibernateTemplate newHibernateTemplate) {
        hibernateTemplate = newHibernateTemplate;
    }


    @SuppressWarnings("unchecked")  
    public Object execute(final String hql, final Map<String, Object> args) {  
        return  hibernateTemplate.execute(new HibernateCallback() {  
            public Object doInHibernate(Session session)throws HibernateException {  
                if(log.isDebugEnabled()) log.debug("execute :" + hql);                
                Query q = session.createQuery(hql);  
                if(args!=null)  
                {  
                    for (String k : args.keySet()){  
                        q.setParameter(k, args.get(k));  
                    }  
                }  
                return q.executeUpdate();  
            }

        });  
    }  

    @SuppressWarnings("unchecked")  
    public void deleteByIds(String[] ids) {       
          if(ids.length<1) return;  
          StringBuilder sb=new StringBuilder();  
          sb.append("id in (");  
          for(int j=0;j< ids.length;j++)  
          {  
              sb.append("'"+ids[j]+"'");  
              if(j<(ids.length-1))  sb.append(",");  
          }  
          sb.append(")");  
          execute("delete SectorUser where " + sb.toString(),null);  
    }  
}
