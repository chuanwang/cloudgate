/**
 *
 * MemoryDataSeriesResource.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.resources;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.verycloud.datacollector.CpuStatusData;
import com.verycloud.datacollector.MemoryStatusData;
import com.verycloud.datacollector.SystemDataCollector;

/**
 * TODO Add class description here.
 */
public class MemoryDataSeriesResource extends ServerResource {

    /** */
    private SystemDataCollector collector;

    /**
     * @return Cpu status.
     */
    @Get("json")
    public Representation retrieveCpuStatus() {
        int i = 0;
        JSONArray array = new JSONArray();
        Iterator<MemoryStatusData> iter = collector.getMemoryIterator();
        while(iter.hasNext()) {
            MemoryStatusData memoryData = iter.next();
            JSONObject obj = new JSONObject();
            try {
                obj.put("timestamp", memoryData.getTimestamp());
                obj.put("free", memoryData.getFree());
                obj.put("used", memoryData.getUsed());
                array.put(i++, obj);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        Representation rep = new JsonRepresentation(array);
        
        return rep;
        
    }
    /**
     * Set the collector.
     * @param collector SystemDataCollector
     */
    public void setCollector(SystemDataCollector collector) {
        this.collector = collector;
    }

    /**
     * Get the collector.
     * @return SystemDataCollector
     */
    public SystemDataCollector getCollector() {
        return collector;
    }

}
