package cl.duoc.cloudstore.order.service;

import cl.duoc.cloudstore.order.model.CustomerOrder;
import cl.duoc.cloudstore.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @CircuitBreaker(name = "paymentCircuitBreaker", fallbackMethod = "fallbackProcessPayment")
    public String createAndProcessOrder(Long productId, Double amount) {
        CustomerOrder order = new CustomerOrder();
        order.setProductId(productId);
        order.setAmount(amount);
        order.setStatus("PROCESSING");
        repository.save(order);

        String paymentUrl = "http://localhost:8083/api/v1/payments/process";
        PaymentCallDto dto = new PaymentCallDto(order.getId(), order.getAmount());
        restTemplate.postForObject(paymentUrl, dto, String.class);

        order.setStatus("CONFIRMED");
        repository.save(order);
        return "Orden #" + order.getId() + " aprobada exitosamente.";
    }

    public String fallbackProcessPayment(Long productId, Double amount, Throwable t) {
        return "Servicio de pagos con latencia/caído. Orden registrada como PENDIENTE.";
    }
}

record PaymentCallDto(Long orderId, Double amount) {}