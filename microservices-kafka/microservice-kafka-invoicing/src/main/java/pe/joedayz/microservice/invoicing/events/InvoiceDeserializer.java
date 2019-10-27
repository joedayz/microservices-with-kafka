package pe.joedayz.microservice.invoicing.events;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import pe.joedayz.microservice.invoicing.Invoice;


public class InvoiceDeserializer extends JsonDeserializer<Invoice> {

    public InvoiceDeserializer() {
        super(Invoice.class);
    }

}
