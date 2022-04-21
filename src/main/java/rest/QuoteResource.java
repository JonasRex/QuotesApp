package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtoFacades.QuoteDTOFacade;
import dtos.QuoteDTO;
import errorhandling.EntityNotFoundException;
import facades.IFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


@Path("quote")
public class QuoteResource {

    private static final IFacade<QuoteDTO> FACADE =  QuoteDTOFacade.getFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Path("demo")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        QuoteDTO quoteDTO = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(quoteDTO)).build();
    }
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        QuoteDTO quoteDTO = GSON.fromJson(content, QuoteDTO.class);
        QuoteDTO newQuoteDTO = FACADE.create(quoteDTO);
        return Response.ok().entity(GSON.toJson(newQuoteDTO)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        QuoteDTO quoteDTO = GSON.fromJson(content, QuoteDTO.class);
        quoteDTO.setId(id);
        QuoteDTO updated = FACADE.update(quoteDTO);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        QuoteDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getCount()  {
        long count = FACADE.getCount();
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
}
