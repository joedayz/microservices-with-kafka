package pe.joedayz.microservice.invoicing;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceTestApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class InvoiceWebIntegrationTest {

    @LocalServerPort
    private int serverPort;

    private RestTemplate restTemplate = new RestTemplate();

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, false, "order");

    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getEmbeddedKafka().getBrokersAsString());
    }

    @Test
    public void isHTMLReturned() {
        String body = getForMediaType(String.class, MediaType.TEXT_HTML, shippingURL());

        assertThat(body, containsString("<div"));
    }

    private String shippingURL() {
        return "http://localhost:" + serverPort;
    }

    private <T> T getForMediaType(Class<T> value, MediaType mediaType, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(mediaType));

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<T> resultEntity = restTemplate.exchange(url, HttpMethod.GET, entity, value);

        return resultEntity.getBody();
    }

}
