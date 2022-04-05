package br.dev.multicode.repositories;

import br.dev.multicode.entities.OrderSecEvent;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderSecEventRepository implements PanacheRepository<OrderSecEvent> {

  @Transactional
  public void save(final OrderSecEvent orderSecEvent)
  {
    this.persistAndFlush(orderSecEvent);
  }

}
