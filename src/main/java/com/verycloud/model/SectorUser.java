/**
 *
 * SectorUser.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A model annotation class to store SectorUser data.
 */

@Entity
@Table(name = "sector_users")
public class SectorUser implements Serializable {

    private Integer userId;
    
    private String name;
    
    private String password;
    
    private Integer quota;
    
    private List<String> acl;
    
    private List<String> readPermission;
    
    private List<String> writePermission;
    
    private Boolean execPermission;

    /**
     * Default constructor.
     */
    public SectorUser() {
        
    }
    
    /**
     * Get the userId.
     * @return Integer
     */
    @Id 
    @SequenceGenerator(name="pk_sequence",sequenceName="sector_users_did_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
    @Column(name="did", unique = true, nullable = false)
    public Integer getUserId() {
        return userId;
    }

    /**
     * Set the userId.
     * @param newUserId Integer
     */
    public void setUserId(Integer newUserId) {
        userId = newUserId;
    }

    /**
     * Get the name.
     * @return String
     */
    @Column(name="name", unique=true, nullable=false, length=40)
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     * @param newName String
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Get the password.
     * @return String
     */
    @Column(name="passwd", length=20)
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     * @param newPassword String
     */
    public void setPassword(String newPassword) {
        password = newPassword;
    }

    /**
     * Get the quota.
     * @return Integer
     */
    @Column(name="quota")
    public Integer getQuota() {
        return quota;
    }

    /**
     * Set the quota.
     * @param newQuota Integer
     */
    public void setQuota(Integer newQuota) {
        quota = newQuota;
    }

    /**
     * Get the acl.
     * @return String[]
     */
    @Column(name="acl", nullable=true, columnDefinition="varchar[]")
    @Type(type="com.verycloud.model.ListAsSQLArrayUserType$STRING") 
    public List<String> getAcl() {
        return acl;
    }

    /**
     * Set the acl.
     * @param newAcl String[]
     */
    public void setAcl(List<String> newAcl) {
        acl = newAcl;
    }

    /**
     * Get the readPermission.
     * @return String[]
     */
    @Column(name="read_permission", nullable=true, columnDefinition="varchar[]")
    @Type(type="com.verycloud.model.ListAsSQLArrayUserType$STRING") 
    public List<String> getReadPermission() {
        return readPermission;
    }

    /**
     * Set the readPermission.
     * @param newReadPermission String[]
     */
    public void setReadPermission(List<String> newReadPermission) {
        readPermission = newReadPermission;
    }

    /**
     * Get the writePermission.
     * @return String[]
     */
    @Column(name="write_permission", nullable=true, columnDefinition="varchar[]")
    @Type(type="com.verycloud.model.ListAsSQLArrayUserType$STRING") 
    public List<String> getWritePermission() {
        return writePermission;
    }

    /**
     * Set the writePermission.
     * @param newWritePermission String[]
     */
    public void setWritePermission(List<String> newWritePermission) {
        writePermission = newWritePermission;
    }

    /**
     * Get the execPermission.
     * @return Boolean
     */
    @Column(name="exec_permission")
    public Boolean getExecPermission() {
        return execPermission;
    }

    /**
     * Set the execPermission.
     * @param newExecPermission Boolean
     */
    public void setExecPermission(Boolean newExecPermission) {
        execPermission = newExecPermission;
    }
    
    /**
     * return json representation of Sectouser.
     * @return JSON object.
     */
    public JSONObject userToJson() {
        JSONObject obj = new JSONObject();
        
        try {
            obj.put("user_id", getUserId());
            obj.put("name", getName());
            obj.put("passwd", getPassword());
            obj.put("quota", getQuota());
            obj.put("exec_permission", getExecPermission());
            
            List<String> acl = getAcl();
            if(acl != null && !acl.isEmpty()) {
                String str = listToCSV(acl);
                obj.put("acl", str);
            }
            
            List<String> readPerm = getReadPermission();
            if(readPerm != null && !readPerm.isEmpty()) {
                String str = listToCSV(readPerm);
                obj.put("read_permission", str);
            }
            
            List<String> writePerm = getWritePermission();
            if(writePerm != null && !writePerm.isEmpty()) {
                String str = listToCSV(writePerm);
                obj.put("write_permission", str);
            }
            
        }catch (JSONException e) {
            e.printStackTrace();
        }
        
        return obj;
    }
    
    private String listToCSV(List<String> src) {
        StringBuilder sb = new StringBuilder();
        for(String s : src) {
            sb.append(s);
            sb.append(",");
        }
        
        return sb.substring(0, sb.length()-1).toString();
    }
}
