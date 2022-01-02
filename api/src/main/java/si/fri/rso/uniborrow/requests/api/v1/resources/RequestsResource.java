package si.fri.rso.uniborrow.requests.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

import si.fri.rso.uniborrow.requests.lib.Request;
import si.fri.rso.uniborrow.requests.models.entities.RequestEntity;
import si.fri.rso.uniborrow.requests.services.beans.RequestBean;
import si.fri.rso.uniborrow.requests.services.config.RestProperties;
import si.fri.rso.uniborrow.requests.services.items.ItemsService;

@ApplicationScoped
@Path("/requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class RequestsResource {

    private final Logger log = Logger.getLogger(RequestsResource.class.getName());

    @Inject
    private RequestBean requestBean;

    @Inject
    private RestProperties rp;

    @Inject
    private ItemsService is;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getRequest() {
        List<RequestEntity> requests = requestBean.getRequestsFilter(uriInfo);
        return Response.status(200).entity(requests).build();
    }

    @GET
    @Path("/{requestId}")
    public Response getRequest(@PathParam("requestId") Integer requestId) {
        Request request = requestBean.getRequest(requestId);
        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (rp.getMaintenanceMode()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(request).build();
    }

    @POST
    public Response createRequest(Request request) {
        if (request == null || request.getMessage() == null || request.getTitle() == null || request.getTimestampStart() == null || request.getTimestampEnd() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Request createdRequest = requestBean.createRequest(request);
        if (createdRequest == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if(is.checkUserExists(request.getUserId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).entity(createdRequest).build();
    }

    @PUT
    @Path("{requestId}")
    public Response updateRequest(Request request, @PathParam("requestId") Integer requestId) {

        request = requestBean.putRequest(request, requestId);
        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Path("{requestId}")
    public Response patchRequest(Request request, @PathParam("requestId") Integer requestId) {
        request = requestBean.patchRequest(request, requestId);
        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{requestId}")
    public Response deleteRequest(@PathParam("requestId") Integer requestId) {
        boolean isSuccessful = requestBean.deleteRequest(requestId);
        if (isSuccessful) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}