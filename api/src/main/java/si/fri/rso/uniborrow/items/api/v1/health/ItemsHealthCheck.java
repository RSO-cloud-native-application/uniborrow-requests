package si.fri.rso.uniborrow.items.api.v1.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import si.fri.rso.uniborrow.items.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class ItemsHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {
        if (restProperties.getBroken()) {
            return HealthCheckResponse.down(ItemsHealthCheck.class.getSimpleName());
        }
        else {
            return HealthCheckResponse.up(ItemsHealthCheck.class.getSimpleName());
        }
    }
}