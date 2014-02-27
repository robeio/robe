package io.robe.common.system;

import com.sun.management.HotSpotDiagnosticMXBean;
import io.robe.common.utils.FileOperations;

import javax.management.MBeanServer;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.UUID;

/**
 * Helper class to get heapdump of the current application.
 * Only works with Oracle HotSpot
 */
public class HeapDump {
    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";

    private static volatile HotSpotDiagnosticMXBean hotspotMBean;

    /**
     * Call this method from your application whenever you
     * want to dump the heap snapshot into a to a temp file.
     *
     * @param live flag that tells whether to dump only the live objects
     * @return heap file
     */
    public static File dump(boolean live) {
        initHotspotMBean();
        try {
            String tempName = FileOperations.TEMPDIR + UUID.randomUUID().toString().replaceAll("-", "");
            hotspotMBean.dumpHeap(tempName, live);
            return new File(tempName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize MBean
     */
    private static void initHotspotMBean() {
        if (hotspotMBean == null) {
            synchronized (HeapDump.class) {
                if (hotspotMBean == null) {
                    hotspotMBean = getHotspotMBean();
                }
            }
        }
    }

    /**
     * @return {@link HotSpotDiagnosticMXBean} instance
     */
    private static HotSpotDiagnosticMXBean getHotspotMBean() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            HotSpotDiagnosticMXBean bean = ManagementFactory.newPlatformMXBeanProxy(server, HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
            return bean;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

}