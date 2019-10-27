package pe.joedayz.microservice.shipping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShippingTestApp.class, webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
public class ShippingServiceTest {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Autowired
	private ShipmentService shipmentService;

	@Test
	public void ensureIdempotencySecondCallIgnored() {
		long countBefore = shipmentRepository.count();
		Shipment shipment = new Shipment(42L,
				new Customer(23L, "Jose", "Diaz"),
				new Date(0L), new Address("Mz P Lt 29, Urb. Los Proceres", "40789", "Ventanilla"),
				new ArrayList<ShipmentLine>());
		shipmentService.ship(shipment);
		assertThat(shipmentRepository.count(), is(countBefore + 1));
		assertThat(shipmentRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
		shipment = new Shipment(42,
				new Customer(23L, "Jose", "Diaz"),
				new Date(), new Address("Mz P Lt 29, Urb. Los Proceres", "40789", "Ventanilla"), new ArrayList<ShipmentLine>());
		shipmentService.ship(shipment);
		assertThat(shipmentRepository.count(), is(countBefore + 1));
		assertThat(shipmentRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
	}

}
