package nire.inventory.inventory.project.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nire.inventory.inventory.project.constants.ItemConstants;
import nire.inventory.inventory.project.domain.Item;
import nire.inventory.inventory.project.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/store")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value = "/inventory")
    public ResponseEntity<?> getInventoryItems(){
            return ResponseEntity.ok().body(itemService.getInventoryItems());

    }

    @GetMapping(value = "/item{id}")
    public ResponseEntity<Item> getItemByItemNumber(@RequestParam("id") Long itemNumber) {
        return Optional.ofNullable(itemService.getItemByItemNumber(itemNumber)).map(item -> ResponseEntity.ok().body(item))
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/withdraw/{id}")
    public ResponseEntity withdrawAnAmountOfItemsById(@PathVariable(value = "id")  Long itemNumber, @RequestBody String amountStr) {
        ResponseEntity responseEntity;
        Integer amount = getAmountAsInteger(amountStr);

        int withdrawalResponse = itemService.withdrawAnAmountOfItemsById(itemNumber, amount);
        if (ItemConstants.AMOUNT_MUST_BE_BIGGER_THAN_ZERO == withdrawalResponse){
            responseEntity = ResponseEntity.badRequest().body("amount to withdraw must be bigger than zero");
        } else if (ItemConstants.ITEM_IN_STOCK == withdrawalResponse){
            responseEntity = ResponseEntity.ok().body(amount + " items of item number " + itemNumber + " were withdrawn successfully");
        } else if (ItemConstants.ITEM_NOT_IN_STOCK == withdrawalResponse){
            responseEntity = ResponseEntity.ok().body("not sufficient amount of item number " + itemNumber + " in stock");
        } else {
            responseEntity = ResponseEntity.ok().body("no item with item number = " + itemNumber + " was found in stock");
        }
        return responseEntity;
    }

    @PutMapping(value = "/deposit/{id}")
    public ResponseEntity depositAnAmountOfItemsById(@PathVariable(value = "id") Long itemNumber, @RequestBody String amountStr) {
        ResponseEntity responseEntity;
        Integer amount = getAmountAsInteger(amountStr);


        int depositResponse = itemService.depositAnAmountOfItemsById(itemNumber, amount);
        if (ItemConstants.AMOUNT_MUST_BE_BIGGER_THAN_ZERO == depositResponse){
            responseEntity = ResponseEntity.badRequest().body("amount to deposit must be bigger than zero");
        } else if (ItemConstants.ITEM_IN_STOCK == depositResponse){
            responseEntity = ResponseEntity.ok().body(amount + " items of item number " + itemNumber + " deposited successfully");
        } else {
            responseEntity = ResponseEntity.ok().body("no item with item number = " + itemNumber + " was found in stock");
        }
        return responseEntity;
    }

    @PostMapping(value = "/createItem")
    public ResponseEntity<Item> addItemToStock(@RequestBody Item item){
        return new ResponseEntity<Item>(itemService.addItemToStock(item), HttpStatus.CREATED);
    }

    @DeleteMapping (value = "/deleteItem/{id}")
    public ResponseEntity deleteItemFromStock(@PathVariable(value = "id") Long itemNumber){
        boolean isDeleted = itemService.deleteItemFromStock(itemNumber);
        if (isDeleted){
            return new ResponseEntity(itemNumber, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    private Integer getAmountAsInteger(@RequestBody String amountStr) {
        Integer amount = null;
        LinkedHashMap<String, Integer> requestMap;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestMap = (LinkedHashMap<String, Integer>) mapper.readValue(amountStr, Object.class);
            Set<String> keys = requestMap.keySet();
            amount = requestMap.get(keys.iterator().next());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return amount;
    }
}
