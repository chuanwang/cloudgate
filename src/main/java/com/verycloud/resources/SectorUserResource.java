/**
 *
 * SectorUserResource.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.resources;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.verycloud.dao.SectorUserDao;
import com.verycloud.model.SectorUser;

/**
 * TODO Add class description here.
 */
public class SectorUserResource extends ServerResource {

    private static final Log log = LogFactory.getLog(SectorUserResource.class);
    
    private SectorUserDao sectorUserDao;
    
    /**
     * @return Sector status.
     */
    @Get("json")
    public Representation retrieveSectorUserByName() {
        
        String userName = (String)(getRequestAttributes().get("username")); //$NON-NLS-1$
        
        JSONObject result = new JSONObject(); 
        try {
            SectorUser user = sectorUserDao.findByName(userName);
            result.put("success", true);
            result.put("data", user.userToJson());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if(log.isDebugEnabled()) {
                log.debug("Cannot find user by name: " + userName);
            }
            try {
                result.put("success", false);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        
        Representation rep = new JsonRepresentation(result);

        return rep;
    }

    @Put
    public Representation updateSectorUser(Representation entity) {
        JSONObject resultObj = new JSONObject();
        
        Form form = new Form(entity);
        
        SectorUser user = new SectorUser();
        
        user.setUserId(Integer.parseInt(form.getFirstValue("user_id")));
        user.setName(form.getFirstValue("name"));
        user.setPassword(form.getFirstValue("passwd"));
        user.setQuota(Integer.parseInt(form.getFirstValue("quota")));
        
        String acl = form.getFirstValue("acl");
        if(acl != null) {
            String [] aclArray = acl.split(",");
            user.setAcl(Arrays.asList(aclArray));
        }
        
        String readPerm = form.getFirstValue("read_permission");
        if(readPerm != null) {
            String [] readArray = readPerm.split(",");
            user.setReadPermission(Arrays.asList(readArray));
        }
        
        String writePerm = form.getFirstValue("write_permission");
        if(writePerm != null) {
            String [] writeArray = writePerm.split(",");
            user.setWritePermission(Arrays.asList(writeArray));
        }
        
        String execPerm = form.getFirstValue("exec_permission");
        if(execPerm != null) {
            if(execPerm.equalsIgnoreCase("on")) {
                user.setExecPermission(true);
            }
        }
        
        try {
            sectorUserDao.update(user);
        
            resultObj.put("success", true);
            resultObj.put("data", user.userToJson());
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            try {
                resultObj.put("success", false);
                resultObj.put("msg", "Failed to save sector user into DB");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        
        return new JsonRepresentation(resultObj);
        
    }
    
    /**
     * Get the sectorUserDao.
     * @return SectorUserDao
     */
    public SectorUserDao getSectorUserDao() {
        return sectorUserDao;
    }

    /**
     * Set the sectorUserDao.
     * @param newSectorUserDao SectorUserDao
     */
    public void setSectorUserDao(SectorUserDao newSectorUserDao) {
        sectorUserDao = newSectorUserDao;
    } 
}
