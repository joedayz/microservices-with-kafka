package pe.joedayz.microservice.order.logic;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pe.joedayz.microservice.order.OrderApp;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    public void lastCreatedIsUpdated() {
        Order order = new Order();
        order = orderRepository.save(order);
        assertEquals(order.getUpdated(), orderRepository.lastUpdate());
        order = new Order();
        order = orderRepository.save(order);
        assertEquals(order.getUpdated(), orderRepository.lastUpdate());
    }

}
