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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.OCPPRequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class OCPPOverNatsDispatcher implements IRequestHandlerRegistry {
    private static final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final IMessageRouteResolver msgRouteResolver;

    protected Map<OCPPMessageType, OCPPRequestHandler<?, ?>> requestHandlers;

    public OCPPOverNatsDispatcher(IMessageRouteResolver msgRouteResolver) {
        this.msgRouteResolver = msgRouteResolver;
        this.requestHandlers = new HashMap<>();
    }

    @Override
    public <TRequest, TResponse> void addRequestHandler(OCPPMessageType requestType, OCPPRequestHandler<TRequest, TResponse> requestHandler) {
        if (requestHandlers.containsKey(requestType)) {
            logger.warn(String.format("Request handler for OCPPMessageType=%s already exists. Aborting.",
                    requestType.getValue()));
            return;
        }

        logger.info(String.format("Added request handler for OCPPMessageType=%s.", requestType.getValue()));
        requestHandler.activate();
        this.requestHandlers.put(requestType, requestHandler);
    }

    @Override
    public void removeRequestHandler(OCPPMessageType requestType) {
        if (!requestHandlers.containsKey(requestType)) {
            logger.warn(String.format("Request handler for OCPPMessageType=%s does not exists. Aborting.",
                    requestType.getValue()));
        }

        requestHandlers.remove(requestType);
    }

    @Override
    public boolean existsRequestHandler(OCPPMessageType requestType) {
        return requestHandlers.containsKey(requestType);
    }

    public IMessageRouteResolver getMsgRouteResolver() {
        return msgRouteResolver;
    }
}
