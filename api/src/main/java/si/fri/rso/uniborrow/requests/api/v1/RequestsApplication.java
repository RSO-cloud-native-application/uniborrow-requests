package si.fri.rso.uniborrow.requests.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import com.kumuluz.ee.discovery.annotations.RegisterService;

@RegisterService(value = "uniborrow-requests-service", environment = "dev", version = "1.0.0")
@ApplicationPath("/v1")
public class RequestsApplication extends Application {

}
