package com.sahoora.examples.dropwizard.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/api")
public class HelloRestResource {

    @Path("/hello")
    @GET
    public String hello() {
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "Unknown";
        }
        return "Hello Dropwizard is running on "+ hostAddress;
    }
}
