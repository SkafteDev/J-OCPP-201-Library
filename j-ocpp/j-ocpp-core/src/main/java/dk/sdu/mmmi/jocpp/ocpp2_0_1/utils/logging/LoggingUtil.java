/*
 * Copyright (c) 2025 SDU Center for Energy Informatics, University of Southern Denmark.
 * All rights reserved.
 *
 * Author: Christian Skafte Beck Clausen (csbc@mmmi.sdu.dk)
 *
 *  This code is proprietary and confidential.
 *  Unauthorized copying of this file, via any medium, is strictly prohibited unless permission
 *  is granted by SDU Center for Energy Informatics, University of Southern Denmark.
 */
package dk.sdu.mmmi.jocpp.ocpp2_0_1.utils.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingUtil {
    /**
     * Sets the logging level for all loggers in the OCPP framework.
     * E.g.:
     * Level.OFF
     * Level.INFO
     * Level.WARNING
     * Level.SEVERE
     * @param level
     */
    public static void setLevel(Level level) {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(level);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(level);
        }
    }
}
