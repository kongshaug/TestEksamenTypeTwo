/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTO.JokeDTO;
import DTO.QuoteDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errorhandling.NotFoundException;
import facades.DataFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author aamandajuhl
 */
@Path("jokeByCategory/")
public class JokeResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final DataFacade DF = DataFacade.getDataFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @Context
    private UriInfo context;
    @Path("{categories}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getQoutes(@PathParam("categories") String categories) throws NotFoundException, IOException, Exception {
        
        String[] CategoriList = categories.split(",");
        if(CategoriList.length > 4)
        {
        //Make error message with max length of request
        }
        try {
            List<JokeDTO> Jokes = DF.getData(CategoriList);
            return GSON.toJson(Jokes);
        } catch (InterruptedException | ExecutionException ex) {

            throw new NotFoundException(ex.getMessage());
        }
    }
}
