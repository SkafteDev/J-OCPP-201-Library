/*
 * Copyright (c) 2024 SDU Center for Energy Informatics, University of Southern Denmark.
 * All rights reserved.
 *
 * Author: Christian Skafte Beck Clausen (csbc@mmmi.sdu.dk)
 *
 *  This code is proprietary and confidential.
 *  Unauthorized copying of this file, via any medium, is strictly prohibited unless permission
 *  is granted by SDU Center for Energy Informatics, University of Southern Denmark.
 */

package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

public interface IOCPPSession {
    void disconnect();
    ICsmsEndpoint getCsms();
    ICsEndpoint getChargingStation();

    SessionInfo getSessionInfo();

    interface SessionInfo {
        String getCsId();
        String getCsmsId();
        String getConnectionURI();
        String getTransportType();
        OcppVersion getOCPPVersion();
    }
}
