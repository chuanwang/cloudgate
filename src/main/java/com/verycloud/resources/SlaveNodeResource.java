/**
 *
 * SlaveNodeStatus.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import com.opendatagroup.sector.sectorjni.SlaveStat;
import com.opendatagroup.sector.sectorjni.SysStat;
import com.verycloud.datacollector.SysInfoUtil;

/**
 * Slave node server resource.
 */
public class SlaveNodeResource extends SectorServerResource {

    /** Slave node id */
    private int id;
    
    /**
     * @return slave node status based on id in request.
     */
    @Get("json")
    public Representation retrieveSlaveStatus() {
        
        SysStat sectorStat = getSectorClient().getClient().sectorSysInfo();
        
        SlaveStat [] slaves = sectorStat.getSlaveList();
        
        setId(Integer.valueOf((String)getRequestAttributes().get("slaveid"))); //$NON-NLS-1$
        
        JSONArray array = new JSONArray();
        try {
            
            // find slave node from slavestat list.
            SlaveStat slave = null;
            for (SlaveStat slaveStat : slaves) {
                if(slaveStat.getId() == getId()) {
                    slave = slaveStat;
                    break;
                }
            }
                
            JSONObject obj = new JSONObject();
            obj.put("id", slave.getId()); //$NON-NLS-1$
            obj.put("clusterId", slave.getClusterId()); //$NON-NLS-1$
            
            int status = slave.getStatus();
            obj.put("status", SysInfoUtil.getSlaveNodeStatus(status)); //$NON-NLS-1$
            
            obj.put("address", slave.getIp()); //$NON-NLS-1$
            obj.put("port", slave.getPort()); //$NON-NLS-1$
            
            long diskSpace = slave.getTotalDiskSpace();
            obj.put("availableDisk", SysInfoUtil.getFormattedSize(diskSpace)); //$NON-NLS-1$
            
            long fileSize = slave.getTotalFileSize();
            obj.put("fileSize", SysInfoUtil.getFormattedSize(fileSize)); //$NON-NLS-1$
            
            long inputSize = slave.getTotalInputData();
            obj.put("netIn", SysInfoUtil.getFormattedSize(inputSize)); //$NON-NLS-1$
            
            long outputSize = slave.getTotalOutputData();
            obj.put("netOut", SysInfoUtil.getFormattedSize(outputSize)); //$NON-NLS-1$
            
            long mem = slave.getCurrMemUsed();
            obj.put("memory", SysInfoUtil.getFormattedSize(mem)); //$NON-NLS-1$
            
            obj.put("cpu", slave.getCurrCpuUsed()); //$NON-NLS-1$
            
            long lastUpdate = slave.getLastUpdateTimestamp();
            obj.put("lastUpdate", SysInfoUtil.getFormattedTime(lastUpdate)); //$NON-NLS-1$
            
            obj.put("dataDir", slave.getDataDir()); //$NON-NLS-1$

            array.put(0, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Representation rep = new JsonRepresentation(array);

        return rep;

    }

    /**
     * Set the id.
     * @param id1 int
     */
    public void setId(int id1) {
        this.id = id1;
    }

    /**
     * Get the id.
     * @return int
     */
    public int getId() {
        return id;
    }

}
