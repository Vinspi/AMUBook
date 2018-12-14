package controllers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/ws")
public class IndexController {

    /* Premier exemple avec un GET */
    @GET()
    @Path("hello")
    public String hello() {
        return "Hello";
    }

    @PostConstruct
    public void start() {
        System.err.println("start " + this);
    }

    @PreDestroy
    public void stop() {
        System.err.println("stop " + this);
    }
}