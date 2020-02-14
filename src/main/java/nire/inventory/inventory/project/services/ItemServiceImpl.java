package nire.inventory.inventory.project.services;

import nire.inventory.inventory.project.constants.ItemConstants;
import nire.inventory.inventory.project.domain.Item;
import nire.inventory.inventory.project.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getInventoryItems() {
        List<Item> itemList = new ArrayList<>();
        itemRepository.findAll().iterator().forEachRemaining(itemList::add);
        return itemList;
    }

    @Override
    public Item getItemByItemNumber(Long itemNumber) {
        Item returnedItem = null;
        Optional<Item> optionalItem = itemRepository.findByItemNumber(itemNumber);
        if (optionalItem.isPresent()){
            returnedItem = optionalItem.get();
        }
        return returnedItem;
    }

    @Override
    public int withdrawAnAmountOfItemsById(Long itemNumber, int amount) {
        if (amount <= 0) return ItemConstants.AMOUNT_MUST_BE_BIGGER_THAN_ZERO;

        int isItemQuantityInStock = ItemConstants.ITEM_NOT_FOUND;
        Optional<Item> OptionalItem = itemRepository.findByItemNumber(itemNumber);
        if (OptionalItem.isPresent()){
            Item retrievedItem = OptionalItem.get();
            Integer amountInStock = retrievedItem.getAmount();
            if (amountInStock >= amount){
                isItemQuantityInStock = ItemConstants.ITEM_IN_STOCK;
                retrievedItem.setAmount(amountInStock-amount);
                itemRepository.save(retrievedItem);
            } else {
                isItemQuantityInStock = ItemConstants.ITEM_NOT_IN_STOCK;
            }
        }
        return isItemQuantityInStock;
    }

    @Override
    public int depositAnAmountOfItemsById(Long itemNumber, int amount) {
        if (amount <= 0) return ItemConstants.AMOUNT_MUST_BE_BIGGER_THAN_ZERO;

        int isItemQuantityInStock = ItemConstants.ITEM_NOT_FOUND;
        Optional<Item> OptionalItem = itemRepository.findByItemNumber(itemNumber);
        if (OptionalItem.isPresent()) {
            Item retrievedItem = OptionalItem.get();
            Integer amountInStock = retrievedItem.getAmount();
            retrievedItem.setAmount(amountInStock + amount);
            isItemQuantityInStock = ItemConstants.ITEM_IN_STOCK;
            itemRepository.save(retrievedItem);
        }
        return isItemQuantityInStock;
    }

    @Override
    public Item addItemToStock(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public boolean deleteItemFromStock(Long itemNumber) {
        boolean isDeleted = false;
        if (itemRepository.existsById(itemNumber)) {
            itemRepository.deleteById(itemNumber);
            isDeleted = true;
        }
        return isDeleted;
    }
}
