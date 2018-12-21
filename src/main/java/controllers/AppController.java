package controllers;

import models.Personne;
import services.PersonneManager;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("ws")
public class AppController {

    @Inject
    PersonneManager personneManager;



    @GET()
    @Path("test")
    public String test(){
        return "coucou";
    }

    @POST()
    @Path("add")
    public Response addPerson(
            @FormParam(value = "nom") String nom,
            @FormParam(value = "prenom") String prenom,
            @FormParam(value = "email") String email,
            @FormParam(value = "birthdate") String birthdate)
    {

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        System.out.println("coucou");

        Map<String, String> map = new HashMap<>();
        map.put("nom", nom);
        map.put("prenom", prenom);
        map.put("email", email);
        map.put("birthdate", birthdate);
        map.put("password", "pantoufle");

        Personne p = personneManager.register(map);

        if(p == null){
            jsonObjectBuilder.add("succes", false);
        }
        else {
            jsonObjectBuilder.add("succes", true);
        }

        return Response.ok().entity(jsonObjectBuilder.build()).build();

    }

    @GET()
    @Path("/search")
    public Response search(@QueryParam(value = "q") String query){


        return Response.ok().build();
    }

}
