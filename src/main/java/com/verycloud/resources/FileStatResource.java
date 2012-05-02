/**
 *
 * FileStatResource.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import com.opendatagroup.sector.sectorjni.SNode;
import com.opendatagroup.sector.sectorjni.SectorJniClient;
import com.verycloud.datacollector.SysInfoUtil;

/**
 * File stat server resource.
 */
public class FileStatResource extends SectorServerResource {

    

    /**
     * @return File stat.
     */
    @Get("json")
    public Representation retrieveFileStat() {
        
        SectorJniClient client = getSectorClient().getClient();
        
        String filePath = (String) getRequestAttributes().get("name"); //$NON-NLS-1$
        filePath = "/" + filePath; //$NON-NLS-1$
        try {
            filePath = URLDecoder.decode(filePath, "UTF-8"); //$NON-NLS-1$
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        
        SNode stat = null;
        try {
            stat = client.sectorStat(filePath);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        JSONArray array = new JSONArray();
        try {

            JSONObject obj = new JSONObject();
            obj.put("name", stat.getName()); //$NON-NLS-1$
            obj.put("dir", stat.isDir()); //$NON-NLS-1$
            obj.put("size", stat.getSize()); //$NON-NLS-1$
            
            long timestamp = stat.getTimestamp();
            obj.put("lastMod", SysInfoUtil.getFormattedTime(timestamp)); //$NON-NLS-1$

            String[] locations = stat.getLocations();
            JSONArray locationsArray = new JSONArray();
            for (int i = 0; i < locations.length; i++) {
                locationsArray.put(i, locations[i]);
            }
            obj.put("locations", locationsArray); //$NON-NLS-1$
            obj.put("numOfRep", locations.length); //$NON-NLS-1$

            array.put(0, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Representation rep = new JsonRepresentation(array);

        return rep;

    }

}
