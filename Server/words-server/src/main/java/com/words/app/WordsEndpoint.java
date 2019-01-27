package com.words.app;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/words")
public class WordsEndpoint {

    public WordsEndpoint() {
    }

    @POST
    @Path("todo")
    @Produces(MediaType.APPLICATION_JSON)
    public void initializeDB() {
        System.out.println("post request called");
    }
}
