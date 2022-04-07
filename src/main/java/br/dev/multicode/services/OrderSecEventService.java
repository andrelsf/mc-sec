package br.dev.multicode.services;

import br.dev.multicode.entities.OrderSecEvent;
import br.dev.multicode.models.CurrentOrderStatus;
import br.dev.multicode.models.OrderMessage;

public interface OrderSecEventService {

  void create(OrderMessage orderMessage);

  OrderSecEvent updateStatus(CurrentOrderStatus currentOrderStatus);
}
