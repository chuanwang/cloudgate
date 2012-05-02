/**
 *
 * SectorUsersResource.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.security.User;

import com.verycloud.dao.SectorUserDao;
import com.verycloud.model.SectorUser;

/**
 * TODO Add class description here.
 */
public class SectorUsersResource extends ServerResource {

    private SectorUserDao sectorUserDao;
    
    /**
     * @return Sector users.
     */
    @Get("json")
    public Representation retrieveSectorUsers() {
        
        List<SectorUser> result = sectorUserDao.findAllUsers();
        
        JSONArray array = new JSONArray();
        
        int i = 0;
        for(SectorUser user : result) {
            try {
                JSONObject obj = user.userToJson();
                array.put(i++, obj);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        
        JSONObject resultObj = new JSONObject();
        try {
            resultObj.put("rows", array);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Representation rep = new JsonRepresentation(resultObj);

        return rep;
    }

    @Post
    public Representation saveSectorUser(Representation entity) {
        Representation result = null;
        SectorUser user = new SectorUser();
        
        Form form = new Form(entity);
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
        
        JSONObject resultObj = new JSONObject();
        try {
            int generatedId = sectorUserDao.save(user);
            user.setUserId(generatedId);
        
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
    
    @Delete
    public Representation deleteSectorUsers(Representation entity) {
        JSONObject resultObj = new JSONObject();
        
        List<String> idList = new ArrayList<String>();
        
        Form form = new Form(entity);
        
        for(Parameter param : form) {
            String id = param.getValue();
            idList.add(id);
        }
        
        try {
            sectorUserDao.deleteByIds(idList.toArray(new String[] {}));

            try {
                resultObj.put("success", true);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        } catch (Exception e) {
            try {
                resultObj.put("success", false);
                return new JsonRepresentation(resultObj);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
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
