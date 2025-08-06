package com.example.task.infrastructure.provider;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "nbp.retry")
@Getter
@Setter
class NbpApiRetryProperties {

    private int maxRetries;
    private Duration minBackoff;
    private Duration maxBackoff;

}
