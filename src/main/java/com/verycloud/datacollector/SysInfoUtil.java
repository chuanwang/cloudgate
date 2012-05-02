/**
 *
 * SysInfoUtil.java
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

/**
 * TODO Add class description here.
 */
public class SysInfoUtil {

    private static final double SIZE_KB = 1000.000;
    private static final double SIZE_MB = SIZE_KB * 1000;
    private static final double SIZE_GB = SIZE_MB * 1000;
    private static final double SIZE_TB = SIZE_GB * 1000;
    private static final double SIZE_PB = SIZE_TB * 1000;
    
    public static enum SlaveNodeStatus {
        DOWN,
        NORMAL,
        DISKFULL,
        ERROR,
        UNKNOWN;
    }
    
    /**
     * Round the number up based on the size.
     * @param size
     * @return Rounded number.
     */
    public static String getFormattedSize(long size) {
        DecimalFormat dec = new DecimalFormat("#0.000 B"); //$NON-NLS-1$
        if(size < SIZE_KB)
            return dec.format(size);
        else if(size < SIZE_MB) {
            dec = new DecimalFormat("#0.000 KB"); //$NON-NLS-1$
            return dec.format(size / SIZE_KB);
        } else if(size < SIZE_GB) {
            dec = new DecimalFormat("#0.000 MB"); //$NON-NLS-1$
            return dec.format(size / SIZE_MB);
        } else if(size < SIZE_TB) {
            dec = new DecimalFormat("#0.000 GB"); //$NON-NLS-1$
            return dec.format(size / SIZE_GB);
        } else if(size < SIZE_PB) {
            dec = new DecimalFormat("#0.000 TB"); //$NON-NLS-1$
            return dec.format(size / SIZE_TB);
        } else {
            dec = new DecimalFormat("#0.000 PB"); //$NON-NLS-1$
            return dec.format(size / SIZE_PB);
        }
    }
    
    /**
     * @param status 
     * @return status string.
     */
    public static String getSlaveNodeStatus(int status) {
        
        SlaveNodeStatus result = SlaveNodeStatus.values()[status];
        return result.name();
        
    }
    
    /**
     * @param time  Time in second from epoch.
     * @return Formatted time string.
     */
    public static String getFormattedTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "EEE, d MMM yyyy HH:mm:ss"); //$NON-NLS-1$
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time*1000);
        return formatter.format(cal.getTime());
    }
}
