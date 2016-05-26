package edu.yale.library.fileservice;

import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Kicks off indexing. Meant to replace with a cron type process
 */
@Path("/index")
public class Indexer {

    private final Logger logger = getLogger(this.getClass());

    @GET
    @Path("/{param}")
    public Response get(@PathParam("param") String msg) {
        logger.debug("INDEX request for:{}", msg);
        DBManager dbManager = new DBManager();
        try {
            dbManager.insert();
            return Response.status(200).build();
        } catch (Exception e) {
            logger.error("Error:", e);
        }
        return Response.status(404).build(); //TODO
    }
}
