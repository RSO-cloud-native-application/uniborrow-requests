package si.fri.rso.uniborrow.items.services.beans;

import si.fri.rso.uniborrow.items.lib.Item;
import si.fri.rso.uniborrow.items.models.converters.ItemConverter;
import si.fri.rso.uniborrow.items.models.entities.ItemEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class ItemBean {

    private final Logger log = Logger.getLogger(ItemBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Item> getItems() {
        TypedQuery<ItemEntity> query =
                em.createNamedQuery("ItemEntity.getAll", ItemEntity.class);
        List<ItemEntity> resultList = query.getResultList();
        return resultList.stream().map(ItemConverter::toDto).collect(Collectors.toList());
    }

    public Item getItem(Integer id) {
        ItemEntity itemEntity = em.find(ItemEntity.class, id);
        if (itemEntity == null) {
            throw new NotFoundException();
        }
        Item item = ItemConverter.toDto(itemEntity);
        return item;
    }
}