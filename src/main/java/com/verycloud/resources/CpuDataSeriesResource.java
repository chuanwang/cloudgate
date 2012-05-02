/**
 *
 * CpuDataSeriesResource.java
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
import com.verycloud.datacollector.SystemDataCollector;

/**
 * TODO Add class description here.
 */
public class CpuDataSeriesResource extends ServerResource {

    /** */
    private SystemDataCollector collector;


    /**
     * @return Cpu status.
     */
    @Get("json")
    public Representation retrieveCpuStatus() {
        int i = 0;
        JSONArray array = new JSONArray();
        Iterator<CpuStatusData> iter = collector.getCpuDataIterator();
        while(iter.hasNext()) {
            CpuStatusData cpuData = iter.next();
            JSONObject obj = new JSONObject();
            try {
                obj.put("timestamp", cpuData.getTimestamp());
                obj.put("idle", cpuData.getIdle());
                obj.put("system", cpuData.getSystem());
                obj.put("user", cpuData.getUser());
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
     * Get the collector.
     * @return SystemDataCollector
     */
    public SystemDataCollector getCollector() {
        return collector;
    }

    /**
     * Set the collector.
     * @param newCollector SystemDataCollector
     */
    public void setCollector(SystemDataCollector newCollector) {
        collector = newCollector;
    }

}
