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

package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

import java.util.UUID;

public class CsController implements Runnable {

    private final IOCPPSession ocppSession;

    public CsController(IOCPPSession ocppSession) {
        this.ocppSession = ocppSession;
    }

    /**
     * Implement the CS logic here...
     */
    @Override
    public void run() {
        /*
         * (1) Build the BootNotificationRequest (payload) object to be sent to the CSMS.
         */
        BootNotificationRequest bootNotificationRequest = BootNotificationRequest.builder()
                .withChargingStation(ChargingStation.builder()
                        .withVendorName("ABB")
                        .withSerialNumber("f8125503-8d0f-467f-abad-b830ca6782e2")
                        .withModel("ABB TAC-W11-G5-R-0")
                        .withFirmwareVersion("3.1.3")
                        .build())
                .withReason(BootReasonEnum.POWER_UP)
                .build();

        /*
         * (2) Encapsulate the BootNotificationRequest payload within an RPC CALL.
         */
        ICall<BootNotificationRequest> bootRequest = CallImpl.<BootNotificationRequest>newBuilder()
                .withMessageId(UUID.randomUUID().toString())
                .asAction(OCPPMessageType.BootNotificationRequest.getAction())
                .withPayLoad(bootNotificationRequest)
                .build();

        /*
         * (3) Send the BootNotificationRequest and block until receiving a BootNotificationResponse.
         */
        Headers headers = Headers.emptyHeader();
        ICallResult<BootNotificationResponse> response = ocppSession.getCsms().sendBootNotificationRequest(headers, bootRequest);
        System.out.println(response);

        /*
         * (4) Send another request from CS -> CSMS.
         */
        ICall<ClearedChargingLimitRequest> call = CallImpl.<ClearedChargingLimitRequest>newBuilder()
                .asAction(OCPPMessageType.ClearedChargingLimitRequest.getAction())
                .withMessageId(UUID.randomUUID().toString())
                .withPayLoad(ClearedChargingLimitRequest.builder()
                        .withEvseId(0)
                        .withChargingLimitSource(ChargingLimitSourceEnum.EMS)
                        .build())
                .build();
        ICallResult<ClearedChargingLimitResponse> result = ocppSession.getCsms().sendClearedChargingLimitRequest(Headers.emptyHeader(), call);
        System.out.println(result);
    }
}
