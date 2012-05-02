/**
 *
 * SectorServerResource.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.resources;

import org.restlet.resource.ServerResource;

import com.verycloud.datacollector.SectorClient;

/**
 * TODO Add class description here.
 */
public class SectorServerResource extends ServerResource {

    /** Sector client which contains login object */
    private SectorClient sectorClient;

    /**
     * Set the sectorClient.
     * @param sectorClient1 SectorClient
     */
    public void setSectorClient(SectorClient sectorClient1) {
        this.sectorClient = sectorClient1;
    }

    /**
     * Get the sectorClient.
     * @return SectorClient
     */
    public SectorClient getSectorClient() {
        return sectorClient;
    }
    
}
