package si.fri.rso.uniborrow.requests.services.items;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class ItemsService {
    private WebTarget webTarget = ClientBuilder.newClient().target("http://items:8080");

    public boolean checkItemExists(String itemName) {
        return false;
    }
}
