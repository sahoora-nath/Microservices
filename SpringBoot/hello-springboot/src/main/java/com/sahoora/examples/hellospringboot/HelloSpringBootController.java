package com.sahoora.examples.hellospringboot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix = "helloapp")
public class HelloSpringBootController {

    private String saying;

    //to expose an HTTP GET endpoint at /hello (which will really be /api/hello)
    @RequestMapping(method = RequestMethod.GET, value = "/hello", produces = "text/plain")
    private String hello() {
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostName = "Unknown";
        }

        return saying + " "+ hostName;
    }

    public String getSaying() {
        return saying;
    }

    public void setSaying(String saying) {
        this.saying = saying;
    }
}
