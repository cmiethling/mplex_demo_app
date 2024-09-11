package com.cmiethling.mplex.device.api.fluidics;

import com.cmiethling.mplex.device.api.AbstractDeviceCommandTest;
import org.junit.jupiter.api.Test;

public final class SetGelValveCommandTest extends AbstractDeviceCommandTest {

    @Test
    public void command() throws Exception {
        final var command = this.eventCommandFactory.command(SetGelValveCommand.class);
        command.setOn(true);

        toRequestMessage(command, "SetGelValve_request");
    }

    @Test
    public void result() throws Exception {
        fromResultMessage(this.eventCommandFactory.command(SetGelValveCommand.class), "SetGelValve_result");
    }
}

