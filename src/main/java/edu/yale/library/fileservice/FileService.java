package edu.yale.library.fileservice;

import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;

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
            dbManager.test(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String result = "Got request for path=" + msg;
        return Response.status(200).entity(result).build();
    }


}


