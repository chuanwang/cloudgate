/**
 *
 * CpuStatusResource.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.resources;

import java.text.DecimalFormat;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.verycloud.datacollector.CpuStatusData;
import com.verycloud.datacollector.SystemDataCollector;

/**
 * TODO Add class description here.
 */
public class CpuStatusResource extends ServerResource {

    private SystemDataCollector collector;

    /**
     * @return Cpu status.
     */
    @Get("json")
    public Representation retrieveCpuStatus() {

        JSONArray arr = new JSONArray();
        try {
            CpuStatusData cpuData = collector.getCurrentCpuData();

            JSONObject idle = new JSONObject();
            idle.put("type", "Idle");
            idle.put("usage", cpuData.getIdle());
            arr.put(0, idle);

            JSONObject user = new JSONObject();
            user.put("type", "User");
            user.put("usage", cpuData.getUser());
            arr.put(1, user);

            JSONObject sys = new JSONObject();
            sys.put("type", "System");
            sys.put("usage", cpuData.getSystem());
            arr.put(2, sys);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Representation rep = new JsonRepresentation(arr);

        return rep;

    }

    /**
     * Set the collector.
     * 
     * @param collector
     *            SystemDataCollector
     */
    public void setCollector(SystemDataCollector collector) {
        this.collector = collector;
    }

    /**
     * Get the collector.
     * 
     * @return SystemDataCollector
     */
    public SystemDataCollector getCollector() {
        return collector;
    }
}
