package pe.joedayz.microservice.order.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pe.joedayz.microservice.order.customer.CustomerRepository;
import pe.joedayz.microservice.order.item.ItemRepository;



@Controller
public class OrderController {


    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private ItemRepository itemRepository;
    private OrderService orderService;


    @Autowired
    private OrderController(OrderService orderService, OrderRepository orderRepository,
                            CustomerRepository customerRepository, ItemRepository itemRepository) {
        super();
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.orderService = orderService;
    }


    @RequestMapping("/")
    public ModelAndView orderList() {
        return new ModelAndView("orderlist", "orders", orderRepository.findAll());
    }


    @RequestMapping(value = "/form.html", method = RequestMethod.GET)
    public ModelAndView form() {
        return new ModelAndView("orderForm", "order", new Order());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView post(Order order) {
        order = orderService.order(order);
        return new ModelAndView("success");
    }
}
