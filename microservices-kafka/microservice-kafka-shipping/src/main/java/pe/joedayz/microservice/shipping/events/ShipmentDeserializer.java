package pe.joedayz.microservice.shipping.events;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import pe.joedayz.microservice.shipping.Shipment;


public class ShipmentDeserializer extends JsonDeserializer<Shipment> {

	public ShipmentDeserializer() {
		super(Shipment.class);
	}

}
