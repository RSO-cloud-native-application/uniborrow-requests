package si.fri.rso.uniborrow.requests.services.items;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogCommons;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import si.fri.rso.uniborrow.requests.services.beans.RequestBean;

import java.time.temporal.ChronoUnit;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.util.logging.LogManager;
import javax.ws.rs.InternalServerErrorException;

import java.util.logging.Logger;


@ApplicationScoped
public class ItemsService {
    private static final Logger LOG = Logger.getLogger(RequestBean.class
            .getSimpleName());
    private WebTarget webTarget = ClientBuilder.newClient().target("http://35.223.79.242/uniborrow-items");


    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "checkItemExistsFallback")
    @DiscoverService(value = "uniborrow-items-service", environment = "dev", version = "1.0.0")
    public boolean checkItemExists(int itemId) {
        try {
            Response response = webTarget.path("/v1/items/" + itemId).request(MediaType.APPLICATION_JSON).buildGet().invoke();
            Item item = response.readEntity(Item.class);
            item.status = "Requested";
            System.out.println("I'm here now2");
            response = webTarget.path("/v1/items/2").request(MediaType.APPLICATION_JSON).buildPut(Entity.entity(item, MediaType.APPLICATION_JSON)).invoke();
            return response.getStatus() != 404;
        }
        catch (WebApplicationException | ProcessingException e) {
                //log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
        }

    }

    public boolean checkItemExistsFallback(int itemId) {
        System.out.println("I'm here now");
        return false;
    }



    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "checkUserExistsFallback")
    @Retry(maxRetries = 3)
    @DiscoverService(value = "uniborrow-users-service", environment = "dev", version = "1.0.0")
    public boolean checkUserExists(int userId) {
        LOG.info("Check if user exists.");
        try {
            Client restClient = ClientBuilder.newClient();
            Response r = restClient
                    .target("/v1/users/" + userId)
                    .request(MediaType.APPLICATION_JSON).buildGet().invoke();

            System.out.println(r.getStatus());
            if (r.getStatus() > 400) {
                throw new WebApplicationException();
            }
            return true;
        }
        catch (Exception e) {
            LOG.severe("Users unavailable");
            throw new InternalServerErrorException(e);
        }
    }

    public boolean checkUserExistsFallback(int userId) {
        LOG.info("checkUserExistsFallback");
        return false;
    }

    @DiscoverService(value = "uniborrow-users-service", environment = "dev", version = "1.0.0")
    public boolean markRequested(Integer itemId) {
        Response response = webTarget.path("/v1/items").path(itemId.toString()).request(MediaType.APPLICATION_JSON).buildGet().invoke();
        Item item = response.readEntity(Item.class);
        item.status = "Requested";
        response = webTarget.path("/v1/items").path(itemId.toString()).request(MediaType.APPLICATION_JSON).buildPut(Entity.entity(item, MediaType.APPLICATION_JSON)).invoke();
        return response.getStatus() != 404;
    }

}
