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

public interface IHandshakeResponse {
    /*
     * The result of the handshake
     */
    HandshakeResult getHandshakeResult();

    /*
     * The endpoint used to exchange messages.
     */
    String getEndPoint();

    OcppVersion getOcppVersion();

    /**
     * Optional description of the response.
     * E.g. an explanation of why a connection was rejected.
     */
    String getReason();
}
