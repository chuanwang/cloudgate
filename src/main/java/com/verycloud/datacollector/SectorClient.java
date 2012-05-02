/**
 *
 * SectorClient.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.datacollector;

import com.opendatagroup.sector.sectorjni.SectorJniClient;

/**
 * Class to init the sector client.
 */
public class SectorClient {
    
    /** sector jni client */
    private SectorJniClient client;

    /**
     * Init and login sector.
     */
    public void init() {
        
        // read bunch of sys envs
        //TODO: remove these once we have user management and authentication in place
        String host = System.getenv("SECTOR_HOST"); //$NON-NLS-1$
        int port = Integer.parseInt(System.getenv("SECTOR_PORT")); //$NON-NLS-1$
        String user = System.getenv("SECTOR_USER"); //$NON-NLS-1$
        String password = System.getenv("SECTOR_PASSWD"); //$NON-NLS-1$
        String certPath = System.getenv("SECTOR_CERTPATH"); //$NON-NLS-1$
        
        // Call JNI init and login
        client = new SectorJniClient();
        client.sectorInit(host, port);
        client.sectorLogin(user, password, certPath);
        
    }
        
    /**
     * Set the client.
     * @param client1 SectorJniClient
     */
    public void setClient(SectorJniClient client1) {
        this.client = client1;
    }

    /**
     * Get the client.
     * @return SectorJniClient
     */
    public SectorJniClient getClient() {
        return client;
    }

    public void logout() {
        
    }
}
