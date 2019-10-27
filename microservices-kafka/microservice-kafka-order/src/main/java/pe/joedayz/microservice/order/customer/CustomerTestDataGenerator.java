package pe.joedayz.microservice.order.customer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CustomerTestDataGenerator {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerTestDataGenerator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void generateTestData() {
        customerRepository
                .save(new Customer("Jose", "Diaz", "jose.diaz@joedayz.pe",
                        "Victor Alzamora 147, Santa Catalina", "La Victoria"));
        customerRepository.save(new Customer("Miryan", "Ramirez", "miryan.ramirez@joedayz.pe",
                "Mz Lt 29, Urb. Los Proceres", "Ventanilla"));
    }

}
