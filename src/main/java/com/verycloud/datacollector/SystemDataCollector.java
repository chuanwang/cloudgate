/**
 *
 * SystemDataCollector.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.datacollector;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONException;

/**
 * Data collector to colect system CPU and memory data and put them into queue.
 */
public class SystemDataCollector {

    private static final String TIME_FORMAT = "HH:mm:ss"; //$NON-NLS-1$
    
    /** Flag to indicate whether to stop collecting thread */
    private boolean shouldStop;
    
    private Deque<CpuStatusData> cpuDataQueue;
    
    private Deque<MemoryStatusData> memoryDataQueue;
    
    
    public void start() {
       
        // create deque to contain CPU data. It will hold 60 data points 
        // for one minute of during. So data collecting thread will collect
        // data every second.
        cpuDataQueue = new LinkedBlockingDeque<CpuStatusData>(60);
       
        // deque to hold memory data
        memoryDataQueue = new LinkedBlockingDeque<MemoryStatusData>(60);
        
        Thread thread = new Thread(new DataCollectThread());
        
        thread.start();
    }
    
    public void stop() {
        setShouldStop(true);
    }
    
    public CpuStatusData getCurrentCpuData() {
        return cpuDataQueue.getLast();
    }
    
    public Iterator<CpuStatusData> getCpuDataIterator() {
        return cpuDataQueue.iterator();
    }
    
    public MemoryStatusData getCurrentMemoryData() {
        return memoryDataQueue.getLast();
    }
    
    public Iterator<MemoryStatusData> getMemoryIterator() {
        return memoryDataQueue.iterator();
    }
    
    /**
     * TODO Add class description here.
     */
    protected class DataCollectThread implements Runnable {

        /**
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            while(!shouldStop) {
                Sigar sigar = new Sigar();
                CpuPerc cpuPerc = null;
                Mem memory = null;
                try {
                    
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
                    String timestamp = format.format(cal.getTime());
                    
                    cpuPerc = sigar.getCpuPerc();
                    CpuStatusData cpuData = new CpuStatusData();
                    cpuData.setTimestamp(timestamp);
                    cpuData.setIdle(trimDouble(cpuPerc.getIdle()));
                    cpuData.setUser(trimDouble(cpuPerc.getUser()));
                    cpuData.setSystem(trimDouble(cpuPerc.getSys()));
                    
                    if(cpuDataQueue.size() >= 60) {
                        cpuDataQueue.removeFirst();
                    }
                    cpuDataQueue.add(cpuData);
                    
                    memory = sigar.getMem();
                    MemoryStatusData memData = new MemoryStatusData();
                    memData.setTimestamp(timestamp);
                    memData.setFree((memory.getActualFree())/1000);
                    memData.setUsed((memory.getActualUsed())/1000);
                    if(memoryDataQueue.size() >= 60) 
                        memoryDataQueue.removeFirst();
                    memoryDataQueue.add(memData);
                        
                    Thread.sleep(1000);//sleep for 1000 ms
                } catch (SigarException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        }
        
    }
    
    private double trimDouble(double arg) {
        DecimalFormat df = new DecimalFormat("0.00");
        String out = df.format(arg);
        return Double.valueOf(out);
    }
    

    /**
     * Set the shouldStop.
     * @param shouldStop boolean
     */
    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    /**
     * Get the shouldStop.
     * @return boolean
     */
    public boolean isShouldStop() {
        return shouldStop;
    }
}
