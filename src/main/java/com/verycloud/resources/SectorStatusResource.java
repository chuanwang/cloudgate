/**
 *
 * SectorStatusResource.java
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

import com.opendatagroup.sector.sectorjni.ClusterStat;
import com.opendatagroup.sector.sectorjni.MasterStat;
import com.opendatagroup.sector.sectorjni.SysStat;
import com.verycloud.datacollector.SysInfoUtil;

/**
 * TODO Add class description here.
 */
public class SectorStatusResource extends SectorServerResource {

    /**
     * @return Sector status.
     */
    @Get("json")
    public Representation retrieveSectorStatus() {
        
        SysStat sectorStat = getSectorClient().getClient().sectorSysInfo();

        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            
            JSONArray masterArray = new JSONArray();
            MasterStat[] masters = sectorStat.getMasterList();
            
            int i = 0;
            for(MasterStat master : masters) {
                JSONObject masterObj = new JSONObject();
                masterObj.put("id", master.getId()); //$NON-NLS-1$
                masterObj.put("address", master.getIp()); //$NON-NLS-1$
                masterObj.put("port", master.getPort()); //$NON-NLS-1$
                masterArray.put(i++, masterObj);
            }
            obj.put("masters", masterArray); //$NON-NLS-1$
            
            long startTime = sectorStat.getStartTime();
            obj.put("runningSince", SysInfoUtil.getFormattedTime(startTime)); //$NON-NLS-1$

            long totalDiskSpace = sectorStat.getTotalDiskSpace();
            obj.put("availableDisk", SysInfoUtil.getFormattedSize(totalDiskSpace)); //$NON-NLS-1$
            
            long fileSize = sectorStat.getTotalFileSize();
            obj.put("fileSize", SysInfoUtil.getFormattedSize(fileSize)); //$NON-NLS-1$
            
            obj.put("fileNumber", sectorStat.getTotalFileNum()); //$NON-NLS-1$
            obj.put("slaveNodeNumber", sectorStat.getTotalSlavesNum()); //$NON-NLS-1$
            obj.put("clusterNumber", calculateClusterNum(sectorStat)); //$NON-NLS-1$
            
            array.put(0, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        Representation rep = new JsonRepresentation(array);

        return rep;
    }
   
    /**
     * Calculate number clusters in sector. Only clusters with at least one node are counted.
     * @param sectorStat
     * @return Number clusters in sector.
     */
    private int calculateClusterNum(SysStat sectorStat) {
        
        int result = 0;
        ClusterStat [] clusters = sectorStat.getClusterList();
        
        for(ClusterStat cluster : clusters) {
            // Only count if cluster has > 0 nodes
            if(cluster != null && cluster.getTotalNodes() > 0)
                result++;
        }
        
        return result;
    }

}
