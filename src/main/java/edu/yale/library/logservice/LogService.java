package edu.yale.library.logservice;

import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;

import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Osman Din
 */
@Path("/log")
public class LogService {

    private final Logger logger = getLogger(this.getClass());

    @GET
    @Path("/{param}")
    public Response transform(@PathParam("param") String msg) {
        logger.debug("GET request for:{}", msg);
        final String result = "Nothing here for=" + msg;
        return Response.status(200).entity(result).build();
    }


}


