package com.mediconnect.config;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {
    @Bean
    ZoneId applicationZoneId(@Value("${app.time-zone:America/Bogota}") String zoneId) {
        return ZoneId.of(zoneId);
    }

    @Bean
    Clock applicationClock(ZoneId applicationZoneId) {
        return Clock.system(applicationZoneId);
    }
}
