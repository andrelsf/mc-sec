package br.dev.multicode.services.impl;

import br.dev.multicode.entities.OrderSecEvent;
import br.dev.multicode.models.OrderMessage;
import br.dev.multicode.repositories.OrderSecEventRepository;
import br.dev.multicode.services.OrderSecEventService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderSecEventServiceImpl implements OrderSecEventService {

  @Inject
  OrderSecEventRepository repository;

  @Override
  public void create(OrderMessage orderMessage)
  {
    repository.save(OrderSecEvent.of(orderMessage));
  }
}
