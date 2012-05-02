/**
 *
 * ClusterStatusResource.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.resources;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import com.opendatagroup.sector.sectorjni.ClusterStat;
import com.opendatagroup.sector.sectorjni.SlaveStat;
import com.opendatagroup.sector.sectorjni.SysStat;
import com.verycloud.datacollector.SysInfoUtil;

/**
 * TODO Add class description here.
 */
public class ClusterStatusResource extends SectorServerResource {

    /**
     * @return Cluster status and slave nodes status inside cluster.
     */
    @Get("json")
    public Representation retrieveClusterStatus() {
        
        SysStat sectorStat = getSectorClient().getClient().sectorSysInfo();
        
        ClusterStat[] clusters = sectorStat.getClusterList();
        
        JSONArray array = new JSONArray();
        try {
            int i = 0;
            for (ClusterStat cluster : clusters) {
                //cluster with 0 node doesn't count
                if(cluster.getTotalNodes() <=0 ) continue;
                
                JSONObject obj = new JSONObject();
                obj.put("id", cluster.getId()); //$NON-NLS-1$
                obj.put("nodeNumber", cluster.getTotalNodes()); //$NON-NLS-1$
                
                long diskSpace = cluster.getTotalDiskSpace();
                obj.put("availableDisk", SysInfoUtil.getFormattedSize(diskSpace)); //$NON-NLS-1$
                
                long fileSize = cluster.getTotalFileSize();
                obj.put("fileSize", SysInfoUtil.getFormattedSize(fileSize)); //$NON-NLS-1$
                
                long inputData = cluster.getTotalInputData();
                obj.put("netIn", SysInfoUtil.getFormattedSize(inputData)); //$NON-NLS-1$
                
                long outputData = cluster.getTotalOutputData();
                obj.put("netOut", SysInfoUtil.getFormattedSize(outputData)); //$NON-NLS-1$

                JSONArray slaveNodes = getSlaveNodesJson(sectorStat, cluster);
                obj.put("slaveNodes", slaveNodes); //$NON-NLS-1$
                array.put(i++, obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Representation rep = new JsonRepresentation(array);

        return rep;

    }

    /**
     * @param sectorStat
     * @param cluster
     * @return Slave node json array.
     */
    private JSONArray getSlaveNodesJson(SysStat sectorStat, ClusterStat cluster) {
        
        int clusterId = cluster.getId();
        SlaveStat [] slaves = sectorStat.getSlaveList();
        
        JSONArray result = new JSONArray();
        
        if(slaves == null)
            return result;

        int i = 0;
        for (SlaveStat slave : slaves) {
            
            int slaveClusterId = slave.getClusterId();
            
            if(clusterId != slaveClusterId) continue;
            
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", slave.getId()); //$NON-NLS-1$
                obj.put("clusterId", slave.getClusterId()); //$NON-NLS-1$
                
                int status = slave.getStatus();
                obj.put("status", SysInfoUtil.getSlaveNodeStatus(status)); //$NON-NLS-1$
                
                obj.put("address", slave.getIp()); //$NON-NLS-1$
                
                long diskSpace = slave.getTotalDiskSpace();
                obj.put("availableDisk", SysInfoUtil.getFormattedSize(diskSpace)); //$NON-NLS-1$
                
                long fileSize = slave.getTotalFileSize();
                obj.put("fileSize", SysInfoUtil.getFormattedSize(fileSize)); //$NON-NLS-1$
                
                long memSize = slave.getCurrMemUsed();
                obj.put("memory", SysInfoUtil.getFormattedSize(memSize)); //$NON-NLS-1$
                
                obj.put("cpu", slave.getCurrCpuUsed()); //$NON-NLS-1$
                
                long inputData = slave.getTotalInputData();
                obj.put("netIn", SysInfoUtil.getFormattedSize(inputData)); //$NON-NLS-1$
                
                long outputData = slave.getTotalOutputData();
                obj.put("netOut", SysInfoUtil.getFormattedSize(outputData)); //$NON-NLS-1$
                
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(slave.getLastUpdateTimestamp());
                SimpleDateFormat format = new SimpleDateFormat(
                        "EEE, d MMM yyyy HH:mm:ss"); //$NON-NLS-1$
                obj.put("lastUpdate", format.format(cal.getTime())); //$NON-NLS-1$
                
                result.put(i++, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
