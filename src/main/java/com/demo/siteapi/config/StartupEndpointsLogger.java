package com.demo.siteapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Logs the key application URLs (Swagger UI, H2 Console) when the
 * application has fully started.  Makes it trivial to open the API
 * documentation or inspect the in‑memory database without remembering
 * ports or paths.
 */
@Component
public class StartupEndpointsLogger {

    private static final Logger log = LoggerFactory.getLogger(StartupEndpointsLogger.class);

    @Value("${server.port:8080}")
    private int port;

    /**
     * Prints the local Swagger UI and H2 Console addresses to the
     * console at startup.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logEndpoints() {
        String swaggerUrl = "http://localhost:" + port + "/swagger-ui/index.html";
        String h2ConsoleUrl = "http://localhost:" + port + "/h2-console";

        log.info("");
        log.info(" Swagger UI available at: {}", swaggerUrl);
        log.info(" H2 Console available at: {}", h2ConsoleUrl);
        log.info("");
    }
}