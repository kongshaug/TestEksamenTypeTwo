package rest;

import DTO.JokesDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import DTO.QuoteDTO;
import entities.User;
import errorhandling.NotFoundException;
import facades.DataFacade;
import static facades.DataFacade.getDataFacade;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * @author
 */
@Path("info")
public class LoginResource {

     
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final DataFacade DF = DataFacade.getDataFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            List<User> users = em.createQuery("select user from User user").getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("jokeByCategoryV2/{categories}")
    @RolesAllowed("admin")
    public String getJokesForAdmin(@PathParam("categories") String categories) throws Exception {
     
         String[] CategoriList = categories.split(",");
        if(CategoriList.length > 12)
        {
        return "{\"msg\":\"to many categories you searched for "+CategoriList.length +" but only 12 is allowed\"}";
        }
        String[] catego = {"Career", "celebrity", "dev", "explicit", "fashion", "food", "history", "money", "movie", "music", "political", "science", "sport", "travel"};
       List<String> CategoriesList = Arrays.asList(catego);
        for (String subject : CategoriList) {
               if (!CategoriesList.contains(subject)){
           return "{\"msg\":\""+subject+" is not a categorie option\"}";
        }
    
        }
        try {
            JokesDTO Jokes = DF.getData(CategoriList);
            return GSON.toJson(Jokes);
        } catch (InterruptedException | ExecutionException ex) {

            throw new NotFoundException(ex.getMessage());
        }
    }
    
}
