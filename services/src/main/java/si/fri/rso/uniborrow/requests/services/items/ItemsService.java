package si.fri.rso.uniborrow.requests.services.items;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class ItemsService {
    private WebTarget webTarget = ClientBuilder.newClient().target("http://35.223.79.242/uniborrow-items");

    public boolean checkItemExists(String itemName) {
        Client restClient = ClientBuilder.newClient();
        List<Item> e = restClient
                .target("http://35.223.79.242/uniborrow-items/v1/items/?filter=title:EQ:" + itemName)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Item>> () {});

        if(e.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean checkUserExists(int userId) {
        Client restClient = ClientBuilder.newClient();
        User u = restClient
                .target("http://35.223.79.242/uniborrow-users/v1/users/" + userId)
                .request(MediaType.APPLICATION_JSON)
                .get(User.class);

        if(u == null) {
            return false;
        }
        return true;
    }
}
