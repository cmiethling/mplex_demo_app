package com.cmiethling.mplex.emulator.model;

import com.cmiethling.mplex.device.api.fluidics.FluidicsError;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class FluidicsStatus {
    private FluidicsError fluidicsError;
}
