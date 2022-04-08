package br.dev.multicode.services.impl;

import br.dev.multicode.models.CurrentOrderStatus;
import br.dev.multicode.models.OrderPaymentMessage;
import br.dev.multicode.services.NotificationService;
import br.dev.multicode.services.kafka.producers.OrderPaymentProducer;
import br.dev.multicode.services.kafka.producers.OrderStatusProducer;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {

  @Inject OrderStatusProducer orderStatusProducer;
  @Inject OrderPaymentProducer orderPaymentProducer;

  @Override
  public void doNotificationOrderService(CurrentOrderStatus currentOrderStatus)
  {
    Uni.createFrom()
        .item(currentOrderStatus)
        .emitOn(Infrastructure.getDefaultWorkerPool())
        .subscribe()
        .with(orderStatusProducer::sendToKafka, Throwable::getMessage);
  }

  @Override
  public void doNotificationOrderPayment(OrderPaymentMessage orderPaymentMessage)
  {
    Uni.createFrom()
        .item(orderPaymentMessage)
        .emitOn(Infrastructure.getDefaultWorkerPool())
        .subscribe()
        .with(orderPaymentProducer::sendOrderPaymentToKafka, Throwable::getMessage);
  }
}
