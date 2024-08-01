package com.cmiethling.mplex.device.service;

import com.cmiethling.mplex.device.DeviceMessageException;
import com.cmiethling.mplex.device.message.AbstractDeviceMessage;
import com.cmiethling.mplex.device.message.DeviceMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceMessageService {

    private final ObjectMapper objectMapper;

    @Autowired
    public DeviceMessageService(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String serializeMessage(final DeviceMessage message) throws DeviceMessageException {
        try {
            return this.objectMapper.writeValueAsString(message);
        } catch (final JsonProcessingException ex) {
            throw new DeviceMessageException("Error writing DeviceMessage to JSON string", ex, message);
        }
    }

    public DeviceMessage deserializeMessage(final String json) throws DeviceMessageException {
        try {
            return this.objectMapper.readValue(json, AbstractDeviceMessage.class);
        } catch (final JsonProcessingException ex) {
            throw new DeviceMessageException("Error reading JSON to DeviceMessage", ex);
        }
    }
}
