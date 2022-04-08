package br.dev.multicode.services;

import br.dev.multicode.models.CurrentOrderStatus;
import br.dev.multicode.models.OrderPaymentMessage;

public interface NotificationService {

  void doNotificationOrderService(CurrentOrderStatus currentOrderStatus);
  void doNotificationOrderPayment(OrderPaymentMessage toOrderPaymentMessage);

}
