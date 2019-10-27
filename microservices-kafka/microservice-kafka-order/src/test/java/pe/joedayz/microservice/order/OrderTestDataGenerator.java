package pe.joedayz.microservice.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pe.joedayz.microservice.order.customer.CustomerRepository;
import pe.joedayz.microservice.order.item.ItemRepository;
import pe.joedayz.microservice.order.logic.Address;
import pe.joedayz.microservice.order.logic.Order;
import pe.joedayz.microservice.order.logic.OrderRepository;


import javax.annotation.PostConstruct;

@Component
@Profile("test")
@DependsOn({ "itemTestDataGenerator", "customerTestDataGenerator" })
public class OrderTestDataGenerator {

    private final OrderRepository orderRepository;
    private ItemRepository itemRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public OrderTestDataGenerator(OrderRepository orderRepository, ItemRepository itemRepository,
                                  CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void generateTestData() {
        Order order = createOrder();
        orderRepository.save(order);
    }

    public Order createOrder() {
        Order order = new Order(customerRepository.findAll().iterator().next());
        order.setShippingAddress(new Address("Victor Alzamora 147, La Victoria", "Lima19", "La Victoria"));
        order.setBillingAddress(new Address("Mz P Lt 29, Urb. Los Proceres", "Callao06", "Ventanilla"));
        order.addLine(42, itemRepository.findAll().iterator().next());
        return order;
    }

}