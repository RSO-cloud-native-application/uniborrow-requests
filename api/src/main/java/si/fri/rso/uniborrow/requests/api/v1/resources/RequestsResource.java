package si.fri.rso.uniborrow.requests.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;
import com.kumuluz.ee.logs.cdi.Log;

import si.fri.rso.uniborrow.requests.lib.Request;
import si.fri.rso.uniborrow.requests.models.entities.RequestEntity;
import si.fri.rso.uniborrow.requests.services.beans.RequestBean;
import si.fri.rso.uniborrow.requests.services.config.RestProperties;
import si.fri.rso.uniborrow.requests.services.items.ItemsService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.metrics.annotation.Metered;

@ApplicationScoped
@Log
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
    @Metered(name = "get_requests_rate")
    @Operation(description = "Get requests by filter, or all.", summary = "Get requests by filter, or all.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Requests that fit the filter.",
                    content = @Content(schema = @Schema(implementation = RequestEntity.class, type = SchemaType.ARRAY))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "No requests found."
            )
    })
    public Response getRequest() {
        List<RequestEntity> requests = requestBean.getRequestsFilter(uriInfo);
        return Response.status(200).entity(requests).build();
    }

    @GET
    @Path("/active")
    @Metered(name = "get_requests_active_rate")
    @Operation(description = "Get active requests.", summary = "Get active requests.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Active request.",
                    content = @Content(schema = @Schema(implementation = RequestEntity.class, type = SchemaType.ARRAY))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "No active requests found."
            )
    })
    public Response getActiveRequest() {
        List<RequestEntity> requests = requestBean.getRequestsFilter(uriInfo);
        List<RequestEntity> validRequests = new ArrayList<RequestEntity>();
        for (RequestEntity request : requests) {
            if(request.getTimestampEnd().isAfter(Instant.now())) {
                validRequests.add(request);
            }
        }
        return Response.status(200).entity(validRequests).build();
    }


    @GET
    @Path("/{requestId}")
    @Operation(description = "Get request by id.", summary = "Get request by id.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Request by id.",
                    content = @Content(schema = @Schema(implementation = RequestEntity.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Request with id not found."
            )
    })
    public Response getRequest(@PathParam("requestId") Integer requestId) {
        Request request = requestBean.getRequest(requestId);
        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (rp.getMaintenanceMode()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var a = is.checkUserExists(2);

        System.out.println(a);

        return Response.status(Response.Status.OK).entity(request).build();
    }

    @POST
    @Timed(name = "get_requests_created_time")
    @Operation(description = "Create new request.", summary = "Create new request.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "CreatedRequest",
                    content = @Content(schema = @Schema(implementation = RequestEntity.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Problems with request body."
            )
    })
    public Response createRequest(RequestEntity requestEntity) {
        if (requestEntity == null || requestEntity.getMessage() == null
                || requestEntity.getTitle() == null || requestEntity.getTimestampStart() == null
                || requestEntity.getTimestampEnd() == null
                || requestEntity.getTimestampStart().isAfter(requestEntity.getTimestampEnd())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(!is.checkUserExists(requestEntity.getUserId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        requestEntity = requestBean.createRequest(requestEntity);

        return Response.status(Response.Status.CREATED).entity(requestEntity).build();
    }

    @PUT
    @Path("{requestId}")
    @Operation(description = "Edit a request.", summary = "Edit a request.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Edited request",
                    content = @Content(schema = @Schema(implementation = RequestEntity.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Problems with request body."
            )
    })
    public Response updateRequest(Request request, @PathParam("requestId") Integer requestId) {

        request = requestBean.putRequest(request, requestId);
        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Path("{requestId}")
    @Operation(description = "Patch a request.", summary = "Patch a request.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Patched request",
                    content = @Content(schema = @Schema(implementation = RequestEntity.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Problems with request body."
            )
    })
    public Response patchRequest(Request request, @PathParam("requestId") Integer requestId) {
        request = requestBean.patchRequest(request, requestId);
        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{requestId}")
    @Timed(name = "get_requests_deleted_time")
    @Operation(description = "Delete a request.", summary = "Delete a request.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Request deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Request not found."
            )
    })
    public Response deleteRequest(@PathParam("requestId") Integer requestId) {
        boolean isSuccessful = requestBean.deleteRequest(requestId);
        if (isSuccessful) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}