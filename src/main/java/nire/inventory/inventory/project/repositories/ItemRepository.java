package nire.inventory.inventory.project.repositories;

import nire.inventory.inventory.project.domain.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ItemRepository extends CrudRepository<Item, Long> {

    Optional<Item> findByItemNumber(Long itemNumber);







}
