package pe.joedayz.microservice.order.logic;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pe.joedayz.microservice.order.OrderApp;
import pe.joedayz.microservice.order.customer.Customer;
import pe.joedayz.microservice.order.customer.CustomerRepository;
import pe.joedayz.microservice.order.item.Item;
import pe.joedayz.microservice.order.item.ItemRepository;

import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderWebIntegrationTest {

    private RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort
    private long serverPort;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Item item;

    private Customer customer;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, false, "order");

    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getEmbeddedKafka().getBrokersAsString());
    }

    @Before
    public void setup() {
        item = itemRepository.findAll().iterator().next();
        customer = new Customer("Daniel", "Ramirez", "daniel@joedayz.pe",
                "Urb. Los Licenciados, Mz T5 Lt 1", "Ventanilla");
        customer = customerRepository.save(customer);
    }

    @Test
    public void IsOrderListReturned() {
        try {
            Iterable<Order> orders = orderRepository.findAll();
            assertTrue(StreamSupport.stream(orders.spliterator(), false)
                    .noneMatch(o -> ((o.getCustomer() != null) && (o.getCustomer().equals(customer)))));
            ResponseEntity<String> resultEntity = restTemplate.getForEntity(orderURL(), String.class);
            assertTrue(resultEntity.getStatusCode().is2xxSuccessful());
            String orderList = resultEntity.getBody();
            assertFalse(orderList.contains("Daniel"));
            Order order = new Order(customer);
            order.addLine(42, item);
            orderRepository.save(order);
            orderList = restTemplate.getForObject(orderURL(), String.class);
            assertTrue(orderList.contains("Diaz"));
        } finally {
            orderRepository.deleteAll();
        }
    }

    private String orderURL() {
        return "http://localhost:" + serverPort;
    }



    @Test
    public void IsOrderFormDisplayed() {
        ResponseEntity<String> resultEntity = restTemplate.getForEntity(orderURL() + "/form.html", String.class);
        assertTrue(resultEntity.getStatusCode().is2xxSuccessful());
        assertTrue(resultEntity.getBody().contains("<form"));
    }


    @Test
    public void IsSubmittedOrderSaved() {
        long before = orderRepository.count();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("submit", "");
        map.add("customer", Long.toString(customer.getCustomerId()));
        map.add("orderLine[0].item", Long.toString(item.getItemId()));
        map.add("orderLine[0].count", "42");
        restTemplate.postForLocation(orderURL(), map, String.class);
        assertEquals(before + 1, orderRepository.count());
    }
}
