package nire.inventory.inventory.project.bootstrap;

import nire.inventory.inventory.project.domain.Item;
import nire.inventory.inventory.project.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be used to initialize the data base on application startup with some data
 * that we can work with
 */
@Component
public class ItemBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemBootstrap(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        itemRepository.saveAll(getItems());
    }

    private List<Item> getItems(){
        List<Item> itemList = new ArrayList<>();
        Item itemChair = new Item("Chair", 10, "A11111");
        Item itemTable = new Item("Table", 15, "B22222");
        Item itemCabinet = new Item("Cabinet", 20, "C33333");
        Item itemSofa = new Item("Sofa", 5, "D44444");
        Item itemStool = new Item("Stool", 3, "E55555");

        itemList.add(itemChair);
        itemList.add(itemTable);
        itemList.add(itemCabinet);
        itemList.add(itemSofa);
        itemList.add(itemStool);

        return itemList;

    }
}
