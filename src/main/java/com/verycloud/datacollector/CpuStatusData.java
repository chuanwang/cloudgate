/**
 *
 * CpuStatusData.java
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
public class CpuStatusData {

    private String timestamp;
    
    /** CPU idle time*/
    private double idle;
    
    private double user;
    
    private double system;

    /**
     * Get the idle.
     * @return double
     */
    public double getIdle() {
        return idle;
    }

    /**
     * Set the idle.
     * @param newIdle double
     */
    public void setIdle(double newIdle) {
        idle = newIdle;
    }

    /**
     * Get the user.
     * @return double
     */
    public double getUser() {
        return user;
    }

    /**
     * Set the user.
     * @param newUser double
     */
    public void setUser(double newUser) {
        user = newUser;
    }

    /**
     * Get the system.
     * @return double
     */
    public double getSystem() {
        return system;
    }

    /**
     * Set the system.
     * @param newSystem double
     */
    public void setSystem(double newSystem) {
        system = newSystem;
    }

    /**
     * Set the timeStamp.
     * @param timeStamp String
     */
    public void setTimestamp(String timeStamp) {
        this.timestamp = timeStamp;
    }

    /**
     * Get the timeStamp.
     * @return String
     */
    public String getTimestamp() {
        return timestamp;
    }
    
    
}
