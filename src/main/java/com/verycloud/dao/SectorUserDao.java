/**
 *
 * SectorUserDao.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.dao;

import java.util.List;

import com.verycloud.model.SectorUser;

/**
 * Sector DAO interface.
 */
public interface SectorUserDao {

    /**
     * @param user
     */
    int save(SectorUser user);
    
    /**
     * @param user
     */
    void update(SectorUser user);
    
    /**
     * @param user
     */
    void delete(SectorUser user);
    
    /**
     * @param name
     */
    SectorUser findByName(String name);
    
    /**
     * Find all users.
     * @return
     */
    List<SectorUser> findAllUsers();
   
    /**
     * Delete users by id.
     * @param ids
     */
    void deleteByIds(String[] ids);
}
