/**
 *
 * SampleResource.java
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
import org.restlet.resource.ServerResource;

import com.verycloud.datacollector.MemoryStatusData;
import com.verycloud.datacollector.SystemDataCollector;

/**
 * TODO Add class description here.
 */
public class MemoryStatusResource extends ServerResource {

    private SystemDataCollector collector;
    
    /**
     * @return System status in json format.
     */
    @Get("json")
    public Representation retrieveSystemStatus() {
        
        JSONArray arr = new JSONArray();
        try {
            MemoryStatusData memoryData = collector.getCurrentMemoryData();

            JSONObject free = new JSONObject();
            free.put("type", "Free");
            free.put("usage", memoryData.getFree());
            arr.put(0, free);

            JSONObject used = new JSONObject();
            used.put("type", "Used");
            used.put("usage", memoryData.getUsed());
            arr.put(1, used);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Representation rep = new JsonRepresentation(arr);

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
