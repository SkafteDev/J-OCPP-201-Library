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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.devicemodel;

/**
 * This interface provides methods for accessing and modifying device model data.
 */
public interface IDeviceModel {

    /**
     * Gets the device model that represents the logical structure of a device, except for the VariableAttribute(s) of each Variable.
     * @return A DeviceModelMap consisting of a (Component, VariableMap) pair.
     * A VariableMap consists of a (Variable, VariableMetaData) pair.
     * A VariableMetaData consists of a struct of 1 VariableCharacteristics and * VariableMonitoring.
     */
    //DeviceModelMap getDeviceModel();

    //VariableAttribute getVariableAttribute(String componentId, String variableId, AttributeEnum attributeEnum);

    /**
     * Returns a list of VariableAttributes with maximum of 4 elements (Actual, Target, MinSet, MaxSet), and minimum size of 0 elements
     * (if no VariableAttributes is present for the requested parameter).
     * @param componentId
     * @param variableId
     * @return
     */
    //List<VariableAttribute> getVariableAttributes(String componentId, String variableId);

    /**
     * Sets the value of a VariableAttribute if present.
     * @param componentId
     * @param variableId
     * @param attributeEnum
     * @param value
     * @return
     */
    //boolean setVariableAttributeValue(String componentId, String variableId, AttributeEnum attributeEnum, String value);
}
