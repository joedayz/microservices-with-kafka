package pe.joedayz.microservice.invoicing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceTestApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class InvoicingServiceTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Test
    public void ensureIdempotencySecondCallIgnored() {
        long countBefore = invoiceRepository.count();
        Invoice invoice = new Invoice(42,
                new Customer(23, "Jose", "Diaz", "jamdiazdiaz@gmail.com"),
                new Date(0L), new Address("Mz P LT 29", "201", "Urb. Los Proceres"), new ArrayList<InvoiceLine>());
        invoiceService.generateInvoice(invoice);
        assertThat(invoiceRepository.count(), is(countBefore + 1));
        assertThat(invoiceRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
        invoice = new Invoice(42,
                new Customer(23, "Jose", "Diaz", "jamdiazdiaz@gmail.com"),
                new Date(), new Address("Mz P LT 29", "201", "Urb. Los Proceres"), new ArrayList<InvoiceLine>());
        invoiceService.generateInvoice(invoice);
        assertThat(invoiceRepository.count(), is(countBefore + 1));
        assertThat(invoiceRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
    }

}
