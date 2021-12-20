package si.fri.rso.uniborrow.requests.services.beans;

import si.fri.rso.uniborrow.requests.lib.Request;
import si.fri.rso.uniborrow.requests.models.converters.RequestConverter;
import si.fri.rso.uniborrow.requests.models.entities.RequestEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class RequestBean {

    private final Logger log = Logger.getLogger(RequestBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Request> getRequests() {
        TypedQuery<RequestEntity> query =
                em.createNamedQuery("RequestEntity.getAll", RequestEntity.class);
        List<RequestEntity> resultList = query.getResultList();
        return resultList.stream().map(RequestConverter::toDto).collect(Collectors.toList());
    }

    public Request getRequest(Integer id) {
        RequestEntity requestEntity = em.find(RequestEntity.class, id);
        if (requestEntity == null) {
            throw new NotFoundException();
        }
        Request request = RequestConverter.toDto(requestEntity);
        return request;
    }

    public Request createRequest(Request request) {
        RequestEntity requestEntity = RequestConverter.toEntity(request);
        try {
            beginTransaction();
            em.persist(requestEntity);
            commitTransaction();
        } catch (Exception e) {
            log.warning(e.getMessage());
            rollbackTransaction();
        }

        if (requestEntity.getId() == null) {
            log.warning("Failed to create a request!");
            return null;
        }
        return RequestConverter.toDto(requestEntity);
    }

    public Request putRequest(Request request, Integer id) {
        RequestEntity requestEntity = em.find(RequestEntity.class, id);
        if (requestEntity == null) {
            return null;
        }

        RequestEntity updatedRequestEntity = RequestConverter.toEntity(request);
        try {
            beginTransaction();
            updatedRequestEntity.setId(requestEntity.getId());
            updatedRequestEntity = em.merge(updatedRequestEntity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            log.warning(e.getMessage());
            return null;
        }
        return RequestConverter.toDto(updatedRequestEntity);
    }

    public Request patchRequest(Request request, Integer id) {
        RequestEntity requestEntity = em.find(RequestEntity.class, id);
        if (requestEntity == null) {
            return null;
        }

        RequestEntity updatedRequestEntity = RequestConverter.toEntity(request);
        try {
            beginTransaction();
            if (updatedRequestEntity.getMessage() == null) {
                updatedRequestEntity.setMessage(requestEntity.getMessage());
            }
            if (updatedRequestEntity.getPrice() == null) {
                updatedRequestEntity.setPrice(requestEntity.getPrice());
            }
            if (updatedRequestEntity.getTimestampStart() == null) {
                updatedRequestEntity.setTimestampStart(requestEntity.getTimestampStart());
            }
            if (updatedRequestEntity.getTimestampEnd() == null) {
                updatedRequestEntity.setTimestampEnd(requestEntity.getTimestampEnd());
            }
            updatedRequestEntity.setId(requestEntity.getId());
            updatedRequestEntity = em.merge(updatedRequestEntity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            log.warning(e.getMessage());
            return null;
        }
        return RequestConverter.toDto(updatedRequestEntity);
    }

    public boolean deleteRequest(Integer id) {
        RequestEntity requestEntity = em.find(RequestEntity.class, id);
        if (requestEntity == null) {
            return false;
        }
        try {
            beginTransaction();
            em.remove(requestEntity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            log.warning(e.getMessage());
            return false;
        }
        return true;
    }

    private void beginTransaction() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private  void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}