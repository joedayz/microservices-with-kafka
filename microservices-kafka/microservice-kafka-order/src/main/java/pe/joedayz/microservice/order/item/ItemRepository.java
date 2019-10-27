package pe.joedayz.microservice.order.item;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(exported = false)
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
}
