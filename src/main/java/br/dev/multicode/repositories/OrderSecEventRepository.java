package br.dev.multicode.repositories;

import br.dev.multicode.entities.OrderSecEvent;
import br.dev.multicode.enums.OrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class OrderSecEventRepository implements PanacheRepository<OrderSecEvent> {

  @Transactional
  public void save(final OrderSecEvent orderSecEvent)
  {
    this.persistAndFlush(orderSecEvent);
  }

  @Transactional
  public OrderSecEvent updateStatusBy(final UUID orderId, final OrderStatus status)
  {
    OrderSecEvent orderSecEvent = this.find("order_id = :orderId",
            Parameters.with("orderId", orderId.toString()))
        .firstResultOptional()
        .orElseThrow(() ->
            new NotFoundException("Order not found by id=".concat(orderId.toString())));

    orderSecEvent.setStatus(status);
    return orderSecEvent;
  }
}
