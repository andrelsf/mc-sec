package br.dev.multicode.services.kafka.consumers;

import br.dev.multicode.entities.OrderSecEvent;
import br.dev.multicode.enums.OrderStatus;
import br.dev.multicode.models.CurrentOrderStatus;
import br.dev.multicode.models.OrderProcessingStatus;
import br.dev.multicode.services.OrderSecEventService;
import br.dev.multicode.services.kafka.producers.OrderPaymentProducer;
import br.dev.multicode.services.kafka.producers.OrderStatusProducer;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderResponseStatusConsumer {

  private final Logger log = Logger.getLogger(this.getClass());

  @Inject OrderStatusProducer orderStatusProducer;
  @Inject OrderPaymentProducer orderPaymentProducer;
  @Inject OrderSecEventService orderSecEventService;

  @Incoming("sec-response-status")
  public CompletionStage<Void> receiveOrderStatusResponseFromKafka(Message<OrderProcessingStatus> orderProcessingStatus)
  {
    var metadata = orderProcessingStatus.getMetadata(IncomingKafkaRecordMetadata.class)
        .orElseThrow();

    OrderProcessingStatus orderProcessingStatusReceived = orderProcessingStatus.getPayload();
    log.infof("SEC: %s - Got a order message: orderId=%s :: Status=%s", metadata.getTopic(),
        orderProcessingStatusReceived.getOrderId(), orderProcessingStatusReceived.getStatus());

    final CurrentOrderStatus currentOrderStatus = new CurrentOrderStatus(
        orderProcessingStatusReceived.getOrderId(), orderProcessingStatusReceived.getStatus());

    orderStatusProducer.doNotification(currentOrderStatus);

    final OrderSecEvent orderSecEvent = orderSecEventService.updateStatus(currentOrderStatus);
    if (orderProcessingStatusReceived.getStatus().equals(OrderStatus.RESERVED_PRODUCTS)) {
      orderPaymentProducer.doNotification(orderSecEvent.toOrderPaymentMessage());
    }

    return orderProcessingStatus.ack();
  }

}
