package org.baeldung.web.live.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource({ "classpath:web-${envTarget:local}.properties" })
public class CommonPaths {

    @Value("${http.protocol}")
    private String protocol;

    @Value("${http.port}")
    private String port;

    @Value("${http.host}")
    private String host;

    @Value("${http.address}")
    private String address;

    // API

    public String getServerRoot() {
        if (port.equals("80")) {
            return protocol + "://" + host + "/" + address;
        }
        return protocol + "://" + host + ":" + port + "/" + address;
    }

}
