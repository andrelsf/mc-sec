package br.dev.multicode.entities;

import br.dev.multicode.enums.OrderStatus;
import br.dev.multicode.models.OrderMessage;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_sec_event")
public class OrderSecEvent extends PanacheEntityBase {

  @Id
  @Column(name = "event_id", length = 37)
  private String eventId;

  @Column(name = "order_id", nullable = false, length = 37)
  private String orderId;

  @Column(name = "user_id", nullable = false, length = 37)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private OrderStatus status;

  @Column(nullable = false)
  private BigDecimal price;

  public static OrderSecEvent of(OrderMessage orderMessage) {
    return OrderSecEvent.builder()
        .eventId(orderMessage.getEventId().toString())
        .orderId(orderMessage.getOrderId().toString())
        .userId(orderMessage.getUserId().toString())
        .status(orderMessage.getStatus())
        .price(orderMessage.getPrice())
        .build();
  }
}
