package pe.joedayz.microservice.shipping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShippingTestApp.class, webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
public class ShipmentKafkaTest {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Autowired
	public KafkaTemplate<String, String> kafkaTemplate;

	@ClassRule
	public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, false, "order");

	@BeforeClass
	public static void setUpBeforeClass() {
		System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getEmbeddedKafka().getBrokersAsString());
	}

	@Test
	public void orderAreReceived() throws Exception {
		long countBefore = shipmentRepository.count();
		kafkaTemplate.send("order", order());
		int i = 0;
		while (shipmentRepository.count() == countBefore && i < 10) {
			Thread.sleep(1000);
			i++;
		}
		assertThat(shipmentRepository.count(), is(greaterThan(countBefore)));
	}

	private String order() {
		return "{" +
				"  \"id\" : 1," +
				"  \"customer\" : {" +
				"    \"customerId\" : 1," +
				"    \"name\" : \"Jose\"," +
				"    \"firstname\" : \"Diaz\"," +
				"    \"email\" : \"jamdiazdiaz@gmail.com\"," +
				"    \"street\" : \"Mz P Lt 29, Urb. Los Proceres\"," +
				"    \"city\" : \"Ventanilla\"" +
				"  }," +
				"  \"updated\" : \"2017-04-20T15:42:12.351+0000\"," +
				"  \"shippingAddress\" : {" +
				"    \"street\" : \"Urb. Los Licenciados Mz T5 Lt 1\"," +
				"    \"zip\" : \"10999\"," +
				"    \"city\" : \"Ventanilla\"" +
				"  }," +
				"  \"billingAddress\" : {" +
				"    \"street\" : \"Urb. Los Licenciados Mz T5 Lt 1\"," +
				"    \"zip\" : \"10999\"," +
				"    \"city\" : \"Ventanilla\"" +
				"  }," +
				"  \"orderLine\" : [ {" +
				"    \"count\" : 42," +
				"    \"item\" : {" +
				"      \"itemId\" : 1," +
				"      \"name\" : \"iPod\"," +
				"      \"price\" : 42.0" +
				"    }" +
				"  } ]," +
				"  \"numberOfLines\" : 1," +
				"  \"_links\" : {" +
				"    \"self\" : {" +
				"      \"href\" : \"http://localhost:8080/order/1\"" +
				"    }," +
				"    \"order\" : {" +
				"      \"href\" : \"http://localhost:8080/order/1\"" +
				"    }" +
				"  }" +
				"}";
	}

}
