package com.cmiethling.mplex.device;

import com.cmiethling.mplex.device.config.DeviceMessageConfig;
import com.cmiethling.mplex.device.config.WebSocketConfig;
import com.cmiethling.mplex.device.service.DeviceMessageService;
import com.cmiethling.mplex.device.service.EventCommandFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        WebSocketConfig.class,
        DeviceMessageService.class, DeviceMessageConfig.class,
        EventCommandFactory.class
})
public class TestConfig {
}
