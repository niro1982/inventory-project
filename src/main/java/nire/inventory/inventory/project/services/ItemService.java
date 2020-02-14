package nire.inventory.inventory.project.services;

import nire.inventory.inventory.project.domain.Item;

import java.util.List;

public interface ItemService {

    List<Item> getInventoryItems();
    Item getItemByItemNumber(Long itemNumber);
    int withdrawAnAmountOfItemsById(Long itemNumber, int amount);
    int depositAnAmountOfItemsById(Long itemNumber, int amount);
    Item addItemToStock(Item item);
    boolean deleteItemFromStock(Long itemNumber);
}
