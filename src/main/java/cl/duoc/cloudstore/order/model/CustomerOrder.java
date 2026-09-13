package cl.duoc.cloudstore.order.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Double amount;
    private String status;

    public CustomerOrder() {}
    public Long getId() { return id; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setStatus(String status) { this.status = status; }
    public Double getAmount() { return amount; }
}