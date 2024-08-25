package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.api.hv.ErrorsEvent;
import com.cmiethling.mplex.device.api.hv.HighVoltageError;
import com.cmiethling.mplex.device.message.Subsystem;
import com.cmiethling.mplex.emulator.AbstractSubsystem;
import com.cmiethling.mplex.emulator.model.HighVoltageStatus;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HighVoltageService extends AbstractSubsystem {

    @Autowired
    private HighVoltageStatus highVoltageStatus;

    protected HighVoltageService() {
        super(Subsystem.HIGH_VOLTAGE);
    }

    public SubsystemError getHighVoltageError() {return this.highVoltageStatus.getHighVoltageError();}

    public void processError(@NonNull final String currentError, @NonNull final String newError) throws IOException,
            DeviceException {
        if (!newError.equals(currentError)) {
            final var error = HighVoltageError.valueOf(newError);
            this.highVoltageStatus.setHighVoltageError(error);
            sendErrorEvent(error, ErrorsEvent.TOPIC, ErrorsEvent.ERRORCODE);
        }
    }
}
