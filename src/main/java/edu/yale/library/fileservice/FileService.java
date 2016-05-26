package edu.yale.library.fileservice;

import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Osman Din
 */
@Path("/search")
public class FileService {

    private final Logger logger = getLogger(this.getClass());

    @GET
    @Path("/{param}")
    public Response get(@PathParam("param") String msg) {
        logger.debug("GET request for:{}", msg);
        DBManager dbManager = new DBManager();
        try {
            List<String> s = dbManager.get(msg);
            return Response.status(200).entity(s).build();
        } catch (Exception e) {
            logger.error("Error:", e);
        }
        return Response.status(404).build(); //TODO
    }

    //TODO
    @GET
    @Path("/get")
    public Response getAll() {
        DBManager dbManager = new DBManager();
        try {
            List<String> s = dbManager.getAll();
            return Response.status(200).entity(s).build();
        } catch (Exception e) {
            logger.error("Error:", e);
        }
        return Response.status(404).build(); //TODO
    }
}


