/**
 *
 * MemoryStatusData.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.datacollector;

/**
 * TODO Add class description here.
 */
public class MemoryStatusData {

    private String timestamp;
    
    private long used;
    
    private long free;

    /**
     * Get the used.
     * @return long
     */
    public long getUsed() {
        return used;
    }

    /**
     * Set the used.
     * @param newUsed long
     */
    public void setUsed(long newUsed) {
        used = newUsed;
    }

    /**
     * Get the free.
     * @return long
     */
    public long getFree() {
        return free;
    }

    /**
     * Set the free.
     * @param newFree long
     */
    public void setFree(long newFree) {
        free = newFree;
    }

    /**
     * Set the timestamp.
     * @param timestamp String
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the timestamp.
     * @return String
     */
    public String getTimestamp() {
        return timestamp;
    }

}
