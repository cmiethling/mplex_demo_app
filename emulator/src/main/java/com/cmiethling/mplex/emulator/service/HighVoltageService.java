package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.api.hv.HighVoltageError;
import com.cmiethling.mplex.emulator.model.HighVoltageStatus;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HighVoltageService {

    @Autowired
    private HighVoltageStatus highVoltageStatus;

    public void processError(@NonNull final String currentError, @NonNull final String newError) {
        if (!newError.equals(currentError))
            this.highVoltageStatus.setHighVoltageError(HighVoltageError.valueOf(newError));
    }

    public SubsystemError getHighVoltageError() {return this.highVoltageStatus.getHighVoltageError();}
}
