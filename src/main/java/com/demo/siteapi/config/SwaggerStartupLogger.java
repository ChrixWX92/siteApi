package com.demo.siteapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Logs the Swagger UI URL when the application has fully started.
 * <p>
 * This makes it trivial to open the API documentation without
 * remembering the port or path.
 */
@Component
public class SwaggerStartupLogger {

    private static final Logger log = LoggerFactory.getLogger(SwaggerStartupLogger.class);

    @Value("${server.port:8080}")
    private int port;

    /**
     * Prints the local Swagger UI address to the console at startup.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logSwaggerUrl() {
        String swaggerUrl = "http://localhost:" + port + "/swagger-ui/index.html";
        log.info("");
        log.info(" Swagger UI available at: {}", swaggerUrl);
        log.info("");
    }
}