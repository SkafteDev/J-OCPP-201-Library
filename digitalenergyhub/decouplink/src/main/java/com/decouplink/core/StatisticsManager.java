package com.decouplink.core;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mrj
 */
public class StatisticsManager {

    private static final StatisticsManager inst = new StatisticsManager();
    private Boolean enabled = null;
    private int addCount = 0, followCount = 0;
    private Set<Class> targetTypes = new HashSet<>();
    private Set<Class> actualTypes = new HashSet<>();

    private StatisticsManager() {
    }

    public static StatisticsManager getInstance() {
        return inst;
    }

    private boolean isEnabled() {
        if (enabled == null) {
            String s = System.getProperty("decouplink.statistics", "false");
            enabled = s.equals("true");
        }

        return enabled;
    }

    public synchronized void recordAddLink(Class<?> targetType, Class<?> actualType) {
        if (isEnabled()) {
            addCount++;
            targetTypes.add(targetType);
            actualTypes.add(actualType);
            printStatistics();
        }
    }

    public synchronized void recordFollowLink(Class<?> destType) {
        if (isEnabled()) {
            followCount++;
            printStatistics();
        }
    }

    private void printStatistics() {
        System.out.format(
                "Decouplink: Added=%d Followed=%d TargetTypes=%d ActualTypes=%d\n",
                addCount, followCount, targetTypes.size(), actualTypes.size());
    }
}
