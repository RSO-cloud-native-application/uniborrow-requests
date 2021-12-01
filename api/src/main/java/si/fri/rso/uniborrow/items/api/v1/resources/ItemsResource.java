package si.fri.rso.uniborrow.items.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

import si.fri.rso.uniborrow.items.lib.Item;
import si.fri.rso.uniborrow.items.models.entities.ItemEntity;
import si.fri.rso.uniborrow.items.services.beans.ItemBean;
import si.fri.rso.uniborrow.items.services.config.RestProperties;

@ApplicationScoped
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ItemsResource {

    private final Logger log = Logger.getLogger(ItemsResource.class.getName());

    @Inject
    private ItemBean itemBean;

    @Inject
    private RestProperties rp;

    @GET
    public Response getItems() {
        List<Item> items = itemBean.getItems();
        return Response.status(200).entity(items).build();
    }

    @GET
    @Path("/{itemId}")
    public Response getItem(@PathParam("itemId") Integer itemId) {
        Item item = itemBean.getItem(itemId);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        System.out.println(rp.getMaintenanceMode());

        if (rp.getMaintenanceMode()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(item).build();
    }

    @POST
    public Response createLoan(ItemEntity loanEntity) {
        if (loanEntity.getTitle() == null ||
                loanEntity.getDescription() == null || loanEntity.getUserId() == null
                || loanEntity.getCategory() == null) {
            return Response.status(300).build();
        } else {
            loanEntity = itemBean.createLoan(loanEntity);
        }
        return Response.status(Response.Status.OK).entity(loanEntity).build();
    }
}