package pe.joedayz.microservice.order.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ItemTestDataGenerator {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemTestDataGenerator(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PostConstruct
    public void generateTestData() {
        itemRepository.save(new Item("Diplomado Java", 1000.00));
        itemRepository.save(new Item("Diplomado NET", 1200.00));
        itemRepository.save(new Item("OCA JAVA 11", 1000.00));
        itemRepository.save(new Item("Angular", 500.00));
    }

}
