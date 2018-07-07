package com.sahoora.examples.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/api")
public class HelloRestResource {

    private String saying;

    public HelloRestResource(final String saying) {
        this.saying = saying;
    }

    @Timed
    @Path("/hello")
    @GET
    public String hello() {
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "Unknown";
        }
        return saying + hostAddress;
    }
}
